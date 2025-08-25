<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
</head>
<body>

<h1>Lista de Clientes</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<table border="1" cellpadding="8">
    <thead>
        <tr>
            <th>Nome</th>
            <th>Email</th>
            <th>Ações</th>
        </tr>
    </thead>
    <tbody>
        <g:each in="${payerList}" var="payer">
            <tr>
                <td>${payer?.name}</td>
                <td>${payer?.email}</td>
                <td>
                    <g:link controller="payer" action="show" id="${payer.id}">Visualizar</g:link> |
                    <g:link controller="payer" action="edit" id="${payer.id}">Editar</g:link> |

                    <g:if test="${payer?.deleted}">
                        <g:form controller="payer" action="restore" method="post">
                            <g:hiddenField name="id" value="${payer.id}"/>
                            <g:submitButton name="restore" value="Restaurar"/>
                        </g:form>
                    </g:if>

                    <g:if test="${!payer?.deleted}">
                        <g:form controller="payer" action="delete" method="post">
                            <g:hiddenField name="id" value="${payer.id}"/>
                            <g:submitButton name="delete" value="Deletar"/>
                        </g:form>
                    </g:if>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>

<p>
    <g:link controller="payer" action="create">Novo Cliente</g:link> |
    <g:link class="btn" controller="customer" action="index" id="${customer.id}">Voltar</g:link>
</p>

</body>
</html>
