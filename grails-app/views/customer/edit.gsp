<!DOCTYPE html>
<html>
<head>
    <title>Editar Conta</title>
</head>
<body>
    <h1>Editar Conta</h1>

    <g:if test="${flash.message}">
        <div>${flash.message}</div>
    </g:if>

    <g:form controller="customer" action="update">
        <g:hiddenField name="id" value="${customer?.id}"/>

        <label>Nome:</label>
        <g:textField name="name" value="${customer?.name}"/><br/>

        <label>Email:</label>
        <g:textField name="email" value="${customer?.email}"/><br/>

        <label>CPF/CNPJ:</label>
        <g:textField name="cpfCnpj" value="${customer?.cpfCnpj}"/><br/>

        <h3>Endereço</h3>
        <label>CEP:</label>
        <g:textField name="cep" value="${customer?.address?.cep}"/><br/>

        <label>Cidade:</label>
        <g:textField name="city" value="${customer?.address?.city}"/><br/>

        <label>Estado:</label>
        <g:textField name="state" value="${customer?.address?.state}"/><br/>

        <label>Complemento:</label>
        <g:textField name="complement" value="${customer?.address?.complement}"/><br/>

        <g:submitButton name="update" value="Salvar Alterações"/>
    </g:form>

    <hr/>

    <g:if test="${!customer.deleted}">
        <g:form controller="customer" action="delete" method="post">
            <g:hiddenField name="id" value="${customer?.id}"/>
            <g:submitButton name="delete" value="Excluir Conta"/>
        </g:form>
    </g:if>

    <g:if test="${customer.deleted}">
        <g:form controller="customer" action="restore" method="post">
            <g:hiddenField name="id" value="${customer?.id}"/>
            <g:submitButton name="restore" value="Restaurar Conta"/>
        </g:form>
    </g:if>

    <br/>
    <g:link action="index" id="${customer?.id}">Voltar</g:link>
</body>
</html>
