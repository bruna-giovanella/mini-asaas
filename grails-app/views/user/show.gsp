<!DOCTYPE html>
<html>
<head>
    <title>Detalhes do Usuário</title>
</head>
<body>
<h1>Detalhes do Usuário</h1>

<p><strong>Username:</strong> ${user.username}</p>
<p><strong>Ativo:</strong> ${user.enabled ? 'Sim' : 'Não'}</p>
<p><strong>Roles:</strong> ${user.authorities*.authority.join(', ')}</p>

<p>
    <g:link action="edit" id="${user.id}">Editar</g:link> |
    <g:link action="index">Voltar</g:link>
</p>
</body>
</html>
