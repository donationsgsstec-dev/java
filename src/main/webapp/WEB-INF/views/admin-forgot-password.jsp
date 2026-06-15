<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Password Recovery - Pahappa App</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.3);
            padding: 40px;
            width: 100%;
            max-width: 450px;
            border-top: 5px solid #ff6b6b;
        }

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

        .admin-icon {
            text-align: center;
            font-size: 48px;
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
            margin-bottom: 25px;
            font-size: 14px;
            line-height: 1.5;
        }

        .info-notice {
            background: #e8f4fd;
            border: 1px solid #bee3f8;
            color: #2c5282;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 25px;
            font-size: 13px;
            text-align: center;
        }

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
            border-color: #ff6b6b;
        }

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

        .back-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <div style="text-align: center;">
            <span class="admin-badge">&#128274; Admin Access</span>
        </div>

        <div class="admin-icon">&#128272;</div>

        <h2>Password Recovery</h2>
        <p class="subtitle">Enter your admin username and your password will be sent to your registered email address.</p>

        <div class="info-notice">
            &#8505;&#65039; Your current password will be emailed to the address on your account.
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/forgot-password" method="post">
            <c:if test="${not empty _csrf}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </c:if>

            <div class="form-group">
                <label for="username">Admin Username</label>
                <input type="text"
                       id="username"
                       name="username"
                       placeholder="Enter your admin username"
                       required
                       autofocus />
            </div>

            <button type="submit" class="btn btn-admin">
                &#128231; Send Password to Email
            </button>

            <div style="text-align:center;margin-top:15px;font-size:12px;color:#999;">
                <i>&#128274; Password will be sent to your registered email</i>
            </div>
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/admin/login">&#8592; Back to Admin Login</a>
        </div>
    </div>

    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>
