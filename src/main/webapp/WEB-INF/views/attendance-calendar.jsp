<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Calendar - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .calendar-container {
            padding: 30px 15px;
        }
        .calendar-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        .calendar-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e2e8f0;
        }
        .calendar-title {
            font-size: 28px;
            font-weight: bold;
            color: #2d3748;
        }
        .calendar-nav {
            display: flex;
            gap: 10px;
        }
        .calendar-nav button {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            border: none;
            background: #667eea;
            color: white;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .calendar-nav button:hover {
            background: #764ba2;
            transform: scale(1.1);
        }
        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 10px;
        }
        .calendar-day-header {
            text-align: center;
            font-weight: 600;
            color: #4a5568;
            padding: 15px 5px;
            font-size: 14px;
        }
        .calendar-day {
            aspect-ratio: 1;
            border-radius: 12px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            min-height: 80px;
        }
        .calendar-day:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        .calendar-day.empty {
            background: transparent;
            cursor: default;
        }
        .calendar-day.empty:hover {
            transform: none;
            box-shadow: none;
        }
        .calendar-day.on-time {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        .calendar-day.late {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        .calendar-day.absent {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
        }
        .calendar-day.on-leave {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        .calendar-day.no-data {
            background: #f7fafc;
            color: #a0aec0;
            border: 2px dashed #e2e8f0;
        }
        .day-number {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .day-status {
            font-size: 10px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .legend {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e2e8f0;
        }
        .legend-item {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .legend-color {
            width: 30px;
            height: 30px;
            border-radius: 8px;
        }
        .legend-color.on-time {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        .legend-color.late {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        .legend-color.absent {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        .legend-color.on-leave {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        .legend-color.no-data {
            background: #f7fafc;
            border: 2px dashed #e2e8f0;
        }
        .navbar-custom {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .modal-content {
            border-radius: 15px;
        }
        .modal-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="/home">
                <i class="fas fa-calendar me-2"></i>Pahappa Calendar
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
                        <a class="nav-link" href="/attendance/dashboard"><i class="fas fa-chart-bar me-1"></i>Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/attendance/calendar"><i class="fas fa-calendar me-1"></i>Calendar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/qr-scanner"><i class="fas fa-qrcode me-1"></i>QR Scanner</a>
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

    <div class="container calendar-container">
        <div class="calendar-card">
            <!-- Calendar Header -->
            <div class="calendar-header">
                <div class="calendar-title">
                    <i class="fas fa-calendar-alt me-2"></i>
                    <span id="monthYear"></span>
                </div>
                <div class="calendar-nav">
                    <button onclick="previousMonth()" title="Previous Month">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <button onclick="currentMonth()" title="Current Month">
                        <i class="fas fa-calendar-day"></i>
                    </button>
                    <button onclick="nextMonth()" title="Next Month">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>

            <!-- Calendar Grid -->
            <div class="calendar-grid">
                <!-- Day Headers -->
                <div class="calendar-day-header">Sun</div>
                <div class="calendar-day-header">Mon</div>
                <div class="calendar-day-header">Tue</div>
                <div class="calendar-day-header">Wed</div>
                <div class="calendar-day-header">Thu</div>
                <div class="calendar-day-header">Fri</div>
                <div class="calendar-day-header">Sat</div>
                
                <!-- Calendar Days (will be populated by JavaScript) -->
                <div id="calendarDays"></div>
            </div>

            <!-- Legend -->
            <div class="legend">
                <div class="legend-item">
                    <div class="legend-color on-time"></div>
                    <span><i class="fas fa-check-circle me-1"></i>On Time</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color late"></div>
                    <span><i class="fas fa-exclamation-circle me-1"></i>Late Arrival</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color absent"></div>
                    <span><i class="fas fa-times-circle me-1"></i>Absent</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color on-leave"></div>
                    <span><i class="fas fa-umbrella-beach me-1"></i>On Leave</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color no-data"></div>
                    <span><i class="fas fa-question-circle me-1"></i>No Data</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Day Details Modal -->
    <div class="modal fade" id="dayModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">Attendance Details</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="modalBody">
                    <!-- Details will be populated by JavaScript -->
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Calendar data from server (JSP-generated)
        var calendarData = {};
        <c:forEach items="${calendarData}" var="entry">
        calendarData['${entry.key}'] = '${entry.value}';
        </c:forEach>

        var currentYear = parseInt('${year}');
        var currentMonth = parseInt('${month}');

        const monthNames = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];

        function renderCalendar() {
            var monthYearElement = document.getElementById('monthYear');
            monthYearElement.textContent = monthNames[currentMonth - 1] + ' ' + currentYear;

            var calendarDaysElement = document.getElementById('calendarDays');
            calendarDaysElement.innerHTML = '';

            // Get first day of month and number of days
            var firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
            var daysInMonth = new Date(currentYear, currentMonth, 0).getDate();

            // Add empty cells for days before month starts
            for (var i = 0; i < firstDay; i++) {
                var emptyDay = document.createElement('div');
                emptyDay.className = 'calendar-day empty';
                calendarDaysElement.appendChild(emptyDay);
            }

            // Add days of month
            for (var day = 1; day <= daysInMonth; day++) {
                var dateStr = currentYear + '-' +
                    String(currentMonth).padStart(2, '0') + '-' +
                    String(day).padStart(2, '0');
                
                var status = calendarData[dateStr] || 'NO_DATA';
                var dayElement = document.createElement('div');
                
                var className = 'calendar-day ';
                var statusText = '';
                
                switch(status) {
                    case 'ON_TIME':
                        className += 'on-time';
                        statusText = 'On Time';
                        break;
                    case 'LATE':
                        className += 'late';
                        statusText = 'Late';
                        break;
                    case 'ABSENT':
                        className += 'absent';
                        statusText = 'Absent';
                        break;
                    case 'ON_LEAVE':
                        className += 'on-leave';
                        statusText = 'On Leave';
                        break;
                    default:
                        className += 'no-data';
                        statusText = 'No Data';
                }
                
                dayElement.className = className;
                dayElement.innerHTML = '<div class="day-number">' + day + '</div><div class="day-status">' + statusText + '</div>';
                
                dayElement.onclick = (function(d, dy, st) {
                    return function() { showDayDetails(d, dy, st); };
                })(dateStr, day, status);
                
                calendarDaysElement.appendChild(dayElement);
            }
        }

        function showDayDetails(date, day, status) {
            var modal = new bootstrap.Modal(document.getElementById('dayModal'));
            var modalTitle = document.getElementById('modalTitle');
            var modalBody = document.getElementById('modalBody');
            
            modalTitle.textContent = monthNames[currentMonth - 1] + ' ' + day + ', ' + currentYear;
            
            var statusIcon = '';
            var statusClass = '';
            var statusLabel = '';
            
            switch(status) {
                case 'ON_TIME':
                    statusIcon = 'fa-check-circle';
                    statusClass = 'text-success';
                    statusLabel = 'On Time';
                    break;
                case 'LATE':
                    statusIcon = 'fa-exclamation-circle';
                    statusClass = 'text-warning';
                    statusLabel = 'Late Arrival';
                    break;
                case 'ABSENT':
                    statusIcon = 'fa-times-circle';
                    statusClass = 'text-danger';
                    statusLabel = 'Absent';
                    break;
                case 'ON_LEAVE':
                    statusIcon = 'fa-umbrella-beach';
                    statusClass = 'text-info';
                    statusLabel = 'On Leave';
                    break;
                default:
                    statusIcon = 'fa-question-circle';
                    statusClass = 'text-secondary';
                    statusLabel = 'No Attendance Data';
            }
            
            var infoMessage = status === 'NO_DATA' ?
                'No attendance record found for this date.' :
                'View full details in the attendance history page.';
            
            modalBody.innerHTML =
                '<div class="text-center mb-4">' +
                    '<i class="fas ' + statusIcon + ' ' + statusClass + '" style="font-size: 48px;"></i>' +
                    '<h4 class="mt-3">' + statusLabel + '</h4>' +
                '</div>' +
                '<div class="alert alert-info">' +
                    '<i class="fas fa-info-circle me-2"></i>' + infoMessage +
                '</div>' +
                '<div class="d-grid gap-2">' +
                    '<a href="/attendance/history?startDate=' + date + '&endDate=' + date + '" class="btn btn-primary">' +
                        '<i class="fas fa-history me-2"></i>View Full Details' +
                    '</a>' +
                '</div>';
            
            modal.show();
        }

        function previousMonth() {
            currentMonth--;
            if (currentMonth < 1) {
                currentMonth = 12;
                currentYear--;
            }
            window.location.href = '/attendance/calendar?year=' + currentYear + '&month=' + currentMonth;
        }

        function nextMonth() {
            currentMonth++;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
            window.location.href = '/attendance/calendar?year=' + currentYear + '&month=' + currentMonth;
        }

        function currentMonth() {
            var now = new Date();
            window.location.href = '/attendance/calendar?year=' + now.getFullYear() + '&month=' + (now.getMonth() + 1);
        }

        // Initialize calendar on page load
        renderCalendar();
    </script>
</body>
</html>