<!DOCTYPE html>
<html>
<head>
    <title>Novo Pagador</title>
</head>
<body>
<h1>Criar Pagador</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<g:form controller="payer" action="save">
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
    <g:textField name="cep" value="${payer?.cep}"/><br/>

    <label>Cidade:</label>
    <g:textField name="city" value="${payer?.city}"/><br/>

    <label>Estado:</label>
    <g:textField name="state" value="${payer?.state}"/><br/>

    <label>Complemento:</label>
    <g:textField name="complement" value="${payer?.complement}"/><br/>

    <g:actionSubmit action="save" value="Salvar"/>
</g:form>

<p><g:link action="index">Voltar</g:link></p>
</body>
</html>