<g:applyLayout name="main">

    <atlas-page-header page-name="Editar Cliente"></atlas-page-header>

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
                ${flash.message}
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="md" bold>Informações Pessoais</atlas-text>

        <g:form controller="payer" action="update" method="post">
            <g:hiddenField name="id" value="${payer?.id}"/>

            <atlas-grid>
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Nome *</atlas-text>
                        <g:textField name="name" value="${payer?.name}" class="form-control" placeholder="Digite o nome completo" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Email *</atlas-text>
                        <g:textField name="email" value="${payer?.email}" class="form-control" placeholder="exemplo@email.com" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Telefone *</atlas-text>
                        <g:textField name="contactNumber" value="${payer?.contactNumber}" class="form-control" placeholder="(00) 00000-0000" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>CPF/CNPJ *</atlas-text>
                        <g:textField name="cpfCnpj" value="${payer?.cpfCnpj}" class="form-control" placeholder="000.000.000-00 ou 00.000.000/0000-00" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-divider></atlas-divider>

                <atlas-text size="md" bold>Endereço</atlas-text>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>CEP *</atlas-text>
                        <g:textField name="cep" value="${payer?.address?.cep}" class="form-control" placeholder="00000-000" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Cidade *</atlas-text>
                        <g:textField name="city" value="${payer?.address?.city}" class="form-control" placeholder="Digite a cidade" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Estado *</atlas-text>
                        <g:textField name="state" value="${payer?.address?.state}" class="form-control" placeholder="Digite o estado" required="true"/>
                    </atlas-col>
                </atlas-row>

                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Complemento</atlas-text>
                        <g:textField name="complement" value="${payer?.address?.complement}" class="form-control" placeholder="Apartamento, casa, etc."/>
                    </atlas-col>
                </atlas-row>
            </atlas-grid>

            <atlas-divider></atlas-divider>

            <atlas-layout gap="2" inline>
                <g:submitButton name="update" value="Salvar Alterações" class="btn btn-primary"/>

                <atlas-button
                    description="Voltar"
                    href="${createLink(controller: 'payer', action: 'index', id: payer?.id)}">
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
