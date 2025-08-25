<!DOCTYPE html>
<html>
<head>
    <title>Novo Usuário</title>
</head>
<body>
<h1>Criar Usuário</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<g:form controller="user" action="save">
    <table>
        <tr>
            <td>Username:</td>
            <td><g:textField name="username"/></td>
        </tr>
        <tr>
            <td>Senha:</td>
            <td><g:passwordField name="password"/></td>
        </tr>
        <tr>
            <td>Role:</td>
            <td>
                <g:select name="role"
                          from="${roles}"
                          optionKey="authority"
                          optionValue="authority"
                          noSelection="['':'-- Selecione uma role --']"/>
            </td>
        </tr>
    </table>
    <p><g:submitButton name="create" value="Salvar"/></p>
</g:form>

<p><g:link action="index">Voltar</g:link></p>
</body>
</html>
