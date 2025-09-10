<g:applyLayout name="main">

    <atlas-page-header page-name="Clientes"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="${flash.message?.contains('erro') || flash.message?.contains('Erro') || flash.message?.contains('não é possível') ? 'danger' : 'success'}">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Lista de Clientes</atlas-text>

        <atlas-easy-table

            total-records="${totalCount}"
            columns='[{"name":"name","label":"Nome","size":"lg","sortable":false},{"name":"email","label":"E-mail","size":"lg","sortable":false},{"name":"status","label":"Status","size":"sm","sortable":false},{"name":"actions","label":"Ações","size":"md"}]'>
            
            <g:if test="${payerList && !payerList.isEmpty()}">
                <g:each in="${payerList}" var="payer">

                    <atlas-table-row selection-value="${payer.id}">
                        <atlas-table-col>${payer?.name ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>${payer?.email ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>
                            <atlas-badge 
                                theme="${payer?.deleted ? 'danger' : 'success'}"
                                text="${payer?.deleted ? 'Inativo' : 'Ativo'}">
                            </atlas-badge>
                        </atlas-table-col>
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

                            </atlas-layout>
                        </atlas-table-col>
                    </atlas-table-row>
                </g:each>
            </g:if>

            <g:else>
                <atlas-table-row>
                    <atlas-table-col colspan="4">
                        <atlas-text size="sm" color="neutral">Nenhum pagador encontrado</atlas-text>
                    </atlas-table-col>
                </atlas-table-row>
            </g:else>

        </atlas-easy-table>

        <atlas-layout gap="1" inline>

            <g:if test="${totalCount > 0}">

                <atlas-layout gap="2" inline align="center">
                    <g:if test="${offset > 0}">
                        <atlas-button
                            size="md"
                            description="Página Anterior"
                            href="${createLink(controller: 'payer', action: 'index', params: [max: max, offset: Math.max(0, offset - max), sort: sort, order: order])}"
                            size="sm"
                            ‹ Anterior
                        </atlas-button>
                    </g:if>

                    <g:if test="${offset + max < totalCount}">
                        <atlas-button
                            size="md"
                            description="Próxima Página"
                            href="${createLink(controller: 'payer', action: 'index', params: [max: max, offset: offset + max, sort: sort, order: order])}"
                            size="sm"
                            Próxima ›
                        </atlas-button>
                    </g:if>
                </atlas-layout>
            </g:if>


            <atlas-button
                size="md"
                description="Novo Cliente"
                href="${createLink(controller: 'payer', action: 'create')}"
                Novo Cliente
            </atlas-button>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>
