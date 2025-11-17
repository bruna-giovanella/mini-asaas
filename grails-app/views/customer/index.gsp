<g:applyLayout name="main">
<atlas-page-header page-name="Olá, ${customer?.name}"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-text size="lg" bold>Resumo Financeiro</atlas-text>

    <atlas-layout columns="3" gap="3">

        <atlas-panel>
            <atlas-text bold>Total Recebido</atlas-text>
            <atlas-text size="lg">R$ ${totalReceived}</atlas-text>
        </atlas-panel>

        <atlas-panel>
            <atlas-text bold>Total Atrasado</atlas-text>
            <atlas-text size="lg">R$ ${totalOverdue}</atlas-text>
        </atlas-panel>

        <atlas-panel>
            <atlas-text bold>Pagamentos Recebidos</atlas-text>
            <atlas-text size="lg">${totalPaymentsReceived}</atlas-text>
        </atlas-panel>

        <atlas-panel>
            <atlas-text bold>Pagamentos Atrasados</atlas-text>
            <atlas-text size="lg">${totalPaymentsOverdue}</atlas-text>
        </atlas-panel>

        <atlas-panel>
            <atlas-text bold>Pagadores que já pagaram tudo</atlas-text>
            <atlas-text size="lg">${totalPayersReceived}</atlas-text>
        </atlas-panel>

        <atlas-panel>
            <atlas-text bold>Pagadores com débitos atrasados</atlas-text>
            <atlas-text size="lg">${totalPayersOverdue}</atlas-text>
        </atlas-panel>

    </atlas-layout>

    <atlas-panel>

        <g:if test="${customer?.deleted}">
            <atlas-badge
                text="Conta deletada"
                theme="danger">
            </atlas-badge>
        </g:if>

        <atlas-text size="lg" bold> Dashboard </atlas-text>

        <atlas-text><strong>Email:</strong> ${customer?.email}</atlas-text>
        <atlas-text><strong>CPF/CNPJ:</strong> ${customer?.cpfCnpj}</atlas-text>

        <atlas-divider> </atlas-divider>

        <atlas-layout gap="2" inline>

            <atlas-button
                description="Visualizar"
                href="${createLink(controller: 'customer', action: 'show', id: customer.id)}">
                Visualizar
            </atlas-button>

            <atlas-button
                description="Editar"
                href="${createLink(controller: 'customer', action: 'edit', id: customer.id)}">
                Editar
            </atlas-button>

            <g:if test="${customer}">
                <atlas-form id="form${customer?.id}">
                    <g:hiddenField name="id" value="${customer?.id}"/>
                    <atlas-button
                        theme="${customer?.deleted ? 'success' : 'danger'}"
                        description="${customer?.deleted ? 'Restaurar Conta' : 'Excluir Conta'}"
                        onclick="fetch('${createLink(controller:'customer', action: customer?.deleted ? 'restore' : 'delete')}', {
                                   method: 'POST',
                                   headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                   body: 'id=${customer?.id}'
                               }).then(()=>location.reload())">
                        ${customer?.deleted ? 'Restaurar Conta' : 'Excluir Conta'}
                    </atlas-button>
                </atlas-form>
            </g:if>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>
