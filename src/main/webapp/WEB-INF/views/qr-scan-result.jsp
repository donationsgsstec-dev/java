<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QR Scan Result - Pahappa Attendance</title>
    <style>
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

        .container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            padding: 40px;
            width: 100%;
            max-width: 500px;
            text-align: center;
        }

        .icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        .success-icon {
            color: #28a745;
        }

        .error-icon {
            color: #dc3545;
        }

        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 28px;
        }

        .action {
            color: #667eea;
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .username {
            color: #666;
            font-size: 18px;
            margin-bottom: 20px;
        }

        .message {
            color: #555;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: transform 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .info-box {
            background-color: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-top: 20px;
            text-align: left;
            border-radius: 5px;
        }

        .info-box p {
            margin: 5px 0;
            font-size: 14px;
            color: #666;
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h1 {
                font-size: 24px;
            }

            .icon {
                font-size: 60px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <c:choose>
            <c:when test="${success}">
                <div class="icon success-icon">✓</div>
                <h1>Success!</h1>
                <div class="action">${action}</div>
                <div class="username">Welcome, ${username}!</div>
                <p class="message">${message}</p>
                
                <div class="info-box">
                    <p><strong>📧 Email Notification:</strong> A confirmation email has been sent to your registered email address.</p>
                    <p><strong>🕐 Time:</strong> <%= new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) %></p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="icon error-icon">✗</div>
                <h1>${error}</h1>
                <p class="message">${message}</p>
            </c:otherwise>
        </c:choose>
        
        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/attendance" class="btn">Go to Attendance Dashboard</a>
        </div>
    </div>
</body>
</html>