<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Pahappa App</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://Gaston895-AI.hf.space/sdk/gss-sdk.js" async onerror="handleSDKError()"></script>
    <style>
        /* Global Styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        /* Container Styles */
        .container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            padding: 40px;
            width: 100%;
            max-width: 450px;
        }

        /* Header Styles */
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 10px;
            font-size: 28px;
        }

        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 30px;
            font-size: 14px;
        }

        /* Form Styles */
        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus,
        input[type="password"]:focus {
            outline: none;
            border-color: #667eea;
        }

        /* Alert Styles */
        .alert {
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-error {
            background-color: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }

        .alert-success {
            background-color: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }

        /* Button Styles */
        .btn {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-primary {
            background-color: #667eea;
            color: white;
        }

        .btn-primary:hover {
            background-color: #5568d3;
        }

        /* Link Styles */
        .register-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }

        .register-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .register-link a:hover {
            text-decoration: underline;
        }

        /* Remember Me Checkbox */
        .remember-me {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .remember-me input[type="checkbox"] {
            margin-right: 8px;
            width: 16px;
            height: 16px;
            cursor: pointer;
        }

        .remember-me label {
            margin-bottom: 0;
            cursor: pointer;
            font-weight: normal;
        }

        /* Responsive Design */
        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h2 {
                font-size: 24px;
            }
        }

        /* Chat Widget Styles with Amazing Glow Effects */
        .chat-widget {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 350px;
            max-height: 500px;
            background: linear-gradient(145deg, #ffffff 0%, #f8f9ff 100%);
            border-radius: 20px;
            box-shadow:
                0 20px 60px rgba(102, 126, 234, 0.4),
                0 0 40px rgba(118, 75, 162, 0.3),
                inset 0 1px 0 rgba(255, 255, 255, 0.8);
            display: none;
            flex-direction: column;
            z-index: 1000;
            animation: slideInUp 0.4s ease-out;
            border: 2px solid rgba(102, 126, 234, 0.3);
        }
        .chat-widget.active {
            display: flex;
        }
        
        @keyframes slideInUp {
            from {
                transform: translateY(100px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
        
        @keyframes glowPulse {
            0% {
                box-shadow:
                    0 0 20px rgba(255, 0, 150, 0.6),
                    0 0 40px rgba(0, 150, 255, 0.4),
                    0 0 60px rgba(255, 0, 150, 0.3);
            }
            25% {
                box-shadow:
                    0 0 30px rgba(0, 255, 150, 0.8),
                    0 0 60px rgba(255, 150, 0, 0.6),
                    0 0 90px rgba(0, 255, 150, 0.4);
            }
            50% {
                box-shadow:
                    0 0 30px rgba(150, 0, 255, 0.8),
                    0 0 60px rgba(255, 0, 150, 0.6),
                    0 0 90px rgba(150, 0, 255, 0.4);
            }
            75% {
                box-shadow:
                    0 0 30px rgba(255, 200, 0, 0.8),
                    0 0 60px rgba(0, 150, 255, 0.6),
                    0 0 90px rgba(255, 200, 0, 0.4);
            }
            100% {
                box-shadow:
                    0 0 20px rgba(255, 0, 150, 0.6),
                    0 0 40px rgba(0, 150, 255, 0.4),
                    0 0 60px rgba(255, 0, 150, 0.3);
            }
        }
        
        .chat-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 18px;
            border-radius: 18px 18px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: relative;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        
        .chat-header::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: linear-gradient(
                45deg,
                transparent,
                rgba(255, 255, 255, 0.1),
                transparent
            );
            animation: shimmer 3s infinite;
        }
        
        @keyframes shimmer {
            0% {
                transform: translateX(-100%) translateY(-100%) rotate(45deg);
            }
            100% {
                transform: translateX(100%) translateY(100%) rotate(45deg);
            }
        }
        
        .chat-header h5 {
            margin: 0;
            font-size: 17px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            display: flex;
            align-items: center;
            gap: 8px;
            position: relative;
            z-index: 1;
        }
        
        .chat-header h5 i {
            animation: robotBounce 2s ease-in-out infinite;
        }
        
        @keyframes robotBounce {
            0%, 100% {
                transform: translateY(0) rotate(0deg);
            }
            25% {
                transform: translateY(-3px) rotate(-5deg);
            }
            75% {
                transform: translateY(-3px) rotate(5deg);
            }
        }
        
        .chat-close {
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            font-size: 22px;
            cursor: pointer;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
            position: relative;
            z-index: 1;
        }
        
        .chat-close:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: rotate(90deg) scale(1.1);
        }
        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 20px;
            max-height: 350px;
            background: linear-gradient(to bottom, #ffffff 0%, #f8f9ff 100%);
        }
        
        .chat-messages::-webkit-scrollbar {
            width: 6px;
        }
        
        .chat-messages::-webkit-scrollbar-track {
            background: rgba(102, 126, 234, 0.1);
            border-radius: 10px;
        }
        
        .chat-messages::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 10px;
        }
        
        .chat-message {
            margin-bottom: 12px;
            padding: 12px 16px;
            border-radius: 18px;
            max-width: 80%;
            animation: messageSlideIn 0.3s ease-out;
            position: relative;
        }
        
        @keyframes messageSlideIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .chat-message.user {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            margin-left: auto;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
            border-bottom-right-radius: 4px;
        }
        
        .chat-message.bot {
            background: linear-gradient(145deg, #f0f0f0 0%, #e8e9f3 100%);
            color: #333;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border-bottom-left-radius: 4px;
            border-left: 3px solid #667eea;
        }
        .chat-input-container {
            padding: 18px;
            border-top: 2px solid rgba(102, 126, 234, 0.2);
            background: linear-gradient(to top, #ffffff 0%, #f8f9ff 100%);
            border-radius: 0 0 18px 18px;
        }
        
        .chat-input {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .chat-input input {
            flex: 1;
            padding: 12px 18px;
            border: 2px solid rgba(102, 126, 234, 0.3);
            border-radius: 25px;
            outline: none;
            font-size: 14px;
            transition: all 0.3s ease;
            background: white;
        }
        
        .chat-input input:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .chat-input button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            font-size: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .chat-input button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
        }
        
        .chat-input button:active {
            transform: translateY(0);
        }
        .chat-toggle {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 75px;
            height: 75px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: 3px solid white;
            border-radius: 50%;
            cursor: pointer;
            box-shadow:
                0 8px 25px rgba(102, 126, 234, 0.5),
                0 0 0 0 rgba(102, 126, 234, 0.7);
            z-index: 999;
            transition: all 0.3s ease;
            animation: glowPulse 2s ease-in-out infinite;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0;
            overflow: hidden;
        }
        
        .chat-toggle img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .chat-toggle::before {
            content: '';
            position: absolute;
            top: -5px;
            left: -5px;
            right: -5px;
            bottom: -5px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            z-index: -1;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        
        .chat-toggle:hover {
            transform: scale(1.15) rotate(5deg);
            box-shadow:
                0 12px 35px rgba(255, 0, 150, 0.6),
                0 0 60px rgba(0, 255, 150, 0.5),
                0 0 80px rgba(150, 0, 255, 0.4);
            animation: rainbowGlow 1.5s ease-in-out infinite;
        }
        
        @keyframes rainbowGlow {
            0% {
                filter: hue-rotate(0deg);
            }
            100% {
                filter: hue-rotate(360deg);
            }
        }
        
        .chat-toggle:hover::before {
            opacity: 0.3;
            animation: ripple 1s ease-out infinite;
        }
        
        @keyframes ripple {
            0% {
                transform: scale(1);
                opacity: 0.3;
            }
            100% {
                transform: scale(1.5);
                opacity: 0;
            }
        }
        
        /* Tooltip container */
        .chat-tooltip {
            position: fixed;
            bottom: 105px;
            right: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 14px 22px;
            border-radius: 15px;
            font-size: 14px;
            font-weight: 500;
            white-space: nowrap;
            opacity: 0;
            pointer-events: none;
            transform: translateY(10px) scale(0.95);
            transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
            box-shadow:
                0 10px 30px rgba(102, 126, 234, 0.5),
                0 0 20px rgba(118, 75, 162, 0.3);
            z-index: 998;
            text-align: center;
            max-width: 280px;
        }
        
        .chat-tooltip::after {
            content: '';
            position: absolute;
            bottom: -8px;
            right: 25px;
            width: 0;
            height: 0;
            border-left: 10px solid transparent;
            border-right: 10px solid transparent;
            border-top: 10px solid #764ba2;
        }
        
        .chat-tooltip.show {
            opacity: 1;
            transform: translateY(0) scale(1);
        }
        
        /* Typing indicator animation */
        .typing-indicator {
            display: inline-flex;
            gap: 4px;
            padding: 8px 12px;
        }
        
        .typing-indicator span {
            width: 8px;
            height: 8px;
            background: #667eea;
            border-radius: 50%;
            animation: typingDot 1.4s infinite;
        }
        
        .typing-indicator span:nth-child(2) {
            animation-delay: 0.2s;
        }
        
        .typing-indicator span:nth-child(3) {
            animation-delay: 0.4s;
        }
        
        @keyframes typingDot {
            0%, 60%, 100% {
                transform: translateY(0);
                opacity: 0.7;
            }
            30% {
                transform: translateY(-10px);
                opacity: 1;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Welcome Back</h2>
        <p class="subtitle">Please login to your account</p>

        <!-- Display error messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
            </div>
        </c:if>

        <!-- Display success messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>

        <!-- Login Form with Spring Security CSRF Protection -->
        <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
            
            <!-- Username Field -->
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       placeholder="Enter your username"
                       required 
                       autofocus />
            </div>

            <!-- Password Field -->
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="Enter your password"
                       required />
            </div>

            <!-- Remember Me and Forgot Password -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <div class="remember-me" style="margin-bottom: 0;">
                    <input type="checkbox"
                           id="remember-me"
                           name="remember-me" />
                    <label for="remember-me">Remember me</label>
                </div>
                <a href="${pageContext.request.contextPath}/forgot-password"
                   style="color: #667eea; text-decoration: none; font-size: 14px; font-weight: 500;">
                    Forgot Password?
                </a>
            </div>

            <!-- CSRF Token (required by Spring Security for POST requests) -->
            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
            </c:if>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary">Sign In</button>
            
            <!-- Security Note -->
            <div style="text-align: center; margin-top: 15px; font-size: 12px; color: #999;">
                <i>🔒 Secure login with Spring Security</i>
            </div>
        </form>

        <!-- Register Link -->
        <div class="register-link">
            Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a>
        </div>
    </div>

    <!-- Chat Tooltip -->
    <div class="chat-tooltip" id="chatTooltip">
        💬 Chat with Julia to help you navigate Pahappa Limited
    </div>
    
    <!-- Chat Widget -->
    <button class="chat-toggle" id="chatToggle" onclick="toggleChat()"
            onmouseenter="showTooltip()"
            onmouseleave="hideTooltip()"
            title="">
        <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4"
             alt="Chat with Julia">
    </button>

    <div class="chat-widget" id="chatWidget">
        <div class="chat-header">
            <h5>
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4"
                     alt="Julia"
                     style="width: 32px; height: 32px; border-radius: 50%; border: 2px solid white; margin-right: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.2);">
                Chat with Julia
            </h5>
            <button class="chat-close" onclick="toggleChat()">×</button>
        </div>
        <div class="chat-messages" id="chatMessages">
            <div class="chat-message bot">
                <div style="display: flex; align-items: flex-start; gap: 8px;">
                    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4"
                         alt="Julia"
                         style="width: 28px; height: 28px; border-radius: 50%; border: 2px solid #667eea; flex-shrink: 0;">
                    <div>
                        <div style="font-weight: 600; color: #667eea; font-size: 12px; margin-bottom: 4px;">Julia</div>
                        <div>Hello! I'm Julia, your AI assistant. Ask me anything about Pahappa Limited!</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="chat-input-container">
            <div class="chat-input">
                <input type="text" id="chatInput" placeholder="Type your message..." onkeypress="handleKeyPress(event)">
                <button onclick="sendMessage()"><i class="fas fa-paper-plane"></i></button>
            </div>
        </div>
    </div>

    <script>
        // Global variable to track SDK availability
        let gss = null;
        let sdkAvailable = false;

        // Handle SDK loading error
        function handleSDKError() {
            console.warn('GSS SDK failed to load. Chat functionality will be disabled.');
            sdkAvailable = false;
            // Hide chat toggle button if SDK fails to load
            const chatToggle = document.getElementById('chatToggle');
            if (chatToggle) {
                chatToggle.style.display = 'none';
            }
        }

        // Initialize GSS Client when SDK loads successfully
        window.addEventListener('load', function() {
            try {
                if (typeof GSSClient !== 'undefined') {
                    gss = new GSSClient({
                        apiKey: 'gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ',
                        cfWorkerUrl: 'https://node.gss-tec.com',
                        hfEngineUrl: 'https://Gaston895-AI.hf.space',
                        model: 'llama-3.1-8b-instant'
                    });
                    sdkAvailable = true;
                } else {
                    handleSDKError();
                }
            } catch (error) {
                console.error('Error initializing GSS Client:', error);
                handleSDKError();
            }
        });

        // Tooltip functions
        function showTooltip() {
            const tooltip = document.getElementById('chatTooltip');
            if (tooltip) {
                tooltip.classList.add('show');
            }
        }
        
        function hideTooltip() {
            const tooltip = document.getElementById('chatTooltip');
            if (tooltip) {
                tooltip.classList.remove('show');
            }
        }
        
        // Chat functionality
        function toggleChat() {
            if (!sdkAvailable) {
                alert('Chat functionality is currently unavailable. Please try again later.');
                return;
            }
            const chatWidget = document.getElementById('chatWidget');
            const chatToggle = document.getElementById('chatToggle');
            const tooltip = document.getElementById('chatTooltip');
            
            chatWidget.classList.toggle('active');
            chatToggle.style.display = chatWidget.classList.contains('active') ? 'none' : 'block';
            
            // Hide tooltip when chat opens
            if (chatWidget.classList.contains('active') && tooltip) {
                tooltip.classList.remove('show');
            }
        }

        function handleKeyPress(event) {
            if (event.key === 'Enter') {
                sendMessage();
            }
        }

        async function sendMessage() {
            if (!sdkAvailable || !gss) {
                alert('Chat functionality is currently unavailable. Please try again later.');
                return;
            }

            const input = document.getElementById('chatInput');
            const message = input.value.trim();
            
            if (!message) return;

            // Add user message to chat
            addMessageToChat(message, 'user');
            input.value = '';

            // Show typing indicator with animated dots and Julia's avatar
            const typingDiv = document.createElement('div');
            typingDiv.className = 'chat-message bot';
            typingDiv.innerHTML = `
                <div style="display: flex; align-items: flex-start; gap: 8px;">
                    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4"
                         alt="Julia"
                         style="width: 28px; height: 28px; border-radius: 50%; border: 2px solid #667eea; flex-shrink: 0;">
                    <div style="display: flex; flex-direction: column; gap: 4px;">
                        <div style="font-weight: 600; color: #667eea; font-size: 12px;">Julia</div>
                        <div style="display: flex; align-items: center; gap: 8px;">
                            <span style="font-weight: 500;">typing</span>
                            <div class="typing-indicator">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            typingDiv.id = 'typing-indicator';
            document.getElementById('chatMessages').appendChild(typingDiv);
            scrollToBottom();

            try {
                // Send message to GSS API
                const reply = await gss.chat([
                    { role: 'system', content: 'You are Julia, a helpful AI assistant for Pahappa Limited. Answer questions about the company, its services, and help users with their queries.' },
                    { role: 'user', content: message }
                ]);

                // Remove typing indicator
                const typingIndicator = document.getElementById('typing-indicator');
                if (typingIndicator) {
                    typingIndicator.remove();
                }

                // Add bot response to chat
                addMessageToChat(reply, 'bot');
            } catch (error) {
                console.error('Error sending message:', error);
                
                // Remove typing indicator
                const typingIndicator = document.getElementById('typing-indicator');
                if (typingIndicator) {
                    typingIndicator.remove();
                }
                
                addMessageToChat('Sorry, I encountered an error. Please try again.', 'bot');
            }
        }

        function addMessageToChat(message, sender) {
            const messagesDiv = document.getElementById('chatMessages');
            const messageDiv = document.createElement('div');
            messageDiv.className = `chat-message ${sender}`;
            
            if (sender === 'bot') {
                // Add Julia's avatar to bot messages
                messageDiv.innerHTML = `
                    <div style="display: flex; align-items: flex-start; gap: 8px;">
                        <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4"
                             alt="Julia"
                             style="width: 28px; height: 28px; border-radius: 50%; border: 2px solid #667eea; flex-shrink: 0;">
                        <div>
                            <div style="font-weight: 600; color: #667eea; font-size: 12px; margin-bottom: 4px;">Julia</div>
                            <div>${message}</div>
                        </div>
                    </div>
                `;
            } else {
                // User messages remain simple
                messageDiv.textContent = message;
            }
            
            messagesDiv.appendChild(messageDiv);
            scrollToBottom();
        }

        function scrollToBottom() {
            const messagesDiv = document.getElementById('chatMessages');
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }
    </script>
</body>
</html>