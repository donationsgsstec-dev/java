<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - Pahappa App</title>
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
            line-height: 1.5;
        }

        /* Icon Style */
        .icon {
            text-align: center;
            font-size: 48px;
            margin-bottom: 20px;
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

        input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus {
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

        .alert-info {
            background-color: #e7f3ff;
            color: #0066cc;
            border: 1px solid #b3d9ff;
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

        .btn-secondary {
            background-color: #6c757d;
            color: white;
            margin-top: 10px;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        /* Link Styles */
        .back-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }

        .back-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        /* Info Box */
        .info-box {
            background-color: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .info-box p {
            margin: 0;
            font-size: 13px;
            color: #555;
            line-height: 1.6;
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
        <div class="icon">🔐</div>
        <h2>Forgot Password</h2>
        <p class="subtitle">Enter your details to receive your password via email</p>

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

        <!-- Information Box -->
        <div class="info-box">
            <p>
                <strong>📧 Password Recovery</strong><br>
                Enter your first name and username. We'll send your password to your registered email address.
            </p>
        </div>

        <!-- Forgot Password Form -->
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
            
            <!-- First Name Field -->
            <div class="form-group">
                <label for="firstName">First Name</label>
                <input type="text" 
                       id="firstName" 
                       name="firstName" 
                       placeholder="Enter your first name"
                       required 
                       autofocus 
                       value="${param.firstName}" />
            </div>

            <!-- Username Field -->
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       placeholder="Enter your username"
                       required 
                       value="${param.username}" />
            </div>

            <!-- CSRF Token (required by Spring Security for POST requests) -->
            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
            </c:if>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary">Send Password to Email</button>
            
            <!-- Back to Login Button -->
            <a href="${pageContext.request.contextPath}/login">
                <button type="button" class="btn btn-secondary">Back to Login</button>
            </a>
        </form>

        <!-- Additional Help -->
        <div class="back-link">
            Remember your password? <a href="${pageContext.request.contextPath}/login">Login here</a>
        </div>
    </div>
</body>
</html>