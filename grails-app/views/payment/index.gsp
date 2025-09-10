<g:applyLayout name="main">

    <atlas-page-header page-name="Cobranças"></atlas-page-header>
    
    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert
                message="${flash.message}"
                type="success">
            </atlas-alert>
        </div>
    </g:if>

    <atlas-panel>
        <atlas-text size="lg" bold>Lista de Cobranças</atlas-text>

        <atlas-easy-table
            total-records="${totalCount}"
            columns='[{"name":"payer","label":"Cliente","size":"lg","sortable":true},{"name":"status","label":"Status","size":"md","sortable":true},{"name":"value","label":"Valor","size":"md","sortable":true},{"name":"dueDate","label":"Vencimento","size":"md","sortable":true},{"name":"active","label":"Ativo","size":"sm","sortable":true},{"name":"actions","label":"Ações","size":"lg"}]'>
            
            <g:if test="${paymentList && !paymentList.isEmpty()}">
                <g:each in="${paymentList}" var="payment">
                    <atlas-table-row selection-value="${payment.id}">
                        <atlas-table-col>${payment?.payer?.name ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>

                            <atlas-badge 
                                text="${payment?.status}" 
                                theme="${payment?.status == 'RECEBIDA' ? 'success' : payment?.status == 'VENCIDA' ? 'danger' : 'warning'}">
                            </atlas-badge>

                        </atlas-table-col>
                        <atlas-table-col>R$ ${payment?.value ?: '0,00'}</atlas-table-col>
                        <atlas-table-col>${payment?.dueDate?.format('dd/MM/yyyy') ?: 'N/A'}</atlas-table-col>
                        <atlas-table-col>

                            <atlas-badge 
                                theme="${payment?.deleted ? 'danger' : 'success'}"
                                text="${payment?.deleted ? 'Inativo' : 'Ativo'}">
                            </atlas-badge>

                        </atlas-table-col>
                        <atlas-table-col>

                            <atlas-layout gap="1" inline>

                                <atlas-button
                                    description="Visualizar"
                                    href="${createLink(controller: 'payment', action: 'show', id: payment.id)}"
                                    size="sm">
                                    Visualizar
                                </atlas-button>
                                
                                <atlas-button
                                    description="Editar"
                                    href="${createLink(controller: 'payment', action: 'edit', id: payment.id)}"
                                    size="sm">
                                    Editar
                                </atlas-button>

                                <g:if test="${payment?.status == com.asaas.mini.enums.PaymentStatus.AGUARDANDO_PAGAMENTO && !payment?.deleted}">
                                    <atlas-button
                                        description="Confirmar Recebimento"
                                        href="${createLink(controller: 'payment', action: 'confirmInCash', id: payment.id)}"
                                        theme="success"
                                        size="sm">
                                        Confirmar
                                    </atlas-button>
                                </g:if>

                            </atlas-layout>
                        </atlas-table-col>
                    </atlas-table-row>
                </g:each>
            </g:if>

            <g:else>
                <atlas-table-row>
                    <atlas-table-col colspan="6">
                        <atlas-text size="sm" color="neutral">Nenhuma cobrança encontrada</atlas-text>
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
                            href="${createLink(controller: 'payment', action: 'index', params: [max: max, offset: Math.max(0, offset - max), sort: sort, order: order])}"
                            size="sm"
                            ‹ Anterior
                        </atlas-button>
                    </g:if>

                    <g:if test="${offset + max < totalCount}">
                        <atlas-button
                            size="md"
                            description="Próxima Página"
                            href="${createLink(controller: 'payment', action: 'index', params: [max: max, offset: offset + max, sort: sort, order: order])}"
                            size="sm"
                            Próxima ›
                        </atlas-button>
                    </g:if>
                </atlas-layout>
            </g:if>


            <atlas-button
                size="md"
                description="Novo Cliente"
                href="${createLink(controller: 'payment', action: 'create')}"
                Novo Cliente
            </atlas-button>

        </atlas-layout>

    </atlas-panel>
</g:applyLayout>
