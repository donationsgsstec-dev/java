<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QR Scanner - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://unpkg.com/html5-qrcode@2.3.8/html5-qrcode.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .scanner-container {
            padding: 30px 15px;
        }
        .scanner-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            max-width: 600px;
            margin: 0 auto;
        }
        .scanner-title {
            font-size: 28px;
            font-weight: bold;
            color: #2d3748;
            text-align: center;
            margin-bottom: 30px;
        }
        #reader {
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 20px;
        }
        .action-buttons button {
            flex: 1;
            padding: 15px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .btn-sign-in {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        .btn-sign-out {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
        }
        .action-buttons button:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        .action-buttons button:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            transform: none;
        }
        .status-message {
            margin-top: 20px;
            padding: 15px;
            border-radius: 10px;
            text-align: center;
            font-weight: 600;
            display: none;
        }
        .status-message.success {
            background: #d4edda;
            color: #155724;
            border: 2px solid #c3e6cb;
        }
        .status-message.error {
            background: #f8d7da;
            color: #721c24;
            border: 2px solid #f5c6cb;
        }
        .status-message.info {
            background: #d1ecf1;
            color: #0c5460;
            border: 2px solid #bee5eb;
        }
        .scanner-instructions {
            background: #f7fafc;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
        .scanner-instructions h6 {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 15px;
        }
        .scanner-instructions ul {
            margin: 0;
            padding-left: 20px;
        }
        .scanner-instructions li {
            color: #4a5568;
            margin-bottom: 8px;
        }
        .navbar-custom {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .loading-spinner {
            text-align: center;
            padding: 40px;
            display: none;
        }
        .loading-spinner .spinner-border {
            width: 3rem;
            height: 3rem;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="/home">
                <i class="fas fa-qrcode me-2"></i>Pahappa QR Scanner
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance"><i class="fas fa-clock me-1"></i>Attendance</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/dashboard"><i class="fas fa-chart-bar me-1"></i>Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/calendar"><i class="fas fa-calendar me-1"></i>Calendar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/attendance/qr-scanner"><i class="fas fa-qrcode me-1"></i>QR Scanner</a>
                    </li>
                    <li class="nav-item">
                        <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline; margin: 0;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="nav-link btn btn-link" style="border: none; background: none; cursor: pointer;">
                                <i class="fas fa-sign-out-alt me-1"></i>Logout
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container scanner-container">
        <div class="scanner-card">
            <h2 class="scanner-title">
                <i class="fas fa-qrcode me-2"></i>QR Code Scanner
            </h2>

            <!-- QR Code Reader -->
            <div id="reader"></div>

            <!-- Loading Spinner -->
            <div class="loading-spinner" id="loadingSpinner">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-3">Processing...</p>
            </div>

            <!-- Action Buttons — Room QR only -->
            <div class="action-buttons">
                <button class="btn-sign-in" id="roomScanBtn"
                        onclick="processRoomQR()" disabled
                        style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);flex:1;">
                    <i class="fas fa-qrcode me-2"></i>Submit Scan
                </button>
            </div>

            <!-- Status Message -->
            <div class="status-message" id="statusMessage"></div>

            <!-- Instructions -->
            <div class="scanner-instructions">
                <h6><i class="fas fa-info-circle me-2"></i>How to Use:</h6>
                <ul>
                    <li>Allow camera access when prompted</li>
                    <li>Point your camera at the <strong>Check-In</strong> or <strong>Check-Out</strong> QR on the office screen</li>
                    <li>Once detected, tap <strong>Submit Scan</strong> — the server knows which code it is</li>
                    <li>Check-Out is rejected if you haven't checked in today</li>
                </ul>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let html5QrcodeScanner;
        let lastScannedCode = null;
        let isScanning = true;

        function onScanSuccess(decodedText, decodedResult) {
            if (!isScanning) return;
            isScanning = false;
            lastScannedCode = decodedText;

            if (html5QrcodeScanner) {
                html5QrcodeScanner.clear().catch(err => console.log('Clear error:', err));
            }

            document.getElementById('roomScanBtn').disabled = false;
            showMessage('QR Code detected! Tap Submit to record your attendance.', 'info');
        }

        function onScanFailure(error) {
            // Handle scan failure silently - this is normal during scanning
            // Only log actual errors, not "No MultiFormat Readers" which is normal
            if (error && !error.includes('No MultiFormat Readers')) {
                console.log('Scan error:', error);
            }
        }

        function initScanner() {
            html5QrcodeScanner = new Html5QrcodeScanner(
                "reader",
                { 
                    fps: 10,
                    qrbox: { width: 250, height: 250 },
                    aspectRatio: 1.0
                },
                false
            );
            
            html5QrcodeScanner.render(onScanSuccess, onScanFailure);
        }

        function processRoomQR(action) {
            if (!lastScannedCode) {
                showMessage('Please scan the office QR code first!', 'error');
                return;
            }

            document.getElementById('loadingSpinner').style.display = 'block';
            document.getElementById('roomScanBtn').disabled = true;

            fetch('/attendance/qr/room-scan', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ token: lastScannedCode })
            })
            .then(r => r.json())
            .then(data => {
                document.getElementById('loadingSpinner').style.display = 'none';
                if (data.success) {
                    const icon = data.action === 'CHECK_IN' ? '✅' : '👋';
                    showMessage(icon + ' ' + data.message + ' — Redirecting…', 'success');
                    setTimeout(() => window.location.href = '${pageContext.request.contextPath}/attendance/dashboard', 2000);
                } else {
                    showMessage(data.message, 'error');
                    restartScanner();
                }
            })
            .catch(() => {
                document.getElementById('loadingSpinner').style.display = 'none';
                showMessage('Error. Please try again.', 'error');
                restartScanner();
            });
        }

        function restartScanner() {
            setTimeout(() => {
                lastScannedCode = null;
                isScanning = true;
                document.getElementById('roomScanBtn').disabled = true;
                initScanner();
            }, 3000);
        }

        function showMessage(message, type) {
            const messageElement = document.getElementById('statusMessage');
            messageElement.textContent = message;
            messageElement.className = 'status-message ' + type;
            messageElement.style.display = 'block';
            
            // Auto-hide after 5 seconds
            setTimeout(() => {
                messageElement.style.display = 'none';
            }, 5000);
        }

        // Initialize scanner on page load
        window.addEventListener('load', () => {
            initScanner();
        });

        // Cleanup on page unload
        window.addEventListener('beforeunload', () => {
            if (html5QrcodeScanner) {
                html5QrcodeScanner.clear();
            }
        });
    </script>
</body>
</html>