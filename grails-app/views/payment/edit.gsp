<!DOCTYPE html>
<html>
<head>
    <title>Editar Cobrança</title>
</head>
<body>
<h1>Editar Cobrança</h1>

<g:if test="${flash.message}">
    <p>${flash.message}</p>
</g:if>

<g:set var="anoAtual" value="${Calendar.instance.get(Calendar.YEAR)}"/>

<g:form controller="payment" action="update">
    <g:hiddenField name="id" value="${payment?.id}"/>

    <table>
        <tr>
            <td>Pagador:</td>
        <td>
            <g:select
                name="payer.id"
                from="${payers}"
                optionKey="id"
                optionValue="${{ it.name + ' - ' + it.email }}"
                value="${payment?.payer?.id}"
                noSelection="['':'-- Selecione um pagador --']"/>
        </td>

        </tr>
        <tr>
            <td>Valor:</td>
            <td><g:textField name="value" value="${payment?.value}"/></td>
        </tr>
        <tr>
            <td>Tipo:</td>
            <td>
                <g:select name="type"
                          from="${com.asaas.mini.enums.PaymentType.values()}"
                          value="${payment?.type}"/>
            </td>
        </tr>
        <tr>
            <td>Data de vencimento:</td>
            <td>
                <g:datePicker name="dueDate"
                              precision="day"
                              years="${anoAtual..(anoAtual+10)}"
                              value="${payment?.dueDate}"/>
            </td>
        </tr>
    </table>

    <g:submitButton name="update" value="Atualizar"/>
</g:form>

<hr/>

<g:if test="${!payment.deleted}">
    <g:form controller="payment" action="delete" method="post">
        <g:hiddenField name="id" value="${payment?.id}"/>
        <g:submitButton name="delete" class="btn btn-danger" value="Deletar"/>
    </g:form>
</g:if>

<g:if test="${payment.deleted}">
    <g:form controller="payment" action="restore" method="post">
        <g:hiddenField name="id" value="${payment?.id}"/>
        <g:submitButton name="restore" class="btn btn-warning" value="Restaurar"/>
    </g:form>
</g:if>

<p><g:link action="index">Voltar</g:link></p>
</body>
</html>
