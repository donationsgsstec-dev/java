<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Julia AI Diagnostics - Pahappa</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 40px 20px;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
        }
        .card {
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
            margin-bottom: 30px;
        }
        h1 {
            color: #2d3748;
            margin-bottom: 10px;
            font-size: 32px;
            display: flex;
            align-items: center;
            gap: 15px;
        }
        h2 {
            color: #667eea;
            margin: 30px 0 15px;
            font-size: 20px;
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 10px;
        }
        .status {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 14px;
            margin: 10px 0;
        }
        .status.success { background: #d4edda; color: #155724; }
        .status.error { background: #f8d7da; color: #721c24; }
        .status.warning { background: #fff3cd; color: #856404; }
        .status.info { background: #d1ecf1; color: #0c5460; }
        .test-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 15px 0;
        }
        .test-result {
            margin: 10px 0;
            padding: 15px;
            border-left: 4px solid #667eea;
            background: white;
            border-radius: 5px;
        }
        .test-result.success { border-left-color: #28a745; }
        .test-result.error { border-left-color: #dc3545; }
        button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            transition: all 0.3s ease;
            margin: 10px 5px;
        }
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
        }
        button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
        .code-block {
            background: #2d3748;
            color: #e2e8f0;
            padding: 15px;
            border-radius: 8px;
            overflow-x: auto;
            font-family: 'Courier New', monospace;
            font-size: 13px;
            margin: 10px 0;
        }
        .spinner {
            display: inline-block;
            width: 16px;
            height: 16px;
            border: 3px solid rgba(102, 126, 234, 0.3);
            border-top-color: #667eea;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
        }
        @keyframes spin {
            to { transform: rotate(360deg); }
        }
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin: 20px 0;
        }
        .info-item {
            background: white;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        .info-label {
            font-weight: 600;
            color: #667eea;
            font-size: 12px;
            text-transform: uppercase;
            margin-bottom: 5px;
        }
        .info-value {
            color: #2d3748;
            font-size: 14px;
            word-break: break-all;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <h1>
                🔍 Julia AI Diagnostics
            </h1>
            <p style="color: #718096; margin-top: 10px;">
                This page helps diagnose connection issues with the Julia AI chat service.
            </p>

            <h2>📋 Configuration Status</h2>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">API Endpoint</div>
                    <div class="info-value" id="apiEndpoint">Loading...</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Model</div>
                    <div class="info-value" id="modelName">Loading...</div>
                </div>
                <div class="info-item">
                    <div class="info-label">API Key Status</div>
                    <div class="info-value" id="apiKeyStatus">Loading...</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Service Status</div>
                    <div class="info-value" id="serviceStatus">Loading...</div>
                </div>
            </div>

            <h2>🧪 Connection Tests</h2>
            <div class="test-section">
                <button onclick="runAllTests()" id="runTestsBtn">
                    Run All Tests
                </button>
                <button onclick="testBasicConnection()" id="basicTestBtn">
                    Test Basic Connection
                </button>
                <button onclick="testChatEndpoint()" id="chatTestBtn">
                    Test Chat Endpoint
                </button>
                
                <div id="testResults" style="margin-top: 20px;"></div>
            </div>

            <h2>💬 Live Chat Test</h2>
            <div class="test-section">
                <input type="text" id="testMessage" 
                       placeholder="Type a test message..." 
                       style="width: 100%; padding: 12px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 14px; margin-bottom: 10px;">
                <button onclick="sendTestMessage()" id="sendTestBtn">
                    Send Test Message
                </button>
                <div id="chatTestResult" style="margin-top: 15px;"></div>
            </div>

            <h2>📖 Troubleshooting Guide</h2>
            <div class="test-section">
                <h3 style="color: #2d3748; margin-bottom: 10px;">Common Issues:</h3>
                <ul style="color: #4a5568; line-height: 1.8; padding-left: 20px;">
                    <li><strong>Connection Timeout:</strong> Check if GSS_AI_CF_WORKER_URL is accessible from your server</li>
                    <li><strong>401 Unauthorized:</strong> Verify GSS_AI_API_KEY is correct and active</li>
                    <li><strong>CORS Errors:</strong> Use server-side proxy (already implemented) instead of client-side SDK</li>
                    <li><strong>No Response:</strong> Check if the model name is correct and supported</li>
                    <li><strong>Slow Response:</strong> Free-tier services may have rate limits or cold starts</li>
                </ul>
            </div>
        </div>
    </div>

    <script>
        // Load configuration on page load
        window.addEventListener('DOMContentLoaded', loadConfiguration);

        async function loadConfiguration() {
            try {
                const response = await fetch('/julia/config');
                const config = await response.json();
                
                document.getElementById('apiEndpoint').textContent = config.endpoint || 'Not configured';
                document.getElementById('modelName').textContent = config.model || 'Not configured';
                document.getElementById('apiKeyStatus').innerHTML = config.hasApiKey 
                    ? '<span class="status success">✓ Configured</span>' 
                    : '<span class="status error">✗ Missing</span>';
                document.getElementById('serviceStatus').innerHTML = config.enabled 
                    ? '<span class="status success">✓ Enabled</span>' 
                    : '<span class="status warning">⚠ Disabled</span>';
            } catch (error) {
                console.error('Failed to load configuration:', error);
                document.getElementById('serviceStatus').innerHTML = '<span class="status error">✗ Error loading config</span>';
            }
        }

        async function runAllTests() {
            clearResults();
            await testBasicConnection();
            await new Promise(resolve => setTimeout(resolve, 1000));
            await testChatEndpoint();
        }

        async function testBasicConnection() {
            addTestResult('Testing basic connectivity...', 'info', true);
            disableButtons(true);

            try {
                const startTime = Date.now();
                const response = await fetch('/julia/health', {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                });
                const duration = Date.now() - startTime;
                
                if (response.ok) {
                    const data = await response.json();
                    addTestResult(
                        `✓ Basic connection successful (${duration}ms)<br>` +
                        `Status: ${data.status || 'OK'}<br>` +
                        `Message: ${data.message || 'Service is running'}`,
                        'success'
                    );
                } else {
                    addTestResult(
                        `✗ Connection failed with status ${response.status}<br>` +
                        `This may indicate a server configuration issue.`,
                        'error'
                    );
                }
            } catch (error) {
                addTestResult(
                    `✗ Connection error: ${error.message}<br>` +
                    `The server may be down or unreachable.`,
                    'error'
                );
            } finally {
                disableButtons(false);
            }
        }

        async function testChatEndpoint() {
            addTestResult('Testing chat endpoint...', 'info', true);
            disableButtons(true);

            try {
                const startTime = Date.now();
                const response = await fetch('/julia/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        messages: [
                            { role: 'system', content: 'You are a helpful assistant.' },
                            { role: 'user', content: 'Say "test successful" if you can read this.' }
                        ]
                    })
                });
                const duration = Date.now() - startTime;
                const data = await response.json();

                if (response.ok && data.reply) {
                    addTestResult(
                        `✓ Chat endpoint working (${duration}ms)<br>` +
                        `Response: ${data.reply.substring(0, 100)}${data.reply.length > 100 ? '...' : ''}`,
                        'success'
                    );
                } else {
                    addTestResult(
                        `✗ Chat endpoint failed<br>` +
                        `Error: ${data.error || 'Unknown error'}<br>` +
                        `Status: ${response.status}`,
                        'error'
                    );
                }
            } catch (error) {
                addTestResult(
                    `✗ Chat test error: ${error.message}<br>` +
                    `Check server logs for details.`,
                    'error'
                );
            } finally {
                disableButtons(false);
            }
        }

        async function sendTestMessage() {
            const input = document.getElementById('testMessage');
            const message = input.value.trim();
            
            if (!message) {
                alert('Please enter a test message');
                return;
            }

            const resultDiv = document.getElementById('chatTestResult');
            resultDiv.innerHTML = '<div class="test-result info"><span class="spinner"></span> Sending message...</div>';
            disableButtons(true);

            try {
                const startTime = Date.now();
                const response = await fetch('/julia/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        messages: [
                            { role: 'system', content: 'You are Julia, a helpful AI assistant.' },
                            { role: 'user', content: message }
                        ]
                    })
                });
                const duration = Date.now() - startTime;
                const data = await response.json();

                if (response.ok && data.reply) {
                    resultDiv.innerHTML = `
                        <div class="test-result success">
                            <strong>✓ Success (${duration}ms)</strong><br>
                            <strong>Your message:</strong> ${message}<br>
                            <strong>Julia's response:</strong> ${data.reply}
                        </div>
                    `;
                } else {
                    resultDiv.innerHTML = `
                        <div class="test-result error">
                            <strong>✗ Failed</strong><br>
                            <strong>Error:</strong> ${data.error || 'Unknown error'}<br>
                            <strong>Status:</strong> ${response.status}
                        </div>
                    `;
                }
            } catch (error) {
                resultDiv.innerHTML = `
                    <div class="test-result error">
                        <strong>✗ Connection Error</strong><br>
                        ${error.message}
                    </div>
                `;
            } finally {
                disableButtons(false);
            }
        }

        function addTestResult(message, type, isLoading = false) {
            const resultsDiv = document.getElementById('testResults');
            const resultDiv = document.createElement('div');
            resultDiv.className = `test-result ${type}`;
            resultDiv.innerHTML = isLoading 
                ? `<span class="spinner"></span> ${message}`
                : message;
            resultsDiv.appendChild(resultDiv);
        }

        function clearResults() {
            document.getElementById('testResults').innerHTML = '';
        }

        function disableButtons(disabled) {
            document.getElementById('runTestsBtn').disabled = disabled;
            document.getElementById('basicTestBtn').disabled = disabled;
            document.getElementById('chatTestBtn').disabled = disabled;
            document.getElementById('sendTestBtn').disabled = disabled;
        }
    </script>
</body>
</html>