<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Attendance Dashboard</title>
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
            max-width: 1400px;
            margin: 0 auto;
        }

        .header-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            margin-bottom: 30px;
        }

        .header-card h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .filter-form {
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
        }

        .filter-form input[type="date"] {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }

        .btn-primary {
            background-color: #667eea;
            color: white;
        }

        .btn-primary:hover {
            background-color: #5568d3;
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

        .table-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        .table-card h2 {
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

        .badge-info {
            background-color: #17a2b8;
            color: white;
        }

        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 15px;
            }

            .filter-form {
                flex-direction: column;
                align-items: stretch;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }

            table {
                font-size: 12px;
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
            <div class="navbar-brand">ðŸ‘¥ Admin Dashboard</div>
            <div class="navbar-links">
                <a href="${pageContext.request.contextPath}/attendance/admin/report">ðŸ“Š Reports</a>
                <a href="${pageContext.request.contextPath}/attendance/admin/room-qr">ðŸ“º Room QR</a>
                <a href="${pageContext.request.contextPath}/attendance/admin/settings">âš™ï¸ Settings</a>
                <form action="${pageContext.request.contextPath}/logout" method="post" style="margin: 0;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn" style="padding: 8px 16px; margin: 0; background-color: #e74c3c; color: white;">Logout</button>
                </form>
            </div>
        </nav>

        <!-- Header with Date Filter -->
        <div class="header-card">
            <h1>Attendance Dashboard</h1>
            <form action="${pageContext.request.contextPath}/attendance/admin" method="get" class="filter-form">
                <label for="date">Select Date:</label>
                <input type="date" id="date" name="date" value="${selectedDate}" required>
                <button type="submit" class="btn btn-primary">View Attendance</button>
                <a href="${pageContext.request.contextPath}/attendance/admin" class="btn" style="background-color: #6c757d; color: white;">Today</a>
            </form>
        </div>

        <!-- Statistics -->
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total Attendance Today</h3>
                <div class="stat-value">${totalToday}</div>
            </div>
            <div class="stat-card">
                <h3>Currently Signed In</h3>
                <div class="stat-value" style="color: #28a745;">${currentlySignedInCount}</div>
            </div>
            <div class="stat-card">
                <h3>Signed Out</h3>
                <div class="stat-value" style="color: #6c757d;">${totalToday - currentlySignedInCount}</div>
            </div>
        </div>

        <!-- Currently Signed In Users -->
        <div class="table-card">
            <h2>Currently Signed In (${currentlySignedInCount})</h2>
            <c:choose>
                <c:when test="${not empty currentlySignedIn}">
                    <table>
                        <thead>
                            <tr>
                                <th>Student Name</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Sign In Time</th>
                                <th>Duration</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${currentlySignedIn}" var="record">
                                <tr>
                                    <td>${record.user.fullName}</td>
                                    <td>${record.user.username}</td>
                                    <td>${record.user.email}</td>
                                    <td>${record.signInTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}</td>
                                    <td>
                                        <%
                                            com.pahappa.app.entity.Attendance rec = (com.pahappa.app.entity.Attendance) pageContext.getAttribute("record");
                                            java.time.Duration dur = java.time.Duration.between(rec.getSignInTime(), java.time.LocalDateTime.now());
                                            pageContext.setAttribute("hours", dur.toHours());
                                            pageContext.setAttribute("minutes", dur.toMinutes() % 60);
                                        %>
                                        ${hours}h ${minutes}m
                                    </td>
                                    <td><span class="badge badge-warning">Signed In</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; color: #666; padding: 20px;">
                        No students are currently signed in.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- All Attendance Records for Selected Date -->
        <div class="table-card">
            <h2>All Attendance Records - ${selectedDate}</h2>
            <c:choose>
                <c:when test="${not empty attendanceList}">
                    <table>
                        <thead>
                            <tr>
                                <th>Student Name</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Sign In</th>
                                <th>Sign Out</th>
                                <th>Duration</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${attendanceList}" var="record">
                                <tr>
                                    <td>${record.user.fullName}</td>
                                    <td>${record.user.username}</td>
                                    <td>${record.user.email}</td>
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
                        No attendance records found for this date.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <!-- Include Julia Chat Widget -->
    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>
