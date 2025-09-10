<g:applyLayout name="main">

    <atlas-page-header page-name="Clientes"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="success">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Lista de Clientes</atlas-text>

        <atlas-easy-table

            total-records="${totalCount}"
            columns='[{"name":"name","label":"Nome","size":"lg","sortable":true},{"name":"email","label":"E-mail","size":"lg","sortable":true},{"name":"actions","label":"Ações","size":"md"}]'>
            
            <g:if test="${payerList && !payerList.isEmpty()}">
                <g:each in="${payerList}" var="payer">

                    <atlas-table-row selection-value="${payer.id}">
                        <atlas-table-col>${payer?.name ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>${payer?.email ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>

                            <atlas-layout gap="1" inline>

                                <atlas-button
                                    description="Visualizar"
                                    href="${createLink(controller: 'payer', action: 'show', id: payer.id)}"
                                    size="sm">
                                    Visualizar
                                </atlas-button>
                                
                                <atlas-button
                                    description="Editar"
                                    href="${createLink(controller: 'payer', action: 'edit', id: payer.id)}"
                                    size="sm">
                                    Editar
                                </atlas-button>

                                <g:if test="${payer?.deleted}">
                                    <atlas-form>
                                        <g:hiddenField name="id" value="${payer.id}"/>
                                        <atlas-button
                                            submit
                                            theme="success"
                                            size="sm"
                                            description="Restaurar"
                                            onclick="this.form.action='${createLink(controller: 'payer', action: 'restore')}'">
                                            Restaurar
                                        </atlas-button>
                                    </atlas-form>
                                </g:if>

                                <g:if test="${!payer?.deleted}">
                                    <atlas-form>
                                        <g:hiddenField name="id" value="${payer.id}"/>
                                        <atlas-button
                                            submit
                                            theme="danger"
                                            size="sm"
                                            description="Deletar"
                                            onclick="if(confirm('Tem certeza que deseja excluir este pagador?')) { this.form.action='${createLink(controller: 'payer', action: 'delete')}'; return true; } else { return false; }">
                                            Deletar
                                        </atlas-button>
                                    </atlas-form>
                                </g:if>

                            </atlas-layout>
                        </atlas-table-col>
                    </atlas-table-row>
                </g:each>
            </g:if>

            <g:else>
                <atlas-table-row>
                    <atlas-table-col colspan="3">
                        <atlas-text size="sm" color="neutral">Nenhum pagador encontrado</atlas-text>
                    </atlas-table-col>
                </atlas-table-row>
            </g:else>

        </atlas-easy-table>

        <atlas-divider></atlas-divider>

        <atlas-button
            description="Novo Cliente"
            href="${createLink(controller: 'payer', action: 'create')}"
            Novo Cliente
        </atlas-button>

    </atlas-panel>
</g:applyLayout>
