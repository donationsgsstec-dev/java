<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Julia Test — Pahappa</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card {
            background: white;
            border-radius: 20px;
            padding: 50px 60px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
            text-align: center;
            max-width: 480px;
            width: 90%;
        }
        .pulse {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #28a745, #38ef7d);
            margin: 0 auto 25px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 36px;
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0%   { box-shadow: 0 0 0 0 rgba(40,167,69,0.5); }
            70%  { box-shadow: 0 0 0 20px rgba(40,167,69,0); }
            100% { box-shadow: 0 0 0 0 rgba(40,167,69,0); }
        }
        h1 { color: #2d3748; margin-bottom: 10px; font-size: 28px; }
        p  { color: #718096; font-size: 16px; line-height: 1.6; }
        .badge {
            display: inline-block;
            margin-top: 20px;
            padding: 8px 20px;
            background: #d4edda;
            color: #155724;
            border-radius: 20px;
            font-weight: 600;
            font-size: 14px;
        }
        .timestamp {
            margin-top: 15px;
            font-size: 13px;
            color: #a0aec0;
        }
    </style>
</head>
<body>
    <div class="card">
        <div class="pulse">🤖</div>
        <h1>AI is Responding</h1>
        <p>The Pahappa Attendance System is live and the application server is responding correctly.</p>
        <span class="badge">✓ System Online</span>
        <p class="timestamp">Checked at: <%= new java.util.Date() %></p>
    </div>
</body>
</html>
