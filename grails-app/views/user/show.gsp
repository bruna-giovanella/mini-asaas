<g:applyLayout name="main">

    <atlas-page-header page-name="Detalhes do Usuário"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Informações do Usuário</atlas-text>

        <atlas-layout gap="2" direction="vertical">
            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Nome de Usuário:</atlas-text>
                <atlas-text size="sm">${user?.username ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Cliente:</atlas-text>
                <atlas-text size="sm">${user?.customer?.name ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>E-mail:</atlas-text>
                <atlas-text size="sm">${user?.customer?.email ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>CPF/CNPJ:</atlas-text>
                <atlas-text size="sm">${user?.customer?.cpfCnpj ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Status do Usuário:</atlas-text>
                <atlas-badge 
                    theme="${user?.deleted ? 'danger' : 'success'}"
                    text="${user?.deleted ? 'Inativo' : 'Ativo'}">
                </atlas-badge>
            </atlas-layout>
        </atlas-layout>

        <atlas-divider></atlas-divider>

        <atlas-text size="md" bold>Endereço</atlas-text>

        <atlas-layout gap="2" direction="vertical">
            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>CEP:</atlas-text>
                <atlas-text size="sm">${user?.customer?.address?.cep ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Cidade:</atlas-text>
                <atlas-text size="sm">${user?.customer?.address?.city ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Estado:</atlas-text>
                <atlas-text size="sm">${user?.customer?.address?.state ?: 'N/A'}</atlas-text>
            </atlas-layout>

            <atlas-layout gap="1" direction="horizontal">
                <atlas-text size="sm" color="neutral" bold>Complemento:</atlas-text>
                <atlas-text size="sm">${user?.customer?.address?.complement ?: 'N/A'}</atlas-text>
            </atlas-layout>
        </atlas-layout>

        <atlas-divider></atlas-divider>

        <atlas-layout gap="2" inline>

            <atlas-button
                description="Editar"
                href="${createLink(controller: 'user', action: 'edit', id: user.id)}"
                size="sm">
                Editar
            </atlas-button>

            <atlas-button
                description="Voltar"
                href="${createLink(controller: 'user', action: 'index')}"
                size="sm">
                Voltar
            </atlas-button>

            <g:if test="${!user.deleted}">
                <atlas-form id="deleteForm">
                    <g:hiddenField name="id" value="${user?.id}"/>
                    <atlas-button
                        theme="danger"
                        description="Excluir Usuário"
                        onclick="if(confirm('Tem certeza que deseja excluir este usuário?')) {
                            fetch('${createLink(controller:'user', action: 'delete')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${user.id}'
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
                        Excluir Usuário
                    </atlas-button>
                </atlas-form>
            </g:if>

            <g:if test="${user.deleted}">
                <atlas-form id="restoreForm">
                    <g:hiddenField name="id" value="${user?.id}"/>
                    <atlas-button
                        theme="success"
                        description="Restaurar Usuário"
                        onclick="if(confirm('Tem certeza que deseja restaurar este usuário?')) {
                            fetch('${createLink(controller:'user', action: 'restore')}', {
                                method: 'POST',
                                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                                body: 'id=${user.id}'
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
                        Restaurar Usuário
                    </atlas-button>
                </atlas-form>
            </g:if>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>