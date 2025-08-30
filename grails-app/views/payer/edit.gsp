<!DOCTYPE html>
<html>
<head>
    <title>Editar Pagador</title>
</head>
<body>
<h1>Editar Pagador</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<g:form controller="payer" action="update">
    <g:hiddenField name="id" value="${payer?.id}"/>

    <label>Nome:</label>
    <g:textField name="name" value="${payer?.name}"/><br/>

    <label>Email:</label>
    <g:textField name="email" value="${payer?.email}"/><br/>

    <label>Telefone:</label>
    <g:textField name="contactNumber" value="${payer?.contactNumber}"/><br/>

    <label>CPF/CNPJ:</label>
    <g:textField name="cpfCnpj" value="${payer?.cpfCnpj}"/><br/>

    <h3>Endereço</h3>
    <label>CEP:</label>
    <g:textField name="cep" value="${payer?.address?.cep}"/><br/>

    <label>Cidade:</label>
    <g:textField name="city" value="${payer?.address?.city}"/><br/>

    <label>Estado:</label>
    <g:textField name="state" value="${payer?.address?.state}"/><br/>

    <label>Complemento:</label>
    <g:textField name="complement" value="${payer?.address?.complement}"/><br/>

    <g:submitButton name="update" value="Salvar"/>
</g:form>

<hr/>

<g:if test="${!payer.deleted}">
    <g:form controller="payer" action="delete" method="post">
        <g:hiddenField name="id" value="${payer?.id}"/>
        <g:submitButton name="delete" class="btn btn-danger" value="Deletar"/>
    </g:form>
</g:if>

<g:if test="${payer.deleted}">
    <g:form controller="payer" action="restore" method="post">
        <g:hiddenField name="id" value="${payer?.id}"/>
        <g:submitButton name="restore" class="btn btn-danger" value="Restaurar"/>
    </g:form>
</g:if>

<p><g:link action="show" id="${payer?.id}">Voltar</g:link></p>
</body>
</html>