<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Dashboard - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <script src="https://Gaston895-AI.hf.space/sdk/gss-sdk.js"></script>
    <style>
        /* ===== GLOBAL STYLES ===== */
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --success-gradient: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            --warning-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            --danger-gradient: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            --card-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            --card-shadow-hover: 0 8px 30px rgba(0, 0, 0, 0.12);
            --border-radius: 16px;
            --spacing-unit: 8px;
        }
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            background: var(--primary-gradient);
            min-height: 100vh;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            color: #2d3748;
            line-height: 1.6;
        }
        
        /* ===== LAYOUT ===== */
        .dashboard-container {
            max-width: 1400px;
            margin: 0 auto;
            padding: calc(var(--spacing-unit) * 4) calc(var(--spacing-unit) * 2);
        }
        
        /* ===== CARDS ===== */
        .stat-card {
            background: white;
            border-radius: 12px;
            padding: calc(var(--spacing-unit) * 2.5);
            margin-bottom: calc(var(--spacing-unit) * 2);
            box-shadow: var(--card-shadow);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            border: 1px solid rgba(0, 0, 0, 0.05);
            height: 100%;
        }
        
        .stat-card:hover {
            transform: translateY(-3px);
            box-shadow: var(--card-shadow-hover);
        }
        
        /* ===== STAT CARDS ===== */
        .stat-icon {
            width: 48px;
            height: 48px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            margin-bottom: calc(var(--spacing-unit) * 1.5);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        .stat-icon.blue {
            background: var(--primary-gradient);
            color: white;
        }
        
        .stat-icon.green {
            background: var(--success-gradient);
            color: white;
        }
        
        .stat-icon.orange {
            background: var(--warning-gradient);
            color: white;
        }
        
        .stat-icon.red {
            background: var(--danger-gradient);
            color: white;
        }
        
        .stat-value {
            font-size: 28px;
            font-weight: 700;
            color: #1a202c;
            margin: calc(var(--spacing-unit)) 0 calc(var(--spacing-unit) * 0.5);
            letter-spacing: -0.5px;
            line-height: 1;
        }
        
        .stat-label {
            color: #718096;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            line-height: 1.4;
        }
        
        .stat-change {
            font-size: 12px;
            font-weight: 600;
            margin-top: calc(var(--spacing-unit));
        }
        
        .stat-change.positive {
            color: #38a169;
        }
        
        .stat-change.negative {
            color: #e53e3e;
        }
        /* ===== CHART CARDS ===== */
        .chart-card {
            background: white;
            border-radius: var(--border-radius);
            padding: calc(var(--spacing-unit) * 4);
            margin-bottom: calc(var(--spacing-unit) * 3);
            box-shadow: var(--card-shadow);
            border: 1px solid rgba(0, 0, 0, 0.05);
        }
        
        .chart-title {
            font-size: 20px;
            font-weight: 700;
            color: #1a202c;
            margin-bottom: calc(var(--spacing-unit) * 3);
            display: flex;
            align-items: center;
            gap: calc(var(--spacing-unit) * 1.5);
        }
        
        .chart-title i {
            color: #667eea;
        }
        .progress-ring {
            width: 150px;
            height: 150px;
            margin: 0 auto;
        }
        .status-badge {
            display: inline-block;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
        }
        .status-badge.signed-in {
            background: #d4edda;
            color: #155724;
        }
        .status-badge.signed-out {
            background: #f8d7da;
            color: #721c24;
        }
        /* ===== NAVIGATION ===== */
        .navbar-custom {
            background: rgba(255, 255, 255, 0.98);
            backdrop-filter: blur(20px);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
            padding: calc(var(--spacing-unit) * 2) 0;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
        }
        
        .navbar-brand {
            font-weight: 700;
            font-size: 24px;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        /* ===== BUTTONS ===== */
        .btn-custom {
            border-radius: 12px;
            padding: calc(var(--spacing-unit) * 1.5) calc(var(--spacing-unit) * 3);
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
        }
        
        .btn-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        /* Chat Widget Styles */
        .chat-widget {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 350px;
            max-height: 500px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            display: none;
            flex-direction: column;
            z-index: 1000;
        }
        .chat-widget.active {
            display: flex;
        }
        .chat-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px;
            border-radius: 15px 15px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .chat-header h5 {
            margin: 0;
            font-size: 16px;
        }
        .chat-close {
            background: none;
            border: none;
            color: white;
            font-size: 20px;
            cursor: pointer;
        }
        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 15px;
            max-height: 350px;
        }
        .chat-message {
            margin-bottom: 10px;
            padding: 10px;
            border-radius: 10px;
            max-width: 80%;
        }
        .chat-message.user {
            background: #667eea;
            color: white;
            margin-left: auto;
        }
        .chat-message.bot {
            background: #f0f0f0;
            color: #333;
        }
        .chat-input-container {
            padding: 15px;
            border-top: 1px solid #e0e0e0;
        }
        .chat-input {
            display: flex;
            gap: 10px;
        }
        .chat-input input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 20px;
            outline: none;
        }
        .chat-input button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 20px;
            cursor: pointer;
        }
        .chat-toggle {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 50%;
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
            z-index: 999;
        }
        .chat-toggle:hover {
            transform: scale(1.1);
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="/home">
                <i class="fas fa-chart-line me-2"></i>Pahappa Dashboard
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance"><i class="fas fa-clock me-1"></i>Attendance</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/attendance/dashboard"><i class="fas fa-chart-bar me-1"></i>Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/calendar"><i class="fas fa-calendar me-1"></i>Calendar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/qr-scanner"><i class="fas fa-qrcode me-1"></i>Sign In/Out</a>
                    </li>
                    <li class="nav-item">
                        <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline; margin: 0;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="nav-link btn btn-link" style="border: none; background: none; cursor: pointer;">
                                <i class="fas fa-sign-out-alt me-1"></i>Logout
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container dashboard-container">
        <!-- Welcome Section -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="stat-card">
                    <h2 class="mb-2">Welcome back, ${user.firstName}! 👋</h2>
                    <p class="text-muted mb-3">Here's your attendance overview</p>
                    <c:choose>
                        <c:when test="${stats.currentlySignedIn}">
                            <span class="status-badge signed-in">
                                <i class="fas fa-check-circle me-2"></i>Currently Signed In (${stats.currentSignInTime})
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge signed-out">
                                <i class="fas fa-times-circle me-2"></i>Not Signed In
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Statistics Cards -->
        <div class="row">
            <!-- Weekly Hours -->
            <div class="col-md-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon blue">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="stat-value">
                        <fmt:formatNumber value="${stats.totalHoursThisWeek}" maxFractionDigits="1"/>h
                    </div>
                    <div class="stat-label">Hours This Week</div>
                </div>
            </div>

            <!-- Monthly Hours -->
            <div class="col-md-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon green">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <div class="stat-value">
                        <fmt:formatNumber value="${stats.totalHoursThisMonth}" maxFractionDigits="1"/>h
                    </div>
                    <div class="stat-label">Hours This Month</div>
                </div>
            </div>

            <!-- Late Arrivals -->
            <div class="col-md-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon orange">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-value">${stats.lateArrivalsThisMonth}</div>
                    <div class="stat-label">Late Arrivals</div>
                </div>
            </div>

            <!-- Attendance Rate -->
            <div class="col-md-3 col-sm-6">
                <div class="stat-card">
                    <div class="stat-icon red">
                        <i class="fas fa-percentage"></i>
                    </div>
                    <div class="stat-value">
                        <fmt:formatNumber value="${stats.attendanceRateMonth}" maxFractionDigits="1"/>%
                    </div>
                    <div class="stat-label">Attendance Rate</div>
                </div>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row mt-4">
            <!-- Weekly Attendance Chart -->
            <div class="col-lg-8">
                <div class="chart-card">
                    <h5 class="chart-title">
                        <i class="fas fa-chart-bar me-2"></i>Weekly Attendance Overview
                    </h5>
                    <canvas id="weeklyChart" height="80"></canvas>
                </div>
            </div>

            <!-- Attendance Rate Gauge -->
            <div class="col-lg-4">
                <div class="chart-card text-center">
                    <h5 class="chart-title">
                        <i class="fas fa-tachometer-alt me-2"></i>Monthly Attendance Rate
                    </h5>
                    <canvas id="attendanceGauge" height="200"></canvas>
                    <div class="mt-3">
                        <h3 class="mb-0">
                            <fmt:formatNumber value="${stats.attendanceRateMonth}" maxFractionDigits="1"/>%
                        </h3>
                        <p class="text-muted small">
                            ${stats.daysAttendedThisMonth} of ${stats.daysAttendedThisMonth + (30 - stats.daysAttendedThisMonth)} days
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Detailed Statistics -->
        <div class="row mt-4">
            <div class="col-lg-6">
                <div class="chart-card">
                    <h5 class="chart-title">
                        <i class="fas fa-info-circle me-2"></i>This Week's Summary
                    </h5>
                    <table class="table table-borderless">
                        <tr>
                            <td><i class="fas fa-calendar-day text-primary me-2"></i>Days Attended</td>
                            <td class="text-end fw-bold">${stats.daysAttendedThisWeek}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-clock text-success me-2"></i>Total Hours</td>
                            <td class="text-end fw-bold">
                                <fmt:formatNumber value="${stats.totalHoursThisWeek}" maxFractionDigits="1"/>h
                            </td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-exclamation-circle text-warning me-2"></i>Late Arrivals</td>
                            <td class="text-end fw-bold">${stats.lateArrivalsThisWeek}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-door-open text-danger me-2"></i>Early Departures</td>
                            <td class="text-end fw-bold">${stats.earlyDeparturesThisWeek}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-percentage text-info me-2"></i>Attendance Rate</td>
                            <td class="text-end fw-bold">
                                <fmt:formatNumber value="${stats.attendanceRateWeek}" maxFractionDigits="1"/>%
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="chart-card">
                    <h5 class="chart-title">
                        <i class="fas fa-calendar-alt me-2"></i>This Month's Summary
                    </h5>
                    <table class="table table-borderless">
                        <tr>
                            <td><i class="fas fa-calendar-check text-primary me-2"></i>Days Attended</td>
                            <td class="text-end fw-bold">${stats.daysAttendedThisMonth}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-clock text-success me-2"></i>Total Hours</td>
                            <td class="text-end fw-bold">
                                <fmt:formatNumber value="${stats.totalHoursThisMonth}" maxFractionDigits="1"/>h
                            </td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-exclamation-circle text-warning me-2"></i>Late Arrivals</td>
                            <td class="text-end fw-bold">${stats.lateArrivalsThisMonth}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-door-open text-danger me-2"></i>Early Departures</td>
                            <td class="text-end fw-bold">${stats.earlyDeparturesThisMonth}</td>
                        </tr>
                        <tr>
                            <td><i class="fas fa-plus-circle text-success me-2"></i>Overtime Hours</td>
                            <td class="text-end fw-bold">
                                <fmt:formatNumber value="${stats.overtimeHoursThisMonth}" maxFractionDigits="1"/>h
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="chart-card">
                    <h5 class="chart-title">
                        <i class="fas fa-bolt me-2"></i>Quick Actions
                    </h5>
                    <div class="d-flex flex-wrap gap-3">
                        <a href="/attendance/qr-scanner" class="btn btn-primary btn-custom">
                            <i class="fas fa-qrcode me-2"></i>Sign In/Out
                        </a>
                        <a href="/attendance/calendar" class="btn btn-success btn-custom">
                            <i class="fas fa-calendar me-2"></i>View Calendar
                        </a>
                        <a href="/attendance/history" class="btn btn-info btn-custom">
                            <i class="fas fa-history me-2"></i>View History
                        </a>
                        <a href="/attendance/export" class="btn btn-warning btn-custom">
                            <i class="fas fa-download me-2"></i>Export Data
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Get attendance rate from JSP
        var attendanceRate = parseFloat('${stats.attendanceRateMonth}');
        
        // Weekly Attendance Chart with Real-Time Data
        var weeklyCtx = document.getElementById('weeklyChart').getContext('2d');
        var weeklyChart = new Chart(weeklyCtx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    label: 'Hours Worked',
                    data: [0, 0, 0, 0, 0, 0, 0],
                    backgroundColor: 'rgba(102, 126, 234, 0.8)',
                    borderColor: 'rgba(102, 126, 234, 1)',
                    borderWidth: 2,
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 12,
                        ticks: {
                            callback: function(value) {
                                return value + 'h';
                            }
                        }
                    }
                }
            }
        });

        // Function to update weekly chart with real-time data
        function updateWeeklyChart() {
            fetch('/api/attendance/weekly-overview')
                .then(response => response.json())
                .then(data => {
                    const days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
                    const hoursData = days.map(day => {
                        const dayData = data.weeklyData[day];
                        if (dayData && dayData.present && dayData.duration) {
                            // Parse duration string like "8h 30m" to hours
                            const match = dayData.duration.match(/(\d+)h\s*(\d+)?m?/);
                            if (match) {
                                const hours = parseInt(match[1]) || 0;
                                const minutes = parseInt(match[2]) || 0;
                                return hours + (minutes / 60);
                            }
                        }
                        return 0;
                    });
                    
                    weeklyChart.data.datasets[0].data = hoursData;
                    weeklyChart.update();
                })
                .catch(error => console.error('Error fetching weekly data:', error));
        }

        // Initial load
        updateWeeklyChart();

        // Auto-refresh every 30 seconds
        setInterval(updateWeeklyChart, 30000);

        // Attendance Rate Gauge
        var gaugeCtx = document.getElementById('attendanceGauge').getContext('2d');
        var gaugeColor = attendanceRate >= 90 ? '#38ef7d' : (attendanceRate >= 75 ? '#f5576c' : '#fee140');
        var gaugeChart = new Chart(gaugeCtx, {
            type: 'doughnut',
            data: {
                datasets: [{
                    data: [attendanceRate, 100 - attendanceRate],
                    backgroundColor: [gaugeColor, '#e2e8f0'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                cutout: '75%',
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        enabled: false
                    }
                }
            }
        });

        // Initialize GSS Client
        const gss = new GSSClient({
            apiKey: 'gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ',
            cfWorkerUrl: 'https://node.gss-tec.com',
            hfEngineUrl: 'https://Gaston895-AI.hf.space',
            model: 'llama-3.1-8b-instant'
        });

        // Chat functionality
        function toggleChat() {
            const chatWidget = document.getElementById('chatWidget');
            const chatToggle = document.getElementById('chatToggle');
            chatWidget.classList.toggle('active');
            chatToggle.style.display = chatWidget.classList.contains('active') ? 'none' : 'block';
        }

        function handleKeyPress(event) {
            if (event.key === 'Enter') {
                sendMessage();
            }
        }

        async function sendMessage() {
            const input = document.getElementById('chatInput');
            const message = input.value.trim();
            
            if (!message) return;

            // Add user message to chat
            addMessageToChat(message, 'user');
            input.value = '';

            // Show typing indicator
            const typingDiv = document.createElement('div');
            typingDiv.className = 'chat-message bot';
            typingDiv.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Julia is typing...';
            typingDiv.id = 'typing-indicator';
            document.getElementById('chatMessages').appendChild(typingDiv);
            scrollToBottom();

            try {
                // Send message to GSS API
                const reply = await gss.chat([
                    { role: 'system', content: 'You are Julia, a helpful AI assistant for Pahappa Limited. Answer questions about the company, its services, and help users with their queries.' },
                    { role: 'user', content: message }
                ]);

                // Remove typing indicator
                const typingIndicator = document.getElementById('typing-indicator');
                if (typingIndicator) {
                    typingIndicator.remove();
                }

                // Add bot response to chat
                addMessageToChat(reply, 'bot');
            } catch (error) {
                console.error('Error sending message:', error);
                
                // Remove typing indicator
                const typingIndicator = document.getElementById('typing-indicator');
                if (typingIndicator) {
                    typingIndicator.remove();
                }
                
                addMessageToChat('Sorry, I encountered an error. Please try again.', 'bot');
            }
        }

        function addMessageToChat(message, sender) {
            const messagesDiv = document.getElementById('chatMessages');
            const messageDiv = document.createElement('div');
            messageDiv.className = `chat-message ${sender}`;
            messageDiv.textContent = message;
            messagesDiv.appendChild(messageDiv);
            scrollToBottom();
        }

        function scrollToBottom() {
            const messagesDiv = document.getElementById('chatMessages');
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }
    </script>
    
    <!-- Include Julia Chat Widget -->
    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>