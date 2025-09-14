<g:applyLayout name="main">

    <atlas-page-header page-name="Novo Pagador"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert type="danger">
                ${flash.message}
            </atlas-alert>
        </div>
    </g:if>


    <atlas-panel>
        <atlas-text size="lg" bold>Criar Pagador</atlas-text>
        
        <g:form controller="payer" action="save" method="post">
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
                        <g:textField name="contactNumber" value="${payer?.contactNumber}" class="form-control" placeholder="(11) 99999-9999" required="true"/>
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
                        <g:textField name="cep" value="${payer?.cep}" class="form-control" placeholder="00000-000" required="true"/>
                    </atlas-col>
                </atlas-row>
                
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Cidade *</atlas-text>
                        <g:textField name="city" value="${payer?.city}" class="form-control" placeholder="Digite a cidade" required="true"/>
                    </atlas-col>
                </atlas-row>
                
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Estado *</atlas-text>
                        <g:textField name="state" value="${payer?.state}" class="form-control" placeholder="Digite o estado" required="true"/>
                    </atlas-col>
                </atlas-row>
                
                <atlas-row>
                    <atlas-col>
                        <atlas-text size="sm" bold>Complemento</atlas-text>
                        <g:textField name="complement" value="${payer?.complement}" class="form-control" placeholder="Apartamento, casa, etc."/>
                    </atlas-col>
                </atlas-row>
            </atlas-grid>

            <atlas-divider></atlas-divider>

            <atlas-layout gap="2" inline>
                <g:submitButton name="save" value="Salvar Pagador" class="btn btn-primary"/>
                
            <atlas-button
                description="Voltar"
                href="${createLink(controller: 'payer', action: 'index')}">
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