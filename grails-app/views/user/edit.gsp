<!DOCTYPE html>
<html>
<head>
    <title>Editar Usuário</title>
</head>
<body>
<h1>Editar Usuário</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<g:form controller="user" action="update">
    <g:hiddenField name="id" value="${user.id}"/>

    <table>
        <tr>
            <td>Username:</td>
            <td><g:textField name="username" value="${user.username}"/></td>
        </tr>
        <tr>
            <td>Senha:</td>
            <td><g:passwordField name="password" value=""/></td>
        </tr>
        <tr>
            <td>Role:</td>
            <td>
                <g:select name="role"
                          from="${roles}"
                          optionKey="authority"
                          optionValue="authority"
                          value="${user.authorities ? user.authorities.first().authority : ''}"/>
            </td>
        </tr>
    </table>
    <p><g:submitButton name="update" value="Salvar"/></p>
</g:form>

<p><g:link action="index" id="${user.id}">Voltar</g:link></p>
</body>
</html>
