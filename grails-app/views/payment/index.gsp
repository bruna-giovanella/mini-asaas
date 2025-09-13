<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cobranças</title>
</head>
<body>

<h1>Lista de Cobranças</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<table border="1" cellpadding="8">
    <thead>
        <tr>
            <th>Cliente</th>
            <th>Status</th>
            <th>Valor</th>
            <th>Vencimento</th>
            <th>Ações</th>
        </tr>
    </thead>
    <tbody>
        <g:each in="${paymentList}" var="payment">
            <tr>
                <td>${payment?.payer?.name}</td>
                <td>${payment?.status}</td>
                <td>${payment?.value}</td>
                <td>${payment?.dueDate}</td>
                <td>
                    <g:link controller="payment" action="show" id="${payment.id}">Visualizar</g:link> |
                    <g:link controller="payment" action="edit" id="${payment.id}">Editar</g:link> |
                    <g:link controller="payment" action="confirmInCash" id="${payment.id}">Confirmar recebimento</g:link> |


                    <g:if test="${payment?.deleted}">
                        <g:form controller="payment" action="restore" method="post">
                            <g:hiddenField name="id" value="${payment.id}"/>
                            <g:submitButton name="restore" value="Restaurar"/>
                        </g:form>
                    </g:if>

                    <g:if test="${!payment?.deleted}">
                        <g:form controller="payment" action="delete" method="post">
                            <g:hiddenField name="id" value="${payment.id}"/>
                            <g:submitButton name="delete" value="Deletar"/>
                        </g:form>
                    </g:if>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>

<p>
    <g:link controller="payment" action="create">Nova cobrança</g:link> |
    <g:link class="btn" controller="customer" action="index" id="${customer?.id}">Voltar</g:link>
</p>

</body>
</html>
