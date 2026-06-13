<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Pahappa App</title>
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
            padding: 20px;
        }

        /* Navigation Bar */
        .navbar {
            background: white;
            padding: 15px 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .navbar-brand {
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
        }

        .navbar-user {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-info {
            color: #333;
            font-weight: 500;
        }

        .username {
            color: #667eea;
            font-weight: 600;
        }

        /* Container Styles */
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        /* Welcome Card */
        .welcome-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            padding: 40px;
            text-align: center;
            margin-bottom: 30px;
        }

        .welcome-card h1 {
            color: #333;
            margin-bottom: 15px;
            font-size: 32px;
        }

        .welcome-card p {
            color: #666;
            font-size: 16px;
            line-height: 1.6;
        }

        /* Info Cards Grid */
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .info-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }

        .info-card:hover {
            transform: translateY(-5px);
        }

        .info-card h3 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 20px;
        }

        .info-card p {
            color: #666;
            line-height: 1.6;
        }

        .info-card ul {
            list-style: none;
            padding-left: 0;
        }

        .info-card ul li {
            color: #666;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }

        .info-card ul li:last-child {
            border-bottom: none;
        }

        .info-card ul li:before {
            content: "✓ ";
            color: #667eea;
            font-weight: bold;
            margin-right: 8px;
        }

        /* Button Styles */
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.3s;
        }

        .btn-logout {
            background-color: #e74c3c;
            color: white;
        }

        .btn-logout:hover {
            background-color: #c0392b;
        }

        .btn-about {
            background-color: #667eea;
            color: white;
        }

        .btn-about:hover {
            background-color: #5568d3;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.5);
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .modal-content {
            background-color: white;
            margin: 5% auto;
            padding: 0;
            border-radius: 10px;
            width: 90%;
            max-width: 800px;
            max-height: 85vh;
            overflow-y: auto;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
            animation: slideIn 0.3s;
        }

        @keyframes slideIn {
            from {
                transform: translateY(-50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .modal-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px 30px;
            border-radius: 10px 10px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .modal-header h2 {
            margin: 0;
            font-size: 24px;
        }

        .close {
            color: white;
            font-size: 32px;
            font-weight: bold;
            cursor: pointer;
            line-height: 1;
            transition: transform 0.2s;
        }

        .close:hover {
            transform: scale(1.2);
        }

        .modal-body {
            padding: 30px;
        }

        .modal-section {
            margin-bottom: 30px;
        }

        .modal-section:last-child {
            margin-bottom: 0;
        }

        .modal-section h3 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 20px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }

        .modal-section ul {
            list-style: none;
            padding-left: 0;
        }

        .modal-section ul li {
            color: #666;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }

        .modal-section ul li:last-child {
            border-bottom: none;
        }

        .modal-section ul li:before {
            content: "✓ ";
            color: #667eea;
            font-weight: bold;
            margin-right: 8px;
        }

        .modal-section p {
            color: #666;
            line-height: 1.8;
        }

        .account-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-top: 10px;
        }

        .account-info p {
            margin: 8px 0;
        }

        /* Footer */
        .footer {
            text-align: center;
            color: white;
            margin-top: 30px;
            padding: 20px;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 15px;
            }

            .welcome-card h1 {
                font-size: 24px;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Navigation Bar -->
        <nav class="navbar">
            <div class="navbar-brand">Pahappa App</div>
            <div class="navbar-user">
                <span class="user-info">
                    Welcome, <span class="username"><sec:authentication property="principal.username" /></span>
                </span>
                <!-- Logout Form with CSRF Protection -->
                <form action="${pageContext.request.contextPath}/logout" method="post" style="margin: 0;" class="logout-form">
                    <!-- CSRF Token required for POST logout -->
                    <c:if test="${not empty _csrf}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    </c:if>
                    <button type="submit" class="btn btn-logout">Logout</button>
                </form>
            </div>
        </nav>

        <!-- Welcome Card -->
        <div class="welcome-card">
            <h1>🎉 Welcome to Your Dashboard!</h1>
            <p>
                You have successfully logged in to the Pahappa User Authentication System.
                This is a secure area accessible only to authenticated users.
            </p>
        </div>

        <!-- Quick Actions -->
        <div class="welcome-card" style="margin-bottom: 30px;">
            <h3 style="color: #667eea; margin-bottom: 15px;">Quick Actions</h3>
            <div style="display: flex; gap: 15px; justify-content: center; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/attendance" class="btn" style="background-color: #667eea; color: white; text-decoration: none;">
                    📋 Attendance System
                </a>
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="${pageContext.request.contextPath}/attendance/admin" class="btn" style="background-color: #28a745; color: white; text-decoration: none;">
                        👥 Admin Dashboard
                    </a>
                </sec:authorize>
                <button onclick="openAboutModal()" class="btn btn-about">
                    ℹ️ About
                </button>
            </div>
        </div>

        <!-- About Modal -->
        <div id="aboutModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>ℹ️ About This Application</h2>
                    <span class="close" onclick="closeAboutModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <!-- Security Features -->
                    <div class="modal-section">
                        <h3>🔒 Security Features</h3>
                        <ul>
                            <li>BCrypt password encryption</li>
                            <li>Spring Security authentication</li>
                            <li>CSRF protection enabled</li>
                            <li>Session management</li>
                            <li>Secure form-based login</li>
                        </ul>
                    </div>

                    <!-- Technology Stack -->
                    <div class="modal-section">
                        <h3>💻 Technology Stack</h3>
                        <ul>
                            <li>Spring Boot 3.3+</li>
                            <li>Spring Security 6.x</li>
                            <li>Spring Data JPA</li>
                            <li>MySQL Database</li>
                            <li>JSP Views</li>
                            <li>Maven Build Tool</li>
                        </ul>
                    </div>

                    <!-- Your Account -->
                    <div class="modal-section">
                        <h3>👤 Your Account</h3>
                        <div class="account-info">
                            <p><strong>Username:</strong> <span class="username"><sec:authentication property="principal.username" /></span></p>
                            <p><strong>Status:</strong> Active</p>
                            <p><strong>Role:</strong> <sec:authentication property="principal.authorities" /></p>
                            <p><strong>Session:</strong> Authenticated</p>
                        </div>
                    </div>

                    <!-- Application Features -->
                    <div class="modal-section">
                        <h3>📋 About This Application</h3>
                        <p>
                            This is a complete Spring Boot application demonstrating user authentication, authorization, and advanced attendance tracking.
                            The application includes:
                        </p>
                        
                        <h4 style="margin-top: 20px; color: #667eea;">🔐 Core Features</h4>
                        <ul style="margin-top: 10px;">
                            <li>User registration with validation</li>
                            <li>Secure login with Spring Security</li>
                            <li>Password encryption using BCrypt</li>
                            <li>JPA/Hibernate for database operations</li>
                            <li>MySQL database for production-ready storage</li>
                            <li>JSP views with JSTL tags</li>
                            <li>Responsive design</li>
                        </ul>

                        <h4 style="margin-top: 20px; color: #667eea;">📊 Attendance System</h4>
                        <ul style="margin-top: 10px;">
                            <li><strong>QR Code Check-In/Out:</strong> Scan QR codes for attendance</li>
                            <li><strong>Real-Time Dashboard:</strong> Weekly attendance updates every 30 seconds</li>
                            <li><strong>Attendance History:</strong> View and export attendance records</li>
                            <li><strong>Calendar View:</strong> Visual monthly attendance calendar</li>
                            <li><strong>Statistics:</strong> Track hours worked, late arrivals, and overtime</li>
                            <li><strong>Once-Per-Day Check-In:</strong> Users can only check in once per 24 hours</li>
                        </ul>

                        <h4 style="margin-top: 20px; color: #667eea;">🔒 QR Code Security Features</h4>
                        <ul style="margin-top: 10px;">
                            <li><strong>10-Minute Expiration:</strong> QR codes automatically expire after 10 minutes</li>
                            <li><strong>Single-Use Validation:</strong> Each QR code can only be used once</li>
                            <li><strong>Rate Limiting:</strong> Maximum 2 QR codes per minute per user</li>
                            <li><strong>AES Encryption:</strong> QR code data is encrypted for security</li>
                            <li><strong>Timestamp Validation:</strong> Server-side verification of QR code validity</li>
                        </ul>

                        <h4 style="margin-top: 20px; color: #667eea;">📧 Email Notifications (QSSN API)</h4>
                        <ul style="margin-top: 10px;">
                            <li><strong>Sign-In Notifications:</strong> Email sent on successful check-in</li>
                            <li><strong>Sign-Out Notifications:</strong> Email sent on check-out with duration</li>
                            <li><strong>Late Arrival Alerts:</strong> Automatic notifications for late arrivals</li>
                            <li><strong>Logout Notifications:</strong> Email confirmation on logout</li>
                            <li><strong>QSSN Integration:</strong> Reliable email delivery via QSSN API</li>
                        </ul>

                        <h4 style="margin-top: 20px; color: #667eea;">⚡ Advanced Features</h4>
                        <ul style="margin-top: 10px;">
                            <li><strong>REST API:</strong> JSON endpoints for real-time data access</li>
                            <li><strong>Work Schedule Management:</strong> Configurable work hours and grace periods</li>
                            <li><strong>Admin Dashboard:</strong> Monitor all users' attendance</li>
                            <li><strong>Excel Export:</strong> Download attendance reports</li>
                            <li><strong>Auto-Refresh Charts:</strong> Live data visualization</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <p>&copy; 2026 Pahappa User Authentication System. All rights reserved.</p>
            <p style="margin-top: 10px; font-size: 14px;">Built with Spring Boot 3.3+ and Spring Security</p>
        </div>
    </div>

    <script>
        // Modal functions
        function openAboutModal() {
            document.getElementById('aboutModal').style.display = 'block';
            document.body.style.overflow = 'hidden'; // Prevent background scrolling
        }

        function closeAboutModal() {
            document.getElementById('aboutModal').style.display = 'none';
            document.body.style.overflow = 'auto'; // Restore scrolling
        }

        // Close modal when clicking outside of it
        window.onclick = function(event) {
            const modal = document.getElementById('aboutModal');
            if (event.target == modal) {
                closeAboutModal();
            }
        }

        // Close modal with Escape key
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                closeAboutModal();
            }
        });
    </script>
</body>
</html>