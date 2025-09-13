<g:applyLayout name="main">

    <atlas-page-header page-name="Editar Usuário"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
                ${flash.message}
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="md" bold>Informações do Usuário</atlas-text>

        <g:form controller="user" action="update" method="post">
            <g:hiddenField name="id" value="${user?.id}"/>

            <atlas-grid>
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Nome de Usuário *</atlas-text>
                        <g:textField name="username" value="${user?.username}" class="form-control" placeholder="Digite o nome de usuário" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Senha *</atlas-text>
                        <g:passwordField name="password" class="form-control" placeholder="Digite a nova senha" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Cliente:</atlas-text>
                        <atlas-text size="sm">${user?.customer?.name ?: 'N/A'}</atlas-text>
                        <g:hiddenField name="customer.id" value="${user?.customer?.id}"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Role *</atlas-text>
                        <g:select
                            name="role"
                            from="${roles}"
                            optionKey="authority"
                            optionValue="authority"
                            value="${user?.authorities?.first()?.authority}"
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
                    href="${createLink(controller: 'user', action: 'index')}">
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