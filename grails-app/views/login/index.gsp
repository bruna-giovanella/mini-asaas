<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>

<h1>Login</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<g:form controller="login" action="auth" method="post">
    <p>
        <label for="username">Usuário:</label><br>
        <g:textField name="username" id="username" required="true"/>
    </p>
    
    <p>
        <label for="password">Senha:</label><br>
        <g:passwordField name="password" id="password" required="true"/>
    </p>
    
    <p>
        <g:submitButton name="login" value="Entrar"/>
    </p>
</g:form>

</body>
</html>
