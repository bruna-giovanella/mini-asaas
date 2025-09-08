<g:applyLayout name="main">
<atlas-page-header page-name="Dashboard"></atlas-page-header>

    <atlas-panel>

        <g:if test="${customer?.deleted}">
            <atlas-badge
                text="Conta deletada. Restaure para utilizar"
                theme="danger">
            </atlas-badge>
        </g:if>

        <h1>Olá, ${customer?.name}</h1>
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

        <g:if test="${flash.message}">
            <div class="floating-alert">
                <atlas-alert
                    message="${flash.message}"
                    type="success">
                </atlas-alert>
            </div>
        </g:if>

    </atlas-panel>
</g:applyLayout>
