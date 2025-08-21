<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalhes</title>
</head>
<body>

<h1>Detalhes do Cliente</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<p><strong>Nome:</strong> ${customer.name}</p>
<p><strong>Email:</strong> ${customer.email}</p>
<p><strong>CPF/CNPJ:</strong> ${customer.cpfCnpj}</p>

<h3>Endereço</h3>
<p><strong>CEP:</strong> ${customer.address?.cep}</p>
<p><strong>Cidade:</strong> ${customer.address?.city}</p>
<p><strong>Estado:</strong> ${customer.address?.state}</p>
<p><strong>Complemento:</strong> ${customer.address?.complement}</p>

<p>
    <g:link action="edit" id="${customer.id}">Editar</g:link> |
    <g:link action="index" id="${customer.id}">Voltar</g:link>
</p>

</body>
</html>
