<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Room QR Codes - Pahappa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar-custom {
            background: rgba(255,255,255,0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        /* ── QR card ─────────────────────────────────────── */
        .qr-card {
            background: white;
            border-radius: 20px;
            padding: 30px 25px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
            text-align: center;
        }
        .qr-card.check-in  { border-top: 6px solid #28a745; }
        .qr-card.check-out { border-top: 6px solid #667eea; }

        .qr-card h3 { font-weight: 800; letter-spacing: 1px; }
        .qr-card.check-in  h3 { color: #28a745; }
        .qr-card.check-out h3 { color: #667eea; }

        .qr-img {
            display: block;
            margin: 0 auto;
            border-radius: 12px;
            max-width: 100%;
        }
        /* ── Countdown ───────────────────────────────────── */
        .countdown {
            font-size: 2rem;
            font-weight: 800;
            color: #667eea;
        }
        .countdown.warning { color: #e67e22; }
        .countdown.danger  { color: #e74c3c; }
        /* ── Badges ──────────────────────────────────────── */
        .badge-active  { background: #28a745; color: white; }
        .badge-expired { background: #dc3545; color: white; }
        /* ── Generate panel ──────────────────────────────── */
        .generate-card {
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
            max-width: 520px;
            margin: 0 auto;
            text-align: center;
        }
        .info-card {
            background: white;
            border-radius: 16px;
            padding: 25px 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            max-width: 900px;
            margin: 0 auto;
        }
    </style>
</head>
<body>

    <!-- Nav -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/attendance/admin">
                <i class="fas fa-qrcode me-2"></i>Room QR Codes
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/attendance/admin">
                            <i class="fas fa-users me-1"></i>Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/attendance/admin/settings">
                            <i class="fas fa-cog me-1"></i>Settings
                        </a>
                    </li>
                    <li class="nav-item">
                        <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline;margin:0;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="nav-link btn btn-link" style="border:none;background:none;cursor:pointer;">
                                <i class="fas fa-sign-out-alt me-1"></i>Logout
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container pb-5">

        <!-- Flash messages -->
        <c:if test="${not empty roomQRSuccess}">
            <div class="alert alert-success alert-dismissible fade show mb-4" style="max-width:900px;margin:0 auto 20px;">
                <i class="fas fa-check-circle me-2"></i>${roomQRSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty roomQRError}">
            <div class="alert alert-danger alert-dismissible fade show mb-4" style="max-width:900px;margin:0 auto 20px;">
                <i class="fas fa-exclamation-circle me-2"></i>${roomQRError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:choose>
            <%-- ══ At least one active session → show QR codes ══════════════ --%>
            <c:when test="${not empty checkInSession or not empty checkOutSession}">

                <!-- Action row -->
                <div class="d-flex justify-content-center gap-3 mb-4" style="max-width:900px;margin:0 auto;">
                    <form action="${pageContext.request.contextPath}/attendance/admin/room-qr/generate" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="label"
                               value="${not empty checkInSession ? checkInSession.label : 'Office Session'}"/>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-sync-alt me-2"></i>Regenerate Both Codes
                        </button>
                    </form>
                    <form action="${pageContext.request.contextPath}/attendance/admin/room-qr/revoke" method="post"
                          onsubmit="return confirm('Revoke all active QR codes?')">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-outline-danger">
                            <i class="fas fa-ban me-2"></i>Revoke All
                        </button>
                    </form>
                </div>

                <!-- Two QR codes side by side -->
                <div class="row g-4 justify-content-center" style="max-width:900px;margin:0 auto;">

                    <%-- CHECK IN card --%>
                    <div class="col-md-6">
                        <div class="qr-card check-in">
                            <h3><i class="fas fa-sign-in-alt me-2"></i>CHECK IN</h3>
                            <c:choose>
                                <c:when test="${not empty checkInSession and checkInSession.valid}">
                                    <span class="badge badge-active px-3 py-2 mb-3 d-inline-block">
                                        <i class="fas fa-check-circle me-1"></i>Active
                                    </span>
                                    <c:if test="${not empty checkInSession.label}">
                                        <span class="badge bg-secondary px-3 py-2 mb-3 ms-1 d-inline-block">
                                            ${checkInSession.label}
                                        </span>
                                    </c:if>
                                    <img src="data:image/png;base64,${checkInQRBase64}"
                                         alt="Check-In QR" class="qr-img mb-3" width="280" height="280" id="checkInQR">
                                    <div class="mb-3">
                                        <button onclick="downloadQR('checkInQR', 'CheckIn-QR-Code.png')"
                                                class="btn btn-success btn-sm">
                                            <i class="fas fa-download me-1"></i>Download Check-In QR
                                        </button>
                                    </div>
                                    <div class="countdown mb-1" id="countdownIn">loading…</div>
                                    <p class="text-muted small mb-0">Expires at ${checkInSession.getFormattedExpiresAt()}</p>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-expired px-3 py-2 mb-3 d-inline-block">
                                        <i class="fas fa-times-circle me-1"></i>Expired / Not Active
                                    </span>
                                    <div style="font-size:80px;color:#ddd;margin:20px 0;">
                                        <i class="fas fa-qrcode"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <%-- CHECK OUT card --%>
                    <div class="col-md-6">
                        <div class="qr-card check-out">
                            <h3><i class="fas fa-sign-out-alt me-2"></i>CHECK OUT</h3>
                            <c:choose>
                                <c:when test="${not empty checkOutSession and checkOutSession.valid}">
                                    <span class="badge badge-active px-3 py-2 mb-3 d-inline-block">
                                        <i class="fas fa-check-circle me-1"></i>Active
                                    </span>
                                    <c:if test="${not empty checkOutSession.label}">
                                        <span class="badge bg-secondary px-3 py-2 mb-3 ms-1 d-inline-block">
                                            ${checkOutSession.label}
                                        </span>
                                    </c:if>
                                    <img src="data:image/png;base64,${checkOutQRBase64}"
                                         alt="Check-Out QR" class="qr-img mb-3" width="280" height="280" id="checkOutQR">
                                    <div class="mb-3">
                                        <button onclick="downloadQR('checkOutQR', 'CheckOut-QR-Code.png')"
                                                class="btn btn-primary btn-sm">
                                            <i class="fas fa-download me-1"></i>Download Check-Out QR
                                        </button>
                                    </div>
                                    <div class="countdown mb-1" id="countdownOut">loading…</div>
                                    <p class="text-muted small mb-0">Expires at ${checkOutSession.getFormattedExpiresAt()}</p>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-expired px-3 py-2 mb-3 d-inline-block">
                                        <i class="fas fa-times-circle me-1"></i>Expired / Not Active
                                    </span>
                                    <div style="font-size:80px;color:#ddd;margin:20px 0;">
                                        <i class="fas fa-qrcode"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                </div><!-- /row -->

                <p class="text-center text-white mt-3 small opacity-75">
                    <i class="fas fa-expand me-1"></i>Press F11 for full-screen display mode
                </p>

                <script>
                    // Download QR code function
                    function downloadQR(imgId, filename) {
                        const img = document.getElementById(imgId);
                        if (!img) return;
                        
                        // Wait for image to load completely
                        if (!img.complete) {
                            img.onload = function() {
                                performDownload(img, filename);
                            };
                        } else {
                            performDownload(img, filename);
                        }
                    }
                    
                    function performDownload(img, filename) {
                        // Create a canvas with the natural (full) size of the image
                        const canvas = document.createElement('canvas');
                        canvas.width = img.naturalWidth || 280;
                        canvas.height = img.naturalHeight || 280;
                        
                        const ctx = canvas.getContext('2d');
                        // Draw the image at full resolution
                        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
                        
                        // Convert to blob and download
                        canvas.toBlob(function(blob) {
                            const url = URL.createObjectURL(blob);
                            const a = document.createElement('a');
                            a.href = url;
                            a.download = filename;
                            document.body.appendChild(a);
                            a.click();
                            document.body.removeChild(a);
                            URL.revokeObjectURL(url);
                        }, 'image/png');
                    }
                    
                    function startCountdown(elementId, expiresAtStr) {
                        if (!expiresAtStr) return;
                        const el = document.getElementById(elementId);
                        if (!el) return;
                        // Parse format: "2026-06-14 09:00:00 EAT" - remove " EAT" and replace space with T
                        const dateStr = expiresAtStr.replace(' EAT', '').replace(' ', 'T');
                        const target = new Date(dateStr);
                        function tick() {
                            const diff = Math.max(0, Math.floor((target - new Date()) / 1000));
                            const m = Math.floor(diff / 60), s = diff % 60;
                            el.textContent = m + 'm ' + (s < 10 ? '0' : '') + s + 's';
                            el.className = 'countdown';
                            if (diff <= 60)  el.classList.add('danger');
                            else if (diff <= 180) el.classList.add('warning');
                            if (diff <= 0) { el.textContent = 'Expired'; setTimeout(() => location.reload(), 2000); }
                        }
                        tick(); setInterval(tick, 1000);
                    }
                    <c:if test="${not empty checkInSession and checkInSession.valid}">
                    startCountdown('countdownIn', '${checkInSession.getFormattedExpiresAt()}');
                    </c:if>
                    <c:if test="${not empty checkOutSession and checkOutSession.valid}">
                    startCountdown('countdownOut', '${checkOutSession.getFormattedExpiresAt()}');
                    </c:if>
                </script>

            </c:when>

            <%-- ══ No active sessions → generate panel ══════════════════════ --%>
            <c:otherwise>
                <div class="generate-card">
                    <div style="font-size:80px;color:#ccc;margin-bottom:20px;">
                        <i class="fas fa-qrcode"></i>
                    </div>
                    <h4 class="fw-bold mb-2" style="color:#2d3748;">No Active QR Codes</h4>
                    <p class="text-muted mb-4">
                        Generate a Check-In and Check-Out QR code pair to allow interns to sign in.
                    </p>
                    <form action="${pageContext.request.contextPath}/attendance/admin/room-qr/generate" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="mb-3 text-start">
                            <label for="label" class="form-label fw-semibold">Session Label (optional)</label>
                            <input type="text" id="label" name="label" class="form-control"
                                   placeholder="e.g. Morning session" maxlength="100">
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg w-100">
                            <i class="fas fa-qrcode me-2"></i>Generate Check-In &amp; Check-Out Codes
                        </button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- How it works -->
        <div class="info-card mt-4">
            <h5 class="fw-bold mb-3" style="color:#2d3748;">
                <i class="fas fa-info-circle me-2 text-primary"></i>How it works
            </h5>
            <div class="row">
                <div class="col-md-6">
                    <ul style="color:#4a5568;line-height:2.2;">
                        <li>Two codes are generated at once — <strong>Check-In</strong> and <strong>Check-Out</strong>.</li>
                        <li>Display this screen on a monitor <strong>inside the office</strong>.</li>
                        <li>Interns scan the appropriate code on arrival and departure.</li>
                    </ul>
                </div>
                <div class="col-md-6">
                    <ul style="color:#4a5568;line-height:2.2;">
                        <li>The system rejects Check-Out if the intern never checked in.</li>
                        <li>Check-In codes expire at <strong>work start time</strong> (09:00 by default).</li>
                        <li>Check-Out codes expire at <strong>work end time</strong> (17:00 by default).</li>
                    </ul>
                </div>
            </div>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
