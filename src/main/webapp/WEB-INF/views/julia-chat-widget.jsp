<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Julia Chat Widget - Reusable Component -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<script src="https://Gaston895-AI.hf.space/sdk/gss-sdk.js" onerror="handleJuliaSDKError()"></script>

<style>
    /* Julia Chat Widget Styles */
    .julia-chat-widget {
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
        animation: juliaSlideInUp 0.4s ease-out;
        border: 2px solid rgba(102, 126, 234, 0.3);
    }
    .julia-chat-widget.active {
        display: flex;
    }
    
    @keyframes juliaSlideInUp {
        from {
            transform: translateY(100px);
            opacity: 0;
        }
        to {
            transform: translateY(0);
            opacity: 1;
        }
    }
    
    @keyframes juliaGlowPulse {
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
    
    .julia-chat-header {
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
    
    .julia-chat-header::before {
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
        animation: juliaShimmer 3s infinite;
    }
    
    @keyframes juliaShimmer {
        0% {
            transform: translateX(-100%) translateY(-100%) rotate(45deg);
        }
        100% {
            transform: translateX(100%) translateY(100%) rotate(45deg);
        }
    }
    
    .julia-chat-header h5 {
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
    
    .julia-chat-header h5 img {
        animation: juliaRobotBounce 2s ease-in-out infinite;
    }
    
    @keyframes juliaRobotBounce {
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
    
    .julia-chat-close {
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
    
    .julia-chat-close:hover {
        background: rgba(255, 255, 255, 0.3);
        transform: rotate(90deg) scale(1.1);
    }
    
    .julia-chat-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        max-height: 350px;
        background: linear-gradient(to bottom, #ffffff 0%, #f8f9ff 100%);
    }
    
    .julia-chat-messages::-webkit-scrollbar {
        width: 6px;
    }
    
    .julia-chat-messages::-webkit-scrollbar-track {
        background: rgba(102, 126, 234, 0.1);
        border-radius: 10px;
    }
    
    .julia-chat-messages::-webkit-scrollbar-thumb {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 10px;
    }
    
    .julia-chat-message {
        margin-bottom: 12px;
        padding: 12px 16px;
        border-radius: 18px;
        max-width: 80%;
        animation: juliaMessageSlideIn 0.3s ease-out;
        position: relative;
    }
    
    @keyframes juliaMessageSlideIn {
        from {
            opacity: 0;
            transform: translateY(10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .julia-chat-message.user {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        margin-left: auto;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        border-bottom-right-radius: 4px;
    }
    
    .julia-chat-message.bot {
        background: linear-gradient(145deg, #f0f0f0 0%, #e8e9f3 100%);
        color: #333;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        border-bottom-left-radius: 4px;
        border-left: 3px solid #667eea;
    }
    
    .julia-chat-input-container {
        padding: 18px;
        border-top: 2px solid rgba(102, 126, 234, 0.2);
        background: linear-gradient(to top, #ffffff 0%, #f8f9ff 100%);
        border-radius: 0 0 18px 18px;
    }
    
    .julia-chat-input {
        display: flex;
        gap: 10px;
        align-items: center;
    }
    
    .julia-chat-input input {
        flex: 1;
        padding: 12px 18px;
        border: 2px solid rgba(102, 126, 234, 0.3);
        border-radius: 25px;
        outline: none;
        font-size: 14px;
        transition: all 0.3s ease;
        background: white;
    }
    
    .julia-chat-input input:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }
    
    .julia-chat-input button {
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
    
    .julia-chat-input button:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
    }
    
    .julia-chat-input button:active {
        transform: translateY(0);
    }
    
    .julia-chat-toggle {
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
        animation: juliaGlowPulse 2s ease-in-out infinite;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0;
        overflow: hidden;
    }
    
    .julia-chat-toggle img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    
    .julia-chat-toggle:hover {
        transform: scale(1.15) rotate(5deg);
        box-shadow: 
            0 12px 35px rgba(255, 0, 150, 0.6),
            0 0 60px rgba(0, 255, 150, 0.5),
            0 0 80px rgba(150, 0, 255, 0.4);
        animation: juliaRainbowGlow 1.5s ease-in-out infinite;
    }
    
    @keyframes juliaRainbowGlow {
        0% {
            filter: hue-rotate(0deg);
        }
        100% {
            filter: hue-rotate(360deg);
        }
    }
    
    .julia-chat-tooltip {
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
    
    .julia-chat-tooltip::after {
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
    
    .julia-chat-tooltip.show {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
    
    .julia-typing-indicator {
        display: inline-flex;
        gap: 4px;
        padding: 8px 12px;
    }
    
    .julia-typing-indicator span {
        width: 8px;
        height: 8px;
        background: #667eea;
        border-radius: 50%;
        animation: juliaTypingDot 1.4s infinite;
    }
    
    .julia-typing-indicator span:nth-child(2) {
        animation-delay: 0.2s;
    }
    
    .julia-typing-indicator span:nth-child(3) {
        animation-delay: 0.4s;
    }
    
    @keyframes juliaTypingDot {
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

<!-- Julia Chat Tooltip -->
<div class="julia-chat-tooltip" id="juliaChatTooltip">
    💬 Chat with Julia to help you navigate Pahappa Limited
</div>

<!-- Julia Chat Toggle Button -->
<button class="julia-chat-toggle" id="juliaChatToggle" onclick="toggleJuliaChat()" 
        onmouseenter="showJuliaTooltip()" 
        onmouseleave="hideJuliaTooltip()"
        title="">
    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4" 
         alt="Chat with Julia">
</button>

<!-- Julia Chat Widget -->
<div class="julia-chat-widget" id="juliaChatWidget">
    <div class="julia-chat-header">
        <h5>
            <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4" 
                 alt="Julia" 
                 style="width: 32px; height: 32px; border-radius: 50%; border: 2px solid white; margin-right: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.2);">
            Chat with Julia
        </h5>
        <button class="julia-chat-close" onclick="toggleJuliaChat()">×</button>
    </div>
    <div class="julia-chat-messages" id="juliaChatMessages">
        <div class="julia-chat-message bot">
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
    <div class="julia-chat-input-container">
        <div class="julia-chat-input">
            <input type="text" id="juliaChatInput" placeholder="Type your message..." onkeypress="handleJuliaKeyPress(event)">
            <button onclick="sendJuliaMessage()"><i class="fas fa-paper-plane"></i></button>
        </div>
    </div>
</div>

<script>
    // Global variables for Julia chat
    let juliaGss = null;
    let juliaSdkAvailable = false;
    let juliaInitAttempts = 0;
    const MAX_INIT_ATTEMPTS = 10;

    // Handle SDK loading error
    function handleJuliaSDKError() {
        console.warn('GSS SDK failed to load. Julia chat functionality will be disabled.');
        juliaSdkAvailable = false;
        const chatToggle = document.getElementById('juliaChatToggle');
        if (chatToggle) {
            chatToggle.style.display = 'none';
        }
    }

    // Initialize GSS Client with retry mechanism
    function initializeJuliaGss() {
        try {
            if (typeof GSSClient !== 'undefined') {
                console.log('Initializing Julia GSS Client...');
                juliaGss = new GSSClient({
                    apiKey: 'gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ',
                    cfWorkerUrl: 'https://node.gss-tec.com',
                    hfEngineUrl: 'https://Gaston895-AI.hf.space',
                    model: 'llama-3.1-8b-instant'
                });
                juliaSdkAvailable = true;
                console.log('Julia GSS Client initialized successfully!');
                return true;
            } else {
                juliaInitAttempts++;
                if (juliaInitAttempts < MAX_INIT_ATTEMPTS) {
                    console.log(`GSSClient not ready yet, retrying... (${juliaInitAttempts}/${MAX_INIT_ATTEMPTS})`);
                    setTimeout(initializeJuliaGss, 500);
                } else {
                    console.error('GSSClient failed to load after maximum attempts');
                    handleJuliaSDKError();
                }
                return false;
            }
        } catch (error) {
            console.error('Error initializing Julia GSS Client:', error);
            juliaInitAttempts++;
            if (juliaInitAttempts < MAX_INIT_ATTEMPTS) {
                setTimeout(initializeJuliaGss, 500);
            } else {
                handleJuliaSDKError();
            }
            return false;
        }
    }

    // Start initialization when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initializeJuliaGss);
    } else {
        initializeJuliaGss();
    }

    // Tooltip functions
    function showJuliaTooltip() {
        const tooltip = document.getElementById('juliaChatTooltip');
        if (tooltip) {
            tooltip.classList.add('show');
        }
    }
    
    function hideJuliaTooltip() {
        const tooltip = document.getElementById('juliaChatTooltip');
        if (tooltip) {
            tooltip.classList.remove('show');
        }
    }
    
    // Chat functionality
    function toggleJuliaChat() {
        if (!juliaSdkAvailable) {
            alert('Chat functionality is currently unavailable. Please try again later.');
            return;
        }
        const chatWidget = document.getElementById('juliaChatWidget');
        const chatToggle = document.getElementById('juliaChatToggle');
        const tooltip = document.getElementById('juliaChatTooltip');
        
        chatWidget.classList.toggle('active');
        chatToggle.style.display = chatWidget.classList.contains('active') ? 'none' : 'block';
        
        if (chatWidget.classList.contains('active') && tooltip) {
            tooltip.classList.remove('show');
        }
    }

    function handleJuliaKeyPress(event) {
        if (event.key === 'Enter') {
            sendJuliaMessage();
        }
    }

    async function sendJuliaMessage() {
        if (!juliaSdkAvailable || !juliaGss) {
            alert('Chat functionality is currently unavailable. Please try again later.');
            return;
        }

        const input = document.getElementById('juliaChatInput');
        const message = input.value.trim();
        
        if (!message) return;

        addJuliaMessageToChat(message, 'user');
        input.value = '';

        const typingDiv = document.createElement('div');
        typingDiv.className = 'julia-chat-message bot';
        typingDiv.innerHTML = `
            <div style="display: flex; align-items: flex-start; gap: 8px;">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Julia&backgroundColor=b6e3f4&hair=long01&hairColor=4a5568&eyes=happy&mouth=smile&skinColor=ffdbb4" 
                     alt="Julia" 
                     style="width: 28px; height: 28px; border-radius: 50%; border: 2px solid #667eea; flex-shrink: 0;">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                    <div style="font-weight: 600; color: #667eea; font-size: 12px;">Julia</div>
                    <div style="display: flex; align-items: center; gap: 8px;">
                        <span style="font-weight: 500;">typing</span>
                        <div class="julia-typing-indicator">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>
                </div>
            </div>
        `;
        typingDiv.id = 'julia-typing-indicator';
        document.getElementById('juliaChatMessages').appendChild(typingDiv);
        scrollJuliaToBottom();

        try {
            const reply = await juliaGss.chat([
                { role: 'system', content: 'You are Julia, a helpful AI assistant for Pahappa Limited. Answer questions about the company, its services, and help users with their queries.' },
                { role: 'user', content: message }
            ]);

            const typingIndicator = document.getElementById('julia-typing-indicator');
            if (typingIndicator) {
                typingIndicator.remove();
            }

            addJuliaMessageToChat(reply, 'bot');
        } catch (error) {
            console.error('Error sending message:', error);
            
            const typingIndicator = document.getElementById('julia-typing-indicator');
            if (typingIndicator) {
                typingIndicator.remove();
            }
            
            addJuliaMessageToChat('Sorry, I encountered an error. Please try again.', 'bot');
        }
    }

    function addJuliaMessageToChat(message, sender) {
        const messagesDiv = document.getElementById('juliaChatMessages');
        const messageDiv = document.createElement('div');
        messageDiv.className = `julia-chat-message ${sender}`;
        
        if (sender === 'bot') {
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
            messageDiv.textContent = message;
        }
        
        messagesDiv.appendChild(messageDiv);
        scrollJuliaToBottom();
    }

    function scrollJuliaToBottom() {
        const messagesDiv = document.getElementById('juliaChatMessages');
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }
</script>