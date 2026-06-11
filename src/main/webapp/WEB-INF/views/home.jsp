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
            </div>
        </div>

        <!-- Info Cards Grid -->
        <div class="info-grid">
            <!-- Security Features Card -->
            <div class="info-card">
                <h3>🔒 Security Features</h3>
                <ul>
                    <li>BCrypt password encryption</li>
                    <li>Spring Security authentication</li>
                    <li>CSRF protection enabled</li>
                    <li>Session management</li>
                    <li>Secure form-based login</li>
                </ul>
            </div>

            <!-- Technology Stack Card -->
            <div class="info-card">
                <h3>💻 Technology Stack</h3>
                <ul>
                    <li>Spring Boot 3.3+</li>
                    <li>Spring Security 6.x</li>
                    <li>Spring Data JPA</li>
                    <li>H2 Database</li>
                    <li>JSP Views</li>
                    <li>Maven Build Tool</li>
                </ul>
            </div>

            <!-- User Information Card -->
            <div class="info-card">
                <h3>👤 Your Account</h3>
                <p>
                    <strong>Username:</strong> <span class="username">${username}</span><br><br>
                    <strong>Status:</strong> Active<br><br>
                    <strong>Role:</strong> USER<br><br>
                    <strong>Session:</strong> Authenticated
                </p>
            </div>
        </div>

        <!-- Additional Information -->
        <div class="welcome-card">
            <h3 style="color: #667eea; margin-bottom: 15px;">About This Application</h3>
            <p style="text-align: left;">
                This is a complete Spring Boot application demonstrating user authentication and authorization.
                The application includes:
            </p>
            <ul style="text-align: left; margin-top: 15px; color: #666;">
                <li style="margin: 10px 0;">✓ User registration with validation</li>
                <li style="margin: 10px 0;">✓ Secure login with Spring Security</li>
                <li style="margin: 10px 0;">✓ Password encryption using BCrypt</li>
                <li style="margin: 10px 0;">✓ JPA/Hibernate for database operations</li>
                <li style="margin: 10px 0;">✓ H2 in-memory database for easy testing</li>
                <li style="margin: 10px 0;">✓ JSP views with JSTL tags</li>
                <li style="margin: 10px 0;">✓ Responsive design</li>
            </ul>
        </div>

        <!-- Footer -->
        <div class="footer">
            <p>&copy; 2026 Pahappa User Authentication System. All rights reserved.</p>
            <p style="margin-top: 10px; font-size: 14px;">Built with Spring Boot 3.3+ and Spring Security</p>
        </div>
    </div>
</body>
</html>