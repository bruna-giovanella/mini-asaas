package com.asaas.mini

import com.asaas.mini.auth.SecurityService
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class PayerController {

    PayerService payerService
    SecurityService securityService

    private Customer getCustomerLogged() {
        return securityService.getCurrentCustomer()
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def index() {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        int max = params.int('max') ?: 10
        int offset = params.int('offset') ?: 0
        String sort = params.sort ?: 'name'
        String order = params.order ?: 'asc'
        
        def result = payerService.listPaginated(customer, max, offset, sort, order)
        
        render(view: "index", model: [
            payerList: result.payerList,
            customer: customer,
            totalCount: result.totalCount,
            max: max,
            offset: offset,
            sort: sort,
            order: order
        ])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show(Long id) {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        Payer payer = payerService.get(id, customer)

        if (!payer) {
            flash.message = "Pagador não encontrado"
            redirect(action: "index")
            return
        }
        render(view: "show", model: [payer: payer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def create() {
        render(view: "create")
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Payer payer = payerService.save(params, customer)
            flash.message = "Pagador criado com sucesso"
            redirect(action: "show", id: payer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao criar pagador"
            render(view: "create", model: [payer: params])
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao criar pagador"
            render(view: "create", model: [payer: params])
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def edit(Long id) {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        Payer payer = payerService.get(id, customer)
        if (!payer) {
            flash.message = "Pagador não encontrado"
            redirect(action: "index")
            return
        }
        render(view: "edit", model: [payer: payer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def update() {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Long id = params.long("id")
            Payer payer = payerService.update(id, params)
            flash.message = "Pagador atualizado com sucesso"
            redirect(action: "show", id: payer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao atualizar pagador"
            redirect(action: "edit", id: params.id)
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao atualizar pagador"
            redirect(action: "edit", id: params.id)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete(Long id) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                response.status = 401
                render(text: "Usuário não autenticado", status: 401)
                return
            }
            
            payerService.delete(id, customer)
            flash.message = "Cliente removido com sucesso"
            redirect(action: "index")

        } catch (IllegalArgumentException illegalArgumentException) {
            response.status = 400
            render(text: illegalArgumentException.message, status: 400)
        } catch (Exception exception) {
            response.status = 500
            render(text: "Erro inesperado ao excluir cliente", status: 500)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore(Long id) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                response.status = 401
                render(text: "Usuário não autenticado", status: 401)
                return
            }
            
            payerService.restore(id, customer)
            flash.message = "Cliente restaurado com sucesso"
            redirect(action: "index")

        } catch (IllegalArgumentException illegalArgumentException) {
            response.status = 400
            render(text: illegalArgumentException.message, status: 400)
        } catch (Exception exception) {
            response.status = 500
            render(text: "Erro inesperado ao restaurar cliente", status: 500)
        }
    }
}