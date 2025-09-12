<g:applyLayout name="main">

    <atlas-page-header page-name="Editar Cobrança"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
                ${flash.message}
            </atlas-alert>
        </div>
    </g:if>

    <atlas-alert type="info" message=
        "Pagamentos via PIX terão vencimento de 1 dia.
        Pagamentos via Cartão ou Boleto terão vencimento de 30 dias.">
    </atlas-alert>

    <atlas-panel>
        <atlas-text size="md" bold>Informações da Cobrança</atlas-text>

        <g:form controller="payment" action="update" method="post">
            <g:hiddenField name="id" value="${payment?.id}"/>

            <atlas-grid>
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Pagador *</atlas-text>
                        <g:select
                            name="payer.id"
                            from="${payers}"
                            optionKey="id"
                            optionValue="${{ it.name + ' - ' + it.email }}"
                            noSelection="['':'-- Selecione um pagador --']"
                            value="${payment?.payer?.id}"
                            class="form-control"
                            required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Valor *</atlas-text>
                        <g:textField
                            name="value"
                            value="${payment?.value ?: ''}"
                            class="form-control"
                            placeholder="0,00"
                            required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Tipo *</atlas-text>
                        <g:select
                            name="type"
                            from="${com.asaas.mini.enums.PaymentType.values()}"
                            value="${payment?.type}"
                            class="form-control"
                            required="true"/>
                    </atlas-col>
                </atlas-row>
            </atlas-grid>

            <atlas-divider></atlas-divider>

            <atlas-layout gap="2" inline>
                <g:submitButton name="update" value="Salvar Alterações" class="btn btn-primary"/>
                <atlas-button
                    description="Voltar"
                    href="${createLink(controller: 'payment', action: 'index', id: payment?.id)}">
                    Voltar
                </atlas-button>
            </atlas-layout>

        </g:form>
    </atlas-panel>

</g:applyLayout>

<style>
.form-control {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    margin-bottom: 8px;
}

.form-control:focus {
    border-color: #007bff;
    outline: none;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}
</style>
