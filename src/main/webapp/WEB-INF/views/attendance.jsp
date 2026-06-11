<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance - Internship System</title>
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
            padding: 20px;
        }

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

        .navbar-links {
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .navbar-links a {
            color: #667eea;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .navbar-links a:hover {
            background-color: #f0f0f0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-weight: 500;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .status-card {
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            text-align: center;
            margin-bottom: 30px;
        }

        .status-card h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .status-indicator {
            display: inline-block;
            padding: 15px 30px;
            border-radius: 50px;
            font-size: 18px;
            font-weight: bold;
            margin: 20px 0;
        }

        .status-signed-in {
            background-color: #28a745;
            color: white;
        }

        .status-signed-out {
            background-color: #6c757d;
            color: white;
        }

        .time-display {
            font-size: 24px;
            color: #667eea;
            margin: 20px 0;
            font-weight: bold;
        }

        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            margin: 10px;
        }

        .btn-sign-in {
            background-color: #28a745;
            color: white;
        }

        .btn-sign-in:hover {
            background-color: #218838;
            transform: translateY(-2px);
        }

        .btn-sign-out {
            background-color: #dc3545;
            color: white;
        }

        .btn-sign-out:hover {
            background-color: #c82333;
            transform: translateY(-2px);
        }

        .btn:disabled {
            background-color: #6c757d;
            cursor: not-allowed;
            opacity: 0.6;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .stat-card h3 {
            color: #667eea;
            margin-bottom: 10px;
            font-size: 16px;
        }

        .stat-card .stat-value {
            font-size: 36px;
            font-weight: bold;
            color: #333;
        }

        .history-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .history-card h2 {
            color: #333;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #667eea;
            color: white;
            font-weight: 600;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-success {
            background-color: #28a745;
            color: white;
        }

        .badge-warning {
            background-color: #ffc107;
            color: #333;
        }

        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 15px;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }

            table {
                font-size: 14px;
            }

            th, td {
                padding: 8px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Navigation Bar -->
        <nav class="navbar">
            <div class="navbar-brand">📋 Attendance System</div>
            <div class="navbar-links">
                <a href="${pageContext.request.contextPath}/home">🏠 Home</a>
                <a href="${pageContext.request.contextPath}/attendance/history">📊 History</a>
                <c:if test="${user.admin}">
                    <a href="${pageContext.request.contextPath}/attendance/admin">👥 Admin View</a>
                </c:if>
                <form action="${pageContext.request.contextPath}/logout" method="post" style="margin: 0;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn" style="padding: 8px 16px; margin: 0; background-color: #e74c3c;">Logout</button>
                </form>
            </div>
        </nav>

        <!-- Success/Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <!-- Status Card -->
        <div class="status-card">
            <h1>Welcome, ${user.fullName}!</h1>
            
            <c:choose>
                <c:when test="${isSignedIn}">
                    <div class="status-indicator status-signed-in">
                        ✓ Currently Signed In
                    </div>
                    <div class="time-display">
                        Signed in at: ${currentAttendance.signInTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}
                    </div>
                    <p style="color: #666; margin: 20px 0;">
                        You are currently signed in. Don't forget to sign out when you leave!
                    </p>
                    <form action="${pageContext.request.contextPath}/attendance/sign-out" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-sign-out">Sign Out</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="status-indicator status-signed-out">
                        ○ Not Signed In
                    </div>
                    <p style="color: #666; margin: 20px 0;">
                        Click the button below to sign in for today.
                    </p>
                    <form action="${pageContext.request.contextPath}/attendance/sign-in" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-sign-in">Sign In</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Statistics -->
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total Attendance Days</h3>
                <div class="stat-value">${totalAttendance}</div>
            </div>
            <div class="stat-card">
                <h3>Current Status</h3>
                <div class="stat-value" style="font-size: 24px;">
                    <c:choose>
                        <c:when test="${isSignedIn}">
                            <span style="color: #28a745;">Signed In</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #6c757d;">Signed Out</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Recent Attendance History -->
        <div class="history-card">
            <h2>Recent Attendance (Last 10 Records)</h2>
            <c:choose>
                <c:when test="${not empty attendanceHistory}">
                    <table>
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Sign In</th>
                                <th>Sign Out</th>
                                <th>Duration</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${attendanceHistory}" var="record" end="9">
                                <tr>
                                    <td>${record.attendanceDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}</td>
                                    <td>${record.signInTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty record.signOutTime}">
                                                ${record.signOutTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${record.formattedDuration}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${record.status == 'SIGNED_IN'}">
                                                <span class="badge badge-warning">Signed In</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-success">Completed</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; color: #666; padding: 20px;">
                        No attendance records yet. Sign in to start tracking your attendance!
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>