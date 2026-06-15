<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Settings - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .settings-container {
            padding: 30px 15px;
        }
        .settings-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .settings-title {
            font-size: 24px;
            font-weight: bold;
            color: #2d3748;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e2e8f0;
        }
        .form-label {
            font-weight: 600;
            color: #4a5568;
        }
        .form-control, .form-select {
            border-radius: 8px;
            border: 2px solid #e2e8f0;
            padding: 10px 15px;
        }
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-save {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-save:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        .info-box {
            background: #e6f7ff;
            border-left: 4px solid #1890ff;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .navbar-custom {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .feature-badge {
            display: inline-block;
            padding: 5px 12px;
            background: #f0f0f0;
            border-radius: 15px;
            font-size: 12px;
            margin: 5px;
        }
        .feature-badge.active {
            background: #d4edda;
            color: #155724;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/attendance/admin">
                <i class="fas fa-cog me-2"></i>Admin Settings
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/admin"><i class="fas fa-users me-1"></i>Admin Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/attendance/admin/report"><i class="fas fa-file-alt me-1"></i>Reports</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/attendance/admin/settings"><i class="fas fa-cog me-1"></i>Settings</a>
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

    <div class="container settings-container">
        <!-- Flash messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Work Schedule Settings -->
        <div class="settings-card">
            <h3 class="settings-title">
                <i class="fas fa-clock me-2"></i>Work Schedule Configuration
            </h3>

            <form id="scheduleForm" action="${pageContext.request.contextPath}/attendance/admin/settings" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <input type="hidden" name="scheduleId" value="${schedule.id}" />

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="workStartTime" class="form-label">
                            <i class="fas fa-clock me-1"></i>Work Start Time
                        </label>
                        <input type="time" class="form-control" id="workStartTime" name="workStartTime"
                               value="${schedule.workStartTime}" required>
                        <small class="text-muted">Default: 9:00 AM</small>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="workEndTime" class="form-label">
                            <i class="fas fa-clock me-1"></i>Work End Time
                        </label>
                        <input type="time" class="form-control" id="workEndTime" name="workEndTime"
                               value="${schedule.workEndTime}" required>
                        <small class="text-muted">Default: 5:00 PM</small>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="gracePeriod" class="form-label">
                            <i class="fas fa-hourglass-half me-1"></i>Grace Period (minutes)
                        </label>
                        <input type="number" class="form-control" id="gracePeriod" name="gracePeriod"
                               value="${schedule.gracePeriodMinutes}" min="0" max="60" required>
                        <small class="text-muted">Time allowed before marking as late</small>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="minimumHours" class="form-label">
                            <i class="fas fa-business-time me-1"></i>Minimum Work Hours
                        </label>
                        <input type="number" class="form-control" id="minimumHours" name="minimumHours"
                               value="${schedule.minimumWorkHours}" min="1" max="12" step="0.5" required>
                        <small class="text-muted">Required hours per day</small>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 mb-3">
                        <label class="form-label">
                            <i class="fas fa-calendar-week me-1"></i>Work Days
                        </label>
                        <div class="d-flex flex-wrap gap-2">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="MON" id="mon" name="workDays"
                                    ${schedule.applicableDays.contains('MON') ? 'checked' : ''}>
                                <label class="form-check-label" for="mon">Monday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="TUE" id="tue" name="workDays"
                                    ${schedule.applicableDays.contains('TUE') ? 'checked' : ''}>
                                <label class="form-check-label" for="tue">Tuesday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="WED" id="wed" name="workDays"
                                    ${schedule.applicableDays.contains('WED') ? 'checked' : ''}>
                                <label class="form-check-label" for="wed">Wednesday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="THU" id="thu" name="workDays"
                                    ${schedule.applicableDays.contains('THU') ? 'checked' : ''}>
                                <label class="form-check-label" for="thu">Thursday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="FRI" id="fri" name="workDays"
                                    ${schedule.applicableDays.contains('FRI') ? 'checked' : ''}>
                                <label class="form-check-label" for="fri">Friday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="SAT" id="sat" name="workDays"
                                    ${schedule.applicableDays.contains('SAT') ? 'checked' : ''}>
                                <label class="form-check-label" for="sat">Saturday</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="SUN" id="sun" name="workDays"
                                    ${schedule.applicableDays.contains('SUN') ? 'checked' : ''}>
                                <label class="form-check-label" for="sun">Sunday</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="notifyLate" name="notifyLate"
                                   value="true" ${schedule.notifyLateArrival ? 'checked' : ''}>
                            <label class="form-check-label" for="notifyLate">
                                <i class="fas fa-envelope me-1"></i>Email Notifications for Late Arrivals
                            </label>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="notifyEarly" name="notifyEarly"
                                   value="true" ${schedule.notifyEarlyDeparture ? 'checked' : ''}>
                            <label class="form-check-label" for="notifyEarly">
                                <i class="fas fa-envelope me-1"></i>Email Notifications for Early Departures
                            </label>
                        </div>
                    </div>
                </div>

                <div class="text-end mt-4">
                    <button type="reset" class="btn btn-secondary me-2">
                        <i class="fas fa-undo me-2"></i>Reset
                    </button>
                    <button type="submit" class="btn-save">
                        <i class="fas fa-save me-2"></i>Save Settings
                    </button>
                </div>
            </form>
        </div>

        <!-- Change Password -->
        <div class="settings-card">
            <h3 class="settings-title">
                <i class="fas fa-key me-2"></i>Change Password
            </h3>

            <c:if test="${not empty pwSuccess}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${pwSuccess}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty pwError}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${pwError}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/attendance/admin/change-password" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="currentPassword" class="form-label">Current Password</label>
                        <input type="password" class="form-control" id="currentPassword"
                               name="currentPassword" required placeholder="Enter current password">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="newPassword" class="form-label">New Password</label>
                        <input type="password" class="form-control" id="newPassword"
                               name="newPassword" required minlength="6" placeholder="Min. 6 characters">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="confirmPassword" class="form-label">Confirm New Password</label>
                        <input type="password" class="form-control" id="confirmPassword"
                               name="confirmPassword" required minlength="6" placeholder="Repeat new password">
                    </div>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn-save">
                        <i class="fas fa-key me-2"></i>Update Password
                    </button>
                </div>
            </form>
        </div>

        <!-- User Role Management -->
        <div class="settings-card">
            <h3 class="settings-title">
                <i class="fas fa-user-shield me-2"></i>User Role Management
            </h3>

            <c:if test="${not empty roleSuccess}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${roleSuccess}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty roleError}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${roleError}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Interns â€” can be promoted -->
            <h5 class="mb-3" style="color:#4a5568;">
                <i class="fas fa-user-graduate me-1"></i>Interns
                <span class="badge bg-secondary ms-2">${interns.size()}</span>
            </h5>
            <c:choose>
                <c:when test="${not empty interns}">
                    <div class="table-responsive mb-4">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th class="text-end">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${interns}" var="u">
                                    <tr>
                                        <td>${u.fullName}</td>
                                        <td><code>${u.username}</code></td>
                                        <td>${u.email}</td>
                                        <td class="text-end">
                                            <form action="${pageContext.request.contextPath}/attendance/admin/promote/${u.id}"
                                                  method="post" style="display:inline;">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                <button type="submit" class="btn btn-sm btn-success"
                                                        onclick="return confirm('Promote ${u.fullName} to Admin?')">
                                                    <i class="fas fa-arrow-up me-1"></i>Promote to Admin
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted mb-4">No interns registered yet.</p>
                </c:otherwise>
            </c:choose>

            <!-- Admins â€” can be demoted (except current user) -->
            <h5 class="mb-3" style="color:#4a5568;">
                <i class="fas fa-user-tie me-1"></i>Admins
                <span class="badge bg-primary ms-2">${admins.size()}</span>
            </h5>
            <c:choose>
                <c:when test="${not empty admins}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th class="text-end">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${admins}" var="u">
                                    <tr>
                                        <td>${u.fullName}</td>
                                        <td>
                                            <code>${u.username}</code>
                                            <c:if test="${u.username == currentAdminUsername}">
                                                <span class="badge bg-info ms-1">You</span>
                                            </c:if>
                                        </td>
                                        <td>${u.email}</td>
                                        <td class="text-end">
                                            <c:choose>
                                                <c:when test="${u.username == currentAdminUsername}">
                                                    <span class="text-muted small">Cannot demote yourself</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <form action="${pageContext.request.contextPath}/attendance/admin/demote/${u.id}"
                                                          method="post" style="display:inline;">
                                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                        <button type="submit" class="btn btn-sm btn-warning"
                                                                onclick="return confirm('Demote ${u.fullName} back to Intern?')">
                                                            <i class="fas fa-arrow-down me-1"></i>Demote to Intern
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">No admins found.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- System Features -->
        <div class="settings-card">
            <h3 class="settings-title">
                <i class="fas fa-star me-2"></i>Active Features
            </h3>
            <div class="d-flex flex-wrap">
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Dashboard Statistics</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Calendar View</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>QR Code System</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Late Detection</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Email Notifications</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Excel Export</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Overtime Tracking</span>
                <span class="feature-badge active"><i class="fas fa-check me-1"></i>Early Departure Detection</span>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="settings-card">
            <h3 class="settings-title">
                <i class="fas fa-bolt me-2"></i>Quick Actions
            </h3>
            <div class="d-flex flex-wrap gap-3">
                <a href="${pageContext.request.contextPath}/attendance/admin" class="btn btn-outline-primary">
                    <i class="fas fa-users me-2"></i>View All Attendance
                </a>
                <a href="${pageContext.request.contextPath}/attendance/admin/report" class="btn btn-outline-success">
                    <i class="fas fa-file-alt me-2"></i>Generate Report
                </a>
                <a href="${pageContext.request.contextPath}/attendance/admin/room-qr" class="btn btn-outline-warning">
                    <i class="fas fa-qrcode me-2"></i>Room QR Code
                </a>
                <a href="${pageContext.request.contextPath}/attendance/admin/export" class="btn btn-outline-info">
                    <i class="fas fa-download me-2"></i>Export Data
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Include Julia Chat Widget -->
    <%@ include file="julia-chat-widget.jsp" %>
</body>
</html>
