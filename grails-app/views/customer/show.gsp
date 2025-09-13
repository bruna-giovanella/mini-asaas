<g:applyLayout name="main">
<atlas-page-header page-name="Detalhes da Conta"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>

        <g:if test="${flash.message}">
            <p>${flash.message}</p>
        </g:if>

        <atlas-text size="lg" bold> Dados Pessoais </atlas-text>

        <atlas-text><strong>Nome:</strong> ${customer.name}</atlas-text>
        <atlas-text><strong>Email:</strong> ${customer.email}</atlas-text>
        <atlas-text><strong>CPF/CNPJ:</strong> ${customer.cpfCnpj}</atlas-text>

        <atlas-divider> </atlas-divider>

        <atlas-text size="lg" bold> Endereço </atlas-text>
        <atlas-text><strong>CEP:</strong> ${customer.address?.cep}</atlas-text>
        <atlas-text><strong>Cidade:</strong> ${customer.address?.city}</atlas-text>
        <atlas-text><strong>Estado:</strong> ${customer.address?.state}</atlas-text>
        <atlas-text><strong>Complemento:</strong> ${customer.address?.complement}</atlas-text>

        <atlas-divider> </atlas-divider>

        <atlas-layout gap="2" inline>

            <atlas-button
                description="Editar"
                href="${createLink(controller: 'customer', action: 'edit', id: customer?.id)}">
                Visualizar
            </atlas-button>

            <atlas-button
                description="Voltar"
                href="${createLink(controller: 'customer', action: 'index', id: customer?.id)}">
                Editar
            </atlas-button>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>