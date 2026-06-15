package com.pahappa.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Server-side proxy for the GSS AI chat API.
 *
 * The browser cannot call node.gss-tec.com directly when served from
 * localhost because browser extensions (ad blockers, privacy tools) intercept
 * and kill the cross-origin request before a response arrives.
 *
 * This controller accepts the conversation history from the widget, forwards
 * it to the GSS Cloudflare Worker on the server side (no extension can touch
 * server-to-server HTTP), and returns the AI reply as plain JSON.
 *
 * Endpoint: POST /julia/chat  — public, no login required.
 */
@Controller
@RequestMapping("/julia")
public class JuliaChatController {

    @Value("${gss.ai.api.key}")
    private String apiKey;

    @Value("${gss.ai.cf.worker.url}")
    private String cfWorkerUrl;

    @Value("${gss.ai.hf.engine.url}")
    private String hfEngineUrl;

    @Value("${gss.ai.model}")
    private String model;

    @Value("${gss.ai.enabled:true}")
    private boolean enabled;

    private final RestTemplate restTemplate = new RestTemplate();
    private String cachedToken = null;
    private long tokenExpiry = 0;

    /**
     * Serve the diagnostics page
     */
    @GetMapping("/diagnostics")
    public String diagnosticsPage() {
        return "julia-diagnostics";
    }

    /**
     * Get configuration info for diagnostics
     */
    @GetMapping("/config")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("endpoint", cfWorkerUrl);
        config.put("model", model);
        config.put("hasApiKey", apiKey != null && !apiKey.isEmpty());
        config.put("enabled", enabled);
        return ResponseEntity.ok(config);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Julia AI Chat");
        health.put("enabled", enabled);
        health.put("timestamp", System.currentTimeMillis());
        
