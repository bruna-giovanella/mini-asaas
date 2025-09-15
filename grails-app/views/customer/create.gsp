<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Criar Conta</title>
</head>
<body>

<h1>Criar Conta</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<g:form controller="customer" action="save">
    <legend>Dados da Conta</legend>
    <label>Nome:</label>
    <g:textField name="name" value="${customer?.name}"/><br/>

    <label>Email:</label>
    <g:textField name="email" value="${customer?.email}"/><br/>

    <label>CPF/CNPJ:</label>
    <g:textField name="cpfCnpj" value="${customer?.cpfCnpj}"/><br/>

    <legend>Endereço</legend>
    <label>CEP:</label>
    <g:textField name="cep" value="${customer?.cep}"/><br/>
    <label>Cidade:</label>
    <g:textField name="city" value="${customer?.city}"/><br/>
    <label>Estado:</label>
    <g:textField name="state" value="${customer?.state}"/><br/>
    <label>Complemento:</label>
    <g:textField name="complement" value="${customer?.complement}"/><br/>

    <legend>Usuário Administrador</legend>
    <label>Email:</label>
    <g:textField name="adminEmail" value="${customer?.adminEmail}"/><br/>
    <label>Senha:</label>
    <g:passwordField name="adminPassword"/><br/>

<g:submitButton name="create" value="Salvar"/>
</g:form>

<p><g:link action="index">Voltar</g:link></p>

</body>
</html>
