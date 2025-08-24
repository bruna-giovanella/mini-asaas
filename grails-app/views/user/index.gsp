<!DOCTYPE html>
<html>
<head>
    <title>Usuários</title>
</head>
<body>
<h1>Lista de Usuários</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<table border="1" cellpadding="8">
    <thead>
        <tr>
            <th>Username</th>
            <th>Ativo</th>
            <th>Roles</th>
            <th>Ações</th>
        </tr>
    </thead>
    <tbody>
        <g:each in="${userList}" var="user">
            <tr>
                <td>${user.username}</td>
                <td>${user.enabled ? 'Sim' : 'Não'}</td>
                <td>${user.authorities*.authority.join(', ')}</td>
                <td>
                    <g:link action="show" id="${user.id}">Visualizar</g:link> |
                    <g:link action="edit" id="${user.id}">Editar</g:link> |

                    <g:if test="${user?.deleted}">
                        <g:form controller="user" action="restore" method="post">
                            <g:hiddenField name="id" value="${user.id}"/>
                            <g:submitButton name="restore" value="Restaurar"/>
                        </g:form>
                    </g:if>

                    <g:if test="${!user?.deleted}">
                        <g:form controller="user" action="delete" method="post">
                            <g:hiddenField name="id" value="${user.id}"/>
                            <g:submitButton name="delete" value="Deletar"/>
                        </g:form>
                    </g:if>

                </td>
            </tr>
        </g:each>
    </tbody>
</table>

<p>
    <g:link action="create">Novo Usuário</g:link>
</p>
</body>
</html>
