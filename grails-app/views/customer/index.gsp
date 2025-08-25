<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Minha Conta</title>
</head>
<body>

<div class="sidebar">

    <h1>Bem-vindo, ${customer?.name}</h1>
    <p><strong>Email:</strong> ${customer?.email}</p>
    <p><strong>CPF/CNPJ:</strong> ${customer?.cpfCnpj}</p>

    <h3>Menu</h3>
    <g:link class="btn" controller="customer" action="show" id="${customer.id}">Visualizar</g:link>
    <g:link class="btn" controller="customer" action="edit" id="${customer.id}">Editar</g:link>
    <g:link class="btn" controller="payer" action="index">Pagadores</g:link>

</div>

<div class="content">
    <g:if test="${flash.message}">
        <p><strong>${flash.message}</strong></p>
    </g:if>

    <g:if test="${customer?.deleted}">
        <div class="alert">⚠️ Sua conta está deletada. Restaure para voltar a usá-la.</div>
    </g:if>

</div>

</body>
</html>
