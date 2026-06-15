<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My QR Code - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .qr-container {
            padding: 30px 15px;
        }
        .qr-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            max-width: 500px;
            margin: 0 auto;
            text-align: center;
        }
        .qr-title {
            font-size: 28px;
            font-weight: bold;
            color: #2d3748;
            margin-bottom: 10px;
        }
        .qr-subtitle {
            color: #718096;
            margin-bottom: 30px;
        }
        .qr-code-wrapper {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 30px;
            border-radius: 15px;
            margin: 30px 0;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        .qr-code-image {
            background: white;
            padding: 20px;
            border-radius: 10px;
            display: inline-block;
        }
        .qr-code-image img {
            max-width: 100%;
            height: auto;
            display: block;
        }
        .user-info {
            background: #f7fafc;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
        }
        .user-info-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e2e8f0;
        }
        .user-info-item:last-child {
            border-bottom: none;
        }
        .user-info-label {
            color: #718096;
            font-weight: 600;
        }
        .user-info-value {
            color: #2d3748;
            font-weight: 500;
        }
        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        .action-buttons button,
        .action-buttons a {
            flex: 1;
            padding: 15px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-download {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        .btn-print {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .action-buttons button:hover,
        .action-buttons a:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        .instructions {
            background: #fff5e6;
            border-left: 4px solid #ff9800;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            text-align: left;
        }
        .instructions h6 {
            color: #e65100;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .instructions ul {
            margin: 0;
            padding-left: 20px;
        }
        .instructions li {
            color: #5d4037;
            margin-bottom: 5px;
        }
        .navbar-custom {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        @media print {
            body {
                background: white;
            }
            .navbar-custom,
            .action-buttons,
            .instructions {
                display: none !important;
            }
            .qr-card {
                box-shadow: none;
                border: 2px solid #000;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="/home">
                <i class="fas fa-qrcode me-2"></i>Pahappa QR Code
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
                        <a class="nav-link" href="/attendance/qr-scanner"><i class="fas fa-qrcode me-1"></i>QR Scanner</a>
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

    <div class="container qr-container">
        <div class="qr-card">
            <h2 class="qr-title">
                <i class="fas fa-qrcode me-2"></i>My QR Code
            </h2>
            <p class="qr-subtitle">Use this QR code for quick attendance check-in/out</p>

            <!-- User Information -->
            <div class="user-info">
                <div class="user-info-item">
                    <span class="user-info-label"><i class="fas fa-user me-2"></i>Name:</span>
                    <span class="user-info-value">${user.firstName} ${user.lastName}</span>
                </div>
                <div class="user-info-item">
                    <span class="user-info-label"><i class="fas fa-id-badge me-2"></i>Username:</span>
                    <span class="user-info-value">${user.username}</span>
                </div>
                <div class="user-info-item">
                    <span class="user-info-label"><i class="fas fa-envelope me-2"></i>Email:</span>
                    <span class="user-info-value">${user.email}</span>
                </div>
            </div>

            <!-- QR Code -->
            <div class="qr-code-wrapper">
                <div class="qr-code-image">
                    <img src="/attendance/qr-code/generate" alt="My QR Code" id="qrCodeImage">
                </div>
                <p class="text-white mt-3 mb-0">
                    <i class="fas fa-shield-alt me-2"></i>Secure & Encrypted
                </p>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <button class="btn-download" onclick="downloadQRCode()">
                    <i class="fas fa-download me-2"></i>Download
                </button>
                <button class="btn-print" onclick="window.print()">
                    <i class="fas fa-print me-2"></i>Print
                </button>
            </div>

            <!-- Instructions -->
            <div class="instructions">
                <h6><i class="fas fa-info-circle me-2"></i>How to Use Your QR Code:</h6>
                <ul>
                    <li>Save or print this QR code for easy access</li>
                    <li>Go to the QR Scanner page on any device</li>
                    <li>Scan your QR code using the camera</li>
                    <li>Click Sign In or Sign Out button</li>
                    <li>Your attendance will be recorded instantly</li>
                </ul>
            </div>

            <!-- Quick Links -->
            <div class="text-center mt-4">
                <p class="text-muted mb-3">Quick Actions</p>
                <div class="d-flex gap-2 justify-content-center flex-wrap">
                    <a href="/attendance/qr-scanner" class="btn btn-outline-primary btn-sm">
                        <i class="fas fa-camera me-1"></i>Scan QR Code
                    </a>
                    <a href="/attendance" class="btn btn-outline-secondary btn-sm">
                        <i class="fas fa-clock me-1"></i>Manual Check-in
                    </a>
                    <a href="/attendance/dashboard" class="btn btn-outline-info btn-sm">
                        <i class="fas fa-chart-bar me-1"></i>View Dashboard
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function downloadQRCode() {
            const img = document.getElementById('qrCodeImage');
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            
            // Set canvas size to match image
            canvas.width = img.naturalWidth;
            canvas.height = img.naturalHeight;
            
            // Draw image on canvas
            ctx.drawImage(img, 0, 0);
            
            // Convert to blob and download
            canvas.toBlob(function(blob) {
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'my-qr-code-${user.username}.png';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            });
        }

        // Add error handling for QR code image
        document.getElementById('qrCodeImage').onerror = function() {
            this.src = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="300" height="300"><rect width="300" height="300" fill="%23f0f0f0"/><text x="50%" y="50%" text-anchor="middle" dy=".3em" fill="%23999" font-family="Arial" font-size="16">QR Code Loading...</text></svg>';
        };
    </script>
    <!-- Include Julia Chat Widget -->
    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>
