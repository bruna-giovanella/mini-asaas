<g:applyLayout name="main">

    <atlas-page-header page-name="Detalhes do Cliente"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Informações do Cliente</atlas-text>

        <atlas-layout gap="2" direction="vertical">
            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Nome:</atlas-text>
                <atlas-text size="sm">${payer?.name ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>E-mail:</atlas-text>
                <atlas-text size="sm">${payer?.email ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Telefone:</atlas-text>
                <atlas-text size="sm">${payer?.contactNumber ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>CPF/CNPJ:</atlas-text>
                <atlas-text size="sm">${payer?.cpfCnpj ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Status:</atlas-text>
                <atlas-badge 
                    theme="${payer?.deleted ? 'danger' : 'success'}"
                    text="${payer?.deleted ? 'Inativo' : 'Ativo'}">
                </atlas-badge>
            </atlas-layout>
        </atlas-layout>

        <atlas-divider></atlas-divider>

        <atlas-text size="md" bold>Endereço</atlas-text>

        <atlas-layout gap="2" direction="vertical">
            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>CEP:</atlas-text>
                <atlas-text size="sm">${payer?.address?.cep ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Cidade:</atlas-text>
                <atlas-text size="sm">${payer?.address?.city ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Estado:</atlas-text>
                <atlas-text size="sm">${payer?.address?.state ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Complemento:</atlas-text>
                <atlas-text size="sm">${payer?.address?.complement ?: 'N/A'}</atlas-text>
            </atlas-layout>
        </atlas-layout>

        <atlas-divider></atlas-divider>

        <atlas-layout gap="2" inline>

            <atlas-button
                description="Editar"
                href="${createLink(controller: 'payer', action: 'edit', id: payer.id)}"
                size="sm">
                Editar
            </atlas-button>

            <atlas-button
                description="Voltar"
                href="${createLink(controller: 'payer', action: 'index')}"
                size="sm">
                Voltar
            </atlas-button>

            <g:if test="${!payer.deleted}">
                <atlas-form id="deleteForm">
                    <g:hiddenField name="id" value="${payer?.id}"/>
                    <atlas-button
                        theme="danger"
                        description="Excluir Cliente"
                        onclick="if(confirm('Tem certeza que deseja excluir este cliente?')) {
                            fetch('${createLink(controller:'payer', action: 'delete')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${payer.id}'
                            }).then(response => {
                                if (response.ok) {
                                    location.reload();
                                } else {
                                    return response.text().then(text => {
                                        alert('Erro: ' + text);
                                    });
                                }
                            }).catch(error => {
                                alert('Erro inesperado: ' + error.message);
                            });
                        }"
                        size="sm">
                        Excluir Cliente
                    </atlas-button>
                </atlas-form>
            </g:if>

            <g:if test="${payer.deleted}">
                <atlas-form id="restoreForm">
                    <g:hiddenField name="id" value="${payer?.id}"/>
                    <atlas-button
                        theme="success"
                        description="Restaurar Cliente"
                        onclick="if(confirm('Tem certeza que deseja restaurar este cliente?')) {
                            fetch('${createLink(controller:'payer', action: 'restore')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${payer.id}'
                            }).then(response => {
                                if (response.ok) {
                                    location.reload();
                                } else {
                                    return response.text().then(text => {
                                        alert('Erro: ' + text);
                                    });
                                }
                            }).catch(error => {
                                alert('Erro inesperado: ' + error.message);
                            });
                        }"
                        size="sm">
                        Restaurar Cliente
                    </atlas-button>
                </atlas-form>
            </g:if>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>