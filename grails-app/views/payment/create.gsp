<g:applyLayout name="main">

    <atlas-page-header page-name="Nova Cobrança"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="error">
            </atlas-alert>
        </div>
    </g:if>

<atlas-alert type="info" message=
    "Pagamentos via PIX terão vencimento de 1 dia.
    Pagamentos via Cartão ou Boleto terão vencimento de 30 dias.">
</atlas-alert>

    <atlas-panel>
        <atlas-text size="lg" bold>Criar Nova Cobrança</atlas-text>

        <g:form controller="payment" action="save" method="post">
            <atlas-grid>
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Pagador </atlas-text>
                        <g:select
                            name="payer.id"
                            from="${payers}"
                            optionKey="id"
                            optionValue="${{ it.name + ' - ' + it.email }}"
                            noSelection="['':'-- Selecione um pagador --']"
                            class="form-control"
                            required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Valor </atlas-text>
                        <input
                            name="value"
                            type="text"
                            class="form-control"
                            placeholder="0,00"
                            required="true"
                            value="${payment?.value ?: ''}">
                        </input>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Tipo </atlas-text>
                        <g:select
                            name="type"
                            from="${com.asaas.mini.enums.PaymentType.values()}"
                            class="form-control"
                            required="true"/>
                    </atlas-col>
                </atlas-row>

            <atlas-layout gap="2" inline>
                <g:submitButton name="create" value="Salvar Cobrança" class="btn btn-primary"/>
                <atlas-button
                    description="Voltar"
                    href="${createLink(controller: 'payment', action: 'index')}"
                    theme="ghost">
                    Voltar
                </atlas-button>
            </atlas-layout>
        </g:form>
    </atlas-panel>

</g:applyLayout>
