<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login - Pahappa App</title>
    <style>
        /* Global Styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
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
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
            padding: 40px;
            width: 100%;
            max-width: 450px;
            border-top: 5px solid #ff6b6b;
        }

        /* Header Styles */
        .admin-badge {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
            padding: 8px 20px;
            border-radius: 20px;
            display: inline-block;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 20px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

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

        .security-notice {
            background: #fff3cd;
            border: 1px solid #ffc107;
            color: #856404;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 13px;
            text-align: center;
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
            border-color: #ff6b6b;
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

        .btn-admin {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }

        .btn-admin:hover {
            background: linear-gradient(135deg, #ee5a6f 0%, #dd4a5f 100%);
        }

        /* Forgot Password Link */
        .forgot-link {
            text-align: right;
            margin-top: -10px;
            margin-bottom: 20px;
        }

        .forgot-link a {
            color: #ff6b6b;
            text-decoration: none;
            font-size: 13px;
            font-weight: 500;
        }

        .forgot-link a:hover {
            text-decoration: underline;
        }

        /* Link Styles */
        .back-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }

        .back-link a {
            color: #1e3c72;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        /* Icon Styles */
        .admin-icon {
            text-align: center;
            font-size: 48px;
            margin-bottom: 20px;
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
    </style>
</head>
<body>
    <div class="container">
        <div style="text-align: center;"><span class="admin-badge">&#128274; Admin Access</span></div>
        
        <div class="admin-icon">&#128104;&#8205;&#128188;</div>
        
        <h2>Administrator Login</h2>
        <p class="subtitle">Restricted access for administrators only</p>

        <div class="security-notice">&#9888;&#65039; This area is restricted to authorized administrators only. All login attempts are logged.
        </div>

        <!-- Display error messages -->
        <c:if test="${param.error != null}">
            <div class="alert alert-error">
                ${error != null ? error : 'Invalid username or password. Admin access only.'}
            </div>
        </c:if>

        <!-- Display logout message -->
        <c:if test="${param.logout != null}">
            <div class="alert alert-success">
                ${message != null ? message : 'You have been logged out successfully.'}
            </div>
        </c:if>

        <!-- Display password reset success message -->
        <c:if test="${param.passwordReset != null}">
            <div class="alert alert-success">
                Password recovery email has been sent! Please check your inbox.
            </div>
        </c:if>

        <!-- Admin Login Form with Spring Security CSRF Protection -->
        <form action="${pageContext.request.contextPath}/admin/login" method="post" class="login-form">
            
            <!-- Username Field -->
            <div class="form-group">
                <label for="username">Admin Username</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       placeholder="Enter admin username"
                       required 
                       autofocus />
            </div>

            <!-- Password Field -->
            <div class="form-group">
                <label for="password">Admin Password</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="Enter admin password"
                       required />
            </div>

            <!-- Forgot Password Link -->
            <div class="forgot-link">
                <a href="${pageContext.request.contextPath}/admin/forgot-password">Forgot Password?</a>
            </div>

            <!-- CSRF Token (required by Spring Security for POST requests) -->
            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
            </c:if>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-admin">
                &#128274; Admin Sign In
            </button>
            
            <!-- Security Note -->
            <div style="text-align: center; margin-top: 15px; font-size: 12px; color: #999;">
                <i>&#128274; Secure admin login with Spring Security</i>
            </div>
        </form>

        <!-- Back to Regular Login Link -->
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/login">&#8592; Back to Regular Login</a>
        </div>
    </div>
    <!-- Include Julia Chat Widget -->
    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>



