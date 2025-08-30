<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Login - Mini Asaas</title>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }
        .login-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .login-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        .login-header {
            background: #007bff;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }
        .login-header h3 {
            margin: 0;
            font-size: 24px;
        }
        .login-body {
            padding: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            box-sizing: border-box;
        }
        .form-control:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
        }
        .btn-primary {
            background: #007bff;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
        }
        .btn-primary:hover {
            background: #0056b3;
        }
        .alert {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 5px;
            border: 1px solid transparent;
        }
        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border-color: #f5c6cb;
        }
        .checkbox-group {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        .checkbox-group input[type="checkbox"] {
            margin-right: 8px;
        }
        .back-link {
            text-align: center;
            margin-top: 20px;
        }
        .back-link a {
            color: #007bff;
            text-decoration: none;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h3>Login</h3>
            </div>
            <div class="login-body">
                <g:if test="${flash.message}">
                    <div class="alert alert-danger">
                        ${flash.message}
                    </div>
                </g:if>
                
                <form action="${createLink(controller: 'login', action: 'auth')}" method="POST">
                    <div class="form-group">
                        <label for="username">Usuário:</label>
                        <input type="text" class="form-control" id="username" name="username" required autocomplete="username">
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Senha:</label>
                        <input type="password" class="form-control" id="password" name="password" required autocomplete="current-password">
                    </div>
                    
                    <div class="checkbox-group">
                        <input type="checkbox" id="remember-me" name="remember-me">
                        <label for="remember-me">Lembrar-me</label>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Entrar</button>
                </form>
                
                <div class="back-link">
                    <g:link controller="index" action="index">← Voltar ao início</g:link>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
