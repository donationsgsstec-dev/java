<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Username - Pahappa Attendance</title>
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
            max-width: 450px;
        }

        .icon {
            text-align: center;
            font-size: 60px;
            margin-bottom: 20px;
        }

        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 10px;
            font-size: 26px;
        }

        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 30px;
            font-size: 14px;
            line-height: 1.5;
        }

        .session-type {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 10px;
            border-radius: 8px;
            text-align: center;
            font-weight: bold;
            margin-bottom: 20px;
            font-size: 18px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus {
            outline: none;
            border-color: #667eea;
        }

        .btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .info-box {
            background-color: #e8eaf6;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-top: 20px;
            border-radius: 5px;
        }

        .info-box p {
            margin: 0;
            font-size: 13px;
            color: #555;
            line-height: 1.6;
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h2 {
                font-size: 22px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="icon">📱</div>
        <h2>Room QR Code Scan</h2>
        <p class="subtitle">Please enter your username to complete the ${sessionType} process</p>
        
        <div class="session-type">
            ${sessionType}
        </div>

        <form action="${pageContext.request.contextPath}/attendance/qr/room-scan" method="get">
            <input type="hidden" name="token" value="${token}" />
            <input type="hidden" name="type" value="${type}" />
            
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       placeholder="Enter your username"
                       required 
                       autofocus />
            </div>

            <button type="submit" class="btn">Continue</button>
        </form>

        <div class="info-box">
            <p>
                <strong>ℹ️ Note:</strong> Enter the username you use to log into the Pahappa Attendance System. 
                After submitting, you will be automatically checked in or out, and a confirmation email will be sent.
            </p>
        </div>
    </div>
</body>
</html>