        if (!enabled) {
            health.put("message", "Service is disabled in configuration");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
        
        if (apiKey == null || apiKey.isEmpty()) {
            health.put("message", "API key not configured");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
        
        health.put("message", "Service is operational");
        return ResponseEntity.ok(health);
    }

    /**
     * Proxy the chat request to the GSS Cloudflare Worker.
     *
     * Request body: { "messages": [ { "role": "user"|"assistant"|"system", "content": "..." }, ... ] }
     * Response:     { "reply": "..." }  or  { "error": "..." }
     */
    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> chat(
            @RequestBody Map<String, Object> body) {

        Map<String, Object> result = new HashMap<>();

        try {
            System.out.println("=== Julia Chat Request Started ===");
            
            @SuppressWarnings("unchecked")
            List<Map<String, String>> messages =
                    (List<Map<String, String>>) body.get("messages");

            if (messages == null || messages.isEmpty()) {
                result.put("error", "No messages provided.");
                return ResponseEntity.badRequest().body(result);
            }

            System.out.println("Messages received: " + messages.size());

            // Step 1: Get JWT token from Cloudflare Worker (like SDK does)
            System.out.println("Getting JWT token...");
            String token = getOrRenewToken();
            System.out.println("JWT token obtained successfully");

            // Step 2: Build the chat request for HuggingFace Engine
            Map<String, Object> chatRequest = new HashMap<>();
            chatRequest.put("model", model);
            chatRequest.put("messages", messages);
            chatRequest.put("temperature", 0.7);
            chatRequest.put("max_tokens", 1024);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);  // Use JWT token, not API key

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(chatRequest, headers);

            // Step 3: Call HuggingFace Engine (not Cloudflare Worker)
            String engineUrl = hfEngineUrl.endsWith("/")
                    ? hfEngineUrl + "v1/chat/completions"
                    : hfEngineUrl + "/v1/chat/completions";

            System.out.println("Calling HuggingFace Engine at: " + engineUrl);
            
            ResponseEntity<Map> chatResponse = restTemplate.exchange(
                    engineUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            System.out.println("Response status: " + chatResponse.getStatusCode());
            
            Map<?, ?> responseBody = chatResponse.getBody();
            String reply = extractReply(responseBody);

            System.out.println("Reply extracted: " + (reply != null ? reply.substring(0, Math.min(50, reply.length())) + "..." : "null"));

            result.put("reply", reply);
            System.out.println("=== Julia Chat Request Completed Successfully ===");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("Julia chat proxy error: " + e.getMessage());
            e.printStackTrace();
            
            String errorMessage = "AI service error: " + e.getMessage();
            
            // Provide more specific error messages
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Connection refused")) {
                    errorMessage = "Cannot connect to GSS API at " + cfWorkerUrl + ". The service may be down.";
                } else if (e.getMessage().contains("UnknownHost")) {
                    errorMessage = "Cannot resolve GSS API hostname. Check your internet connection.";
                } else if (e.getMessage().contains("timeout")) {
                    errorMessage = "Connection to GSS API timed out. The service may be slow or unreachable.";
                } else if (e.getMessage().contains("401")) {
                    errorMessage = "Invalid API key. Please check your GSS_AI_API_KEY configuration.";
                } else if (e.getMessage().contains("403")) {
                    errorMessage = "Access forbidden. Your API key may not have the required permissions.";
                }
            }
            
            result.put("error", errorMessage);
            result.put("details", e.getClass().getSimpleName() + ": " + e.getMessage());
            result.put("endpoint", cfWorkerUrl);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(result);
        }
    }

    /**
     * Extract the assistant reply text from whatever structure the GSS Worker returns.
     * Handles both OpenAI-style { choices:[{message:{content:...}}] }
     * and simple { reply: "..." } or { content: "..." } shapes.
     */
    @SuppressWarnings("unchecked")
    private String extractReply(Map<?, ?> body) {
        if (body == null) return "No response from AI service.";

        // OpenAI-style choices array
        if (body.containsKey("choices")) {
            List<?> choices = (List<?>) body.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<?, ?> first = (Map<?, ?>) choices.get(0);
                if (first.containsKey("message")) {
                    Map<?, ?> msg = (Map<?, ?>) first.get("message");
                    return String.valueOf(msg.get("content"));
                }
                if (first.containsKey("text")) {
                    return String.valueOf(first.get("text"));
                }
            }
        }
        // Simple flat reply
        if (body.containsKey("reply"))   return String.valueOf(body.get("reply"));
        if (body.containsKey("content")) return String.valueOf(body.get("content"));
        if (body.containsKey("text"))    return String.valueOf(body.get("text"));

        return "Received an unexpected response format from the AI service.";
    }

    /**
     * Get or renew JWT token from Cloudflare Worker (mimics SDK behavior)
     */
    private String getOrRenewToken() throws Exception {
        System.out.println("getOrRenewToken() called");
        
        // Check if we have a valid cached token
        if (cachedToken != null && System.currentTimeMillis() / 1000 < tokenExpiry - 300) {
            System.out.println("Using cached token");
            return cachedToken;
        }

        System.out.println("Requesting new token from: " + cfWorkerUrl);
        
        // Request new token from /auth/lease endpoint
        String leaseUrl = cfWorkerUrl.endsWith("/")
                ? cfWorkerUrl + "auth/lease"
                : cfWorkerUrl + "/auth/lease";

        System.out.println("Lease URL: " + leaseUrl);

        Map<String, String> leaseRequest = new HashMap<>();
        leaseRequest.put("api_key", apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(leaseRequest, headers);

        System.out.println("Sending lease request...");
        
        ResponseEntity<Map> leaseResponse = restTemplate.exchange(
                leaseUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        System.out.println("Lease response status: " + leaseResponse.getStatusCode());

        Map<?, ?> leaseBody = leaseResponse.getBody();
        if (leaseBody == null || !leaseBody.containsKey("token")) {
            System.err.println("Lease response body: " + leaseBody);
            throw new Exception("Failed to obtain JWT token from auth/lease");
        }

        cachedToken = String.valueOf(leaseBody.get("token"));
        tokenExpiry = decodeTokenExpiry(cachedToken);

        System.out.println("Token obtained and cached. Expiry: " + tokenExpiry);

        return cachedToken;
    }

    /**
     * Decode JWT token expiry (mimics SDK behavior)
     */
    private long decodeTokenExpiry(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return 0;

            String payload = parts[1]
                    .replace('-', '+')
                    .replace('_', '/');

            byte[] decoded = java.util.Base64.getDecoder().decode(payload);
            String json = new String(decoded);

            // Simple JSON parsing for exp field
            int expIndex = json.indexOf("\"exp\":");
            if (expIndex == -1) return 0;

            String expStr = json.substring(expIndex + 6);
            expStr = expStr.split("[,}]")[0].trim();

            return Long.parseLong(expStr);
        } catch (Exception e) {
            return 0;
        }
    }
}
