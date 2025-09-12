package com.asaas.mini

import com.asaas.mini.auth.SecurityService
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class CustomerController {

    CustomerService customerService
    SecurityService securityService

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def index(Long id) {
        Customer customer
        
        if (id) {
            customer = customerService.get(id)
            if (!customer) {
                flash.message = "Conta não encontrada"
                redirect(action: "create")
                return
            }
            
            if (!securityService.canAccessCustomer(customer)) {
                flash.message = "Acesso negado"
                redirect(controller: "login", action: "denied")
                return
            }
        } else {
            customer = securityService.getCurrentCustomer()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
        }
        
        render(view: "index", model: [customer: customer])
    }

    @Secured('permitAll')
    def create() {
        render(view: "create")
    }

    @Secured('permitAll')
    def save() {
        try {
            Customer customer = customerService.save(params)
            flash.message = "Conta criada com sucesso"
            redirect(action: "index", id: customer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao salvar conta"
            render(view: "create", model: [customer: params])
        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            render(view: "create", model: [customer: params])
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show(Long id) {
        Customer customer = customerService.get(id)
        if (!customer) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
            return
        }
        
        if (!securityService.canAccessCustomer(customer)) {
            flash.message = "Acesso negado"
            redirect(controller: "login", action: "denied")
            return
        }
        
        render(view: "show", model: [customer: customer])
    }

    @Secured('ROLE_ADMINISTRADOR')
    def edit(Long id) {
        Customer customer = customerService.get(id)
        if (!customer) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
            return
        }
        
        if (!securityService.canAccessCustomer(customer)) {
            flash.message = "Acesso negado"
            redirect(controller: "login", action: "denied")
            return
        }
        
        render(view: "edit", model: [customer: customer])
    }

    @Secured('ROLE_ADMINISTRADOR')
    def update() {
        try {
            Long id = params.long("id")
            
            if (!securityService.canAccessCustomer(id)) {
                flash.message = "Acesso negado"
                redirect(controller: "login", action: "denied")
                return
            }
            
            Customer customer = customerService.update(id, params)
            flash.message = "Conta atualizada com sucesso"
            redirect(action: "index", id: customer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao atualizar conta"
            redirect(action: "edit", id: params.id)
        } catch (IllegalArgumentException illegalArgumentException) {
            flash.message = "Erro ao atualizar conta"
            redirect(action: "edit", id: params.id)
        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            redirect(action: "edit", id: params.id)
        }
    }

    @Secured('ROLE_ADMINISTRADOR')
    def delete(Long id) {
        try {
            if (!securityService.canAccessCustomer(id)) {
                flash.message = "Acesso negado"
                redirect(controller: "login", action: "denied")
                return
            }
            
            customerService.delete(id)
            flash.message = "Conta removida com sucesso"
            redirect(action: "index", id: id)

        } catch (IllegalArgumentException illegalArgumentException) {
            flash.message = "Erro ao excluir conta"
            redirect(action: "show", id: id)
        } catch (Exception exception) {
            flash.message = "Erro ao excluir conta"
            redirect(action: "show", id: id)
        }
    }

    @Secured('ROLE_ADMINISTRADOR')
    def restore(Long id) {
        try {
            if (!securityService.canAccessCustomer(id)) {
                flash.message = "Acesso negado"
                redirect(controller: "login", action: "denied")
                return
            }
            
            customerService.restore(id)
            flash.message = "Conta restaurada com sucesso"
            redirect(action: "index", id: id)

        } catch (IllegalArgumentException illegalArgumentException) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
        } catch (Exception exception) {
            flash.message = "Erro ao restaurar conta"
            redirect(action: "create")
        }
    }
}