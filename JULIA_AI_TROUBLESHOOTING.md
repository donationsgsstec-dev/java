# Julia AI Chat - Troubleshooting Guide

## 🔍 Understanding the Architecture

Your app **does NOT use the client-side GSS SDK**. Instead, it uses a **server-side proxy pattern** which is more reliable and secure.

### Current Architecture (WORKING)

```
Browser → /julia/chat → JuliaChatController → node.gss-tec.com → AI Response
```

**Benefits:**
- ✅ No CORS issues
- ✅ No browser extension interference
- ✅ API key stays secure on server
- ✅ Works reliably across all browsers

### Why SDK Approach Fails

The SDK code you mentioned:
```javascript
<script src="https://Gaston895-AI.hf.space/sdk/gss-sdk.js"></script>
```

**Fails because:**
1. ❌ SDK file doesn't exist at that URL
2. ❌ CORS blocks cross-origin requests from localhost
3. ❌ Browser extensions block the requests
4. ❌ Exposes API key in client-side code

---

## 🛠️ Diagnostics Tool

Access the diagnostics page at: **`http://localhost:8080/julia/diagnostics`**

This page will help you:
- ✅ Check configuration status
- ✅ Test connectivity
- ✅ Verify API key
- ✅ Send test messages
- ✅ Identify specific issues

**No login required** - The diagnostics page is publicly accessible for troubleshooting.

---

## 📋 Common Issues & Solutions

### 1. Connection Timeout

**Symptoms:**
- Chat widget shows "Connection error"
- Long delays before error message
- Server logs show timeout exceptions

**Solutions:**
```bash
# Check if GSS endpoint is accessible
curl -I https://node.gss-tec.com

# Verify environment variables are set
echo $GSS_AI_CF_WORKER_URL
echo $GSS_AI_API_KEY
```

**Fix:**
- Ensure `GSS_AI_CF_WORKER_URL=https://node.gss-tec.com` is set
- Check firewall/network settings
- Verify server has internet access

---

### 2. 401 Unauthorized

**Symptoms:**
- "AI service error: 401 Unauthorized"
- API key rejected

**Solutions:**
```properties
# In application-production.properties
gss.ai.api.key=gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ
```

**Fix:**
- Verify API key is correct
- Check if key has expired
- Ensure key has proper permissions
- Contact GSS support for key validation

---

### 3. No Response / Empty Reply

**Symptoms:**
- Chat sends but no response appears
- "No response from AI service"

**Solutions:**
```java
// Check JuliaChatController logs
// Look for response parsing issues
```

**Fix:**
- Verify model name is correct: `llama-3.1-8b-instant`
- Check if HuggingFace Space is running
- Increase timeout settings if needed
- Review server logs for parsing errors

---

### 4. CORS Errors (If Using SDK)

**Symptoms:**
- Browser console shows CORS errors
- "Access-Control-Allow-Origin" errors

**Solution:**
**DON'T use the SDK!** Your current server-side proxy already solves this.

---

### 5. Slow Response Times

**Symptoms:**
- Takes 10+ seconds to get response
- Typing indicator shows for long time

**Causes:**
- Cold start on HuggingFace Space
- Free tier rate limits
- Large conversation history

**Solutions:**
```javascript
// Limit conversation history in julia-chat-widget.jsp
if (juliaConversationHistory.length > 10) {
    // Keep system prompt + last 8 messages
    juliaConversationHistory = [
        juliaConversationHistory[0], // system prompt
        ...juliaConversationHistory.slice(-8)
    ];
}
```

---

## 🔧 Configuration Checklist

### Required Environment Variables

```bash
# Production (Railway/Render)
GSS_AI_API_KEY=gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ
GSS_AI_CF_WORKER_URL=https://node.gss-tec.com
GSS_AI_HF_ENGINE_URL=https://Gaston895-AI.hf.space
GSS_AI_MODEL=llama-3.1-8b-instant
GSS_AI_ENABLED=true
```

### Local Development

```properties
# src/main/resources/application.properties
gss.ai.api.key=gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ
gss.ai.cf.worker.url=https://node.gss-tec.com
gss.ai.hf.engine.url=https://Gaston895-AI.hf.space
gss.ai.model=llama-3.1-8b-instant
gss.ai.enabled=true
```

---

## 🧪 Testing Steps

### 1. Test Health Endpoint
```bash
curl http://localhost:8080/julia/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "Julia AI Chat",
  "enabled": true,
  "message": "Service is operational",
  "timestamp": 1234567890
}
```

### 2. Test Configuration
```bash
curl http://localhost:8080/julia/config
```

**Expected Response:**
```json
{
  "endpoint": "https://node.gss-tec.com",
  "model": "llama-3.1-8b-instant",
  "hasApiKey": true,
  "enabled": true
}
```

### 3. Test Chat Endpoint
```bash
curl -X POST http://localhost:8080/julia/chat \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {"role": "system", "content": "You are a helpful assistant."},
      {"role": "user", "content": "Say hello"}
    ]
  }'
```

**Expected Response:**
```json
{
  "reply": "Hello! How can I help you today?"
}
```

---

## 🎯 Quick Fix Checklist

When Julia chat isn't working:

- [ ] Visit `/julia/diagnostics` page
- [ ] Run all diagnostic tests
- [ ] Check `/julia/health` endpoint
- [ ] Verify environment variables are set
- [ ] Check server logs for errors
- [ ] Verify API key is valid
- [ ] Test network connectivity to GSS endpoint
- [ ] Check if HuggingFace Space is running
- [ ] Clear browser cache and cookies
- [ ] Try in incognito/private mode

---

## 💡 Why Your Current Approach is Better

| Feature | Server Proxy (Current) | Client SDK |
|---------|----------------------|------------|
| CORS Issues | ✅ None | ❌ Blocked |
| Browser Extensions | ✅ No interference | ❌ Blocked |
| API Key Security | ✅ Secure | ❌ Exposed |
| Reliability | ✅ High | ❌ Low |
| Debugging | ✅ Server logs | ❌ Browser only |
| Performance | ✅ Consistent | ⚠️ Variable |
| Maintenance | ✅ Easy | ❌ Complex |

**Recommendation:** Keep using the server-side proxy approach!

---

## 📝 Summary

Your app is **correctly implemented** using a server-side proxy. The SDK approach you mentioned would actually make things worse. If you're experiencing issues:

1. **Visit** `http://localhost:8080/julia/diagnostics`
2. **Run** all diagnostic tests
3. **Check** environment variables
4. **Verify** API key validity
5. **Test** network connectivity
6. **Review** server logs

The current architecture is production-ready and follows best practices for AI service integration.