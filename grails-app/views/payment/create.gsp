<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nova Cobrança</title>
</head>
<body>

<h1>Criar Cobrança</h1>

<g:if test="${flash.message}">
    <p><strong>${flash.message}</strong></p>
</g:if>

<g:set var="anoAtual" value="${Calendar.instance.get(Calendar.YEAR)}"/>

<g:form controller="payment" action="save" method="post">
    <table>
        <tr>
            <td>Pagador:</td>
            <td>
                <g:select
                    name="payer.id"
                    from="${payers}"
                    optionKey="id"
                    optionValue="${{ it.name + ' - ' + it.email }}"
                    noSelection="['':'-- Selecione um pagador --']"/>
            </td>
        </tr>
        <tr>
            <td>Valor:</td>
            <td><g:textField name="value"/></td>
        </tr>
        <tr>
            <td>Tipo:</td>
            <td>
                <g:select name="type" from="${com.asaas.mini.enums.PaymentType.values()}" />
            </td>
        </tr>
        <tr>
            <td>Data de vencimento:</td>
            <td>
                <g:datePicker name="dueDate"
                              precision="day"
                              years="${anoAtual..(anoAtual+10)}"/>
            </td>
        </tr>
    </table>

    <p><g:submitButton name="create" value="Salvar"/></p>
</g:form>

<p>
    <g:link controller="payment" action="index">Voltar</g:link>
</p>

</body>
</html>
