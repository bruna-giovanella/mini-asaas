    <g:applyLayout name="main">

    <atlas-page-header page-name="Detalhes da Cobrança"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="success">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Informações da Cobrança</atlas-text>

        <atlas-layout gap="2" direction="vertical">
            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Cliente:</atlas-text>
                <atlas-text size="sm">${payment?.payer?.name ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Valor:</atlas-text>
                <atlas-text size="sm">R$ ${payment?.value ?: '0,00'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Tipo:</atlas-text>
                <atlas-text size="sm">${payment?.type ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Status:</atlas-text>
                <atlas-badge 
                    text="${payment?.status}" 
                    theme="${payment?.status == 'RECEBIDA' ? 'success' : payment?.status == 'VENCIDA' ? 'danger' : 'warning'}">
                </atlas-badge>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Vencimento:</atlas-text>
                <atlas-text size="sm">${payment?.dueDate?.format('dd/MM/yyyy') ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral">Status da Cobrança:</atlas-text>
                <atlas-badge 
                    theme="${payment?.deleted ? 'danger' : 'success'}"
                    text="${payment?.deleted ? 'Inativo' : 'Ativo'}">
                </atlas-badge>
            </atlas-layout>

            <g:if test="${payment?.confirmedInCash}">
                <atlas-layout gap="1" direction="horizontal">
                    <atlas-text size="sm" color="neutral">Confirmado em Dinheiro:</atlas-text>
                    <atlas-badge theme="success" text="Sim"></atlas-badge>
                </atlas-layout>
            </g:if>
        </atlas-layout>

        <atlas-divider></atlas-divider>

        <atlas-layout gap="1" inline>
            <atlas-button
                description="Editar"
                href="${createLink(controller: 'payment', action: 'edit', id: payment.id)}"
                size="sm">
                Editar
            </atlas-button>

            <atlas-button
                description="Voltar"
                href="${createLink(controller: 'payment', action: 'index')}"
                size="sm">
                Voltar
            </atlas-button>

            <g:if test="${payment?.status == com.asaas.mini.enums.PaymentStatus.AGUARDANDO_PAGAMENTO && !payment?.deleted}">
                <atlas-button
                    description="Confirmar Recebimento"
                    href="${createLink(controller: 'payment', action: 'confirmInCash', id: payment.id)}"
                    theme="success"
                    size="sm">
                    Confirmar Recebimento
                </atlas-button>
            </g:if>

            <g:if test="${!payment.deleted}">
                <atlas-form id="deleteForm">
                    <g:hiddenField name="id" value="${payment?.id}"/>
                    <atlas-button
                        theme="danger"
                        description="Excluir Cobrança"
                        onclick="if(confirm('Tem certeza que deseja excluir esta cobrança?')) {
                            fetch('${createLink(controller:'payment', action: 'delete')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${payment.id}'
                            }).then(()=>location.reload())
                        }"
                        size="sm">
                        Excluir Cobrança
                    </atlas-button>
                </atlas-form>
            </g:if>

            <g:if test="${payment.deleted}">
                <atlas-form id="restoreForm">
                    <g:hiddenField name="id" value="${payment?.id}"/>
                    <atlas-button
                        theme="success"
                        description="Restaurar Cobrança"
                        onclick="if(confirm('Tem certeza que deseja restaurar esta cobrança?')) {
                            fetch('${createLink(controller:'payment', action: 'restore')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${payment.id}'
                            }).then(()=>location.reload())
                        }"
                        size="sm">
                        Restaurar Cobrança
                    </atlas-button>
                </atlas-form>
            </g:if>

            </atlas-layout>

    </atlas-panel>
</g:applyLayout>