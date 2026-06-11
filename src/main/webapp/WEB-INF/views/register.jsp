<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration - Pahappa App</title>
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
            max-width: 500px;
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

        .required {
            color: #e74c3c;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus {
            outline: none;
            border-color: #667eea;
        }

        /* Error Styles */
        .error {
            color: #e74c3c;
            font-size: 12px;
            margin-top: 5px;
            display: block;
        }

        input.error-input {
            border-color: #e74c3c;
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
        .login-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }

        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .login-link a:hover {
            text-decoration: underline;
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
        <h2>Create Account</h2>
        <p class="subtitle">Join us today! Fill in the details below.</p>

        <!-- Display error messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty usernameError}">
            <div class="alert alert-error">
                ${usernameError}
            </div>
        </c:if>

        <c:if test="${not empty emailError}">
            <div class="alert alert-error">
                ${emailError}
            </div>
        </c:if>

        <!-- Registration Form with Spring Security CSRF Protection -->
        <form:form action="${pageContext.request.contextPath}/register/save"
                   method="post"
                   modelAttribute="user"
                   cssClass="registration-form">
            
            <!-- CSRF Token is automatically included by Spring Form tags -->
            
            <!-- Username Field -->
            <div class="form-group">
                <label for="username">Username <span class="required">*</span></label>
                <form:input path="username" 
                           id="username" 
                           placeholder="Enter username (3-50 characters)"
                           cssClass="${not empty usernameError ? 'error-input' : ''}" />
                <form:errors path="username" cssClass="error" />
            </div>

            <!-- Email Field -->
            <div class="form-group">
                <label for="email">Email Address <span class="required">*</span></label>
                <form:input path="email" 
                           type="email"
                           id="email" 
                           placeholder="Enter your email address"
                           cssClass="${not empty emailError ? 'error-input' : ''}" />
                <form:errors path="email" cssClass="error" />
            </div>

            <!-- First Name Field -->
            <div class="form-group">
                <label for="firstName">First Name <span class="required">*</span></label>
                <form:input path="firstName" 
                           id="firstName" 
                           placeholder="Enter your first name" />
                <form:errors path="firstName" cssClass="error" />
            </div>

            <!-- Last Name Field -->
            <div class="form-group">
                <label for="lastName">Last Name <span class="required">*</span></label>
                <form:input path="lastName" 
                           id="lastName" 
                           placeholder="Enter your last name" />
                <form:errors path="lastName" cssClass="error" />
            </div>

            <!-- Password Field -->
            <div class="form-group">
                <label for="password">Password <span class="required">*</span></label>
                <form:password path="password" 
                              id="password" 
                              placeholder="Enter password (minimum 6 characters)" />
                <form:errors path="password" cssClass="error" />
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary">Create Account</button>
            
            <!-- Note about security -->
            <div style="text-align: center; margin-top: 15px; font-size: 12px; color: #999;">
                <i>🔒 Your password will be securely encrypted</i>
            </div>
        </form:form>

        <!-- Login Link -->
        <div class="login-link">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a>
        </div>
    </div>
</body>
</html>