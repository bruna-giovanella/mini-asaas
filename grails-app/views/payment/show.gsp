<!DOCTYPE html>
<html>
<head>
    <title>Detalhes da cobrança</title>
</head>
<body>
<h1>Detalhes da Cobrança</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<p><strong>Cliente:</strong> ${payment?.payer?.name}</p>
<p><strong>Valor:</strong> ${payment?.value}</p>
<p><strong>Tipo:</strong> ${payment?.type}</p>
<p><strong>Status</strong> ${payment?.status}</p>
<p><strong>Vencimento</strong> ${payment?.dueDate}</p>

<p>
    <g:link action="edit" id="${payment.id}">Editar</g:link> |
    <g:link action="index">Voltar</g:link>
</p>
</body>
</html>