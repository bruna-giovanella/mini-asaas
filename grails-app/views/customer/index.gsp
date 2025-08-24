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
    <g:link class="btn" controller="payment" action="index">Cobranças</g:link>
    <g:link class="btn" controller="user" action="index">Usuários</g:link>

    <g:if test="${customer?.deleted}">
        <g:form controller="customer" action="restore" method="post">
            <g:hiddenField name="id" value="${customer?.id}"/>
            <g:submitButton name="restore" class="btn btn-success" value="Restaurar Conta"/>
        </g:form>
    </g:if>

    <g:if test="${!customer?.deleted}">
        <g:form controller="customer" action="delete" method="post">
            <g:hiddenField name="id" value="${customer?.id}"/>
            <g:submitButton name="delete" class="btn btn-danger" value="Excluir Conta"/>
        </g:form>
    </g:if>
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
