<!DOCTYPE html>
<html>
<head>
    <title>Detalhes do Pagador</title>
</head>
<body>
<h1>Detalhes do Pagador</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<p><strong>Nome:</strong> ${payer.name}</p>
<p><strong>Email:</strong> ${payer.email}</p>
<p><strong>Telefone:</strong> ${payer.contactNumber}</p>
<p><strong>CPF/CNPJ:</strong> ${payer.cpfCnpj}</p>

<h3>Endereço</h3>
<p><strong>CEP:</strong> ${payer.address?.cep}</p>
<p><strong>Cidade:</strong> ${payer.address?.city}</p>
<p><strong>Estado:</strong> ${payer.address?.state}</p>
<p><strong>Complemento:</strong> ${payer.address?.complement}</p>

<p>
    <g:link action="edit" id="${payer.id}">Editar</g:link> |
    <g:link action="index">Voltar</g:link>
</p>
</body>
</html>