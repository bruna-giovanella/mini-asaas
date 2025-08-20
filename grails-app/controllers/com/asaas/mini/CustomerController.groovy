package com.asaas.mini

import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class CustomerController {

    CustomerService customerService

    @Secured('permitAll')
    def create() {
        render(view: "create")
    }

    @Secured('permitAll')
    def save() {
        try {
            Customer customer = customerService.save(params)
            flash.message = "Conta criada com sucesso"
            redirect(action: "show", id: customer.id)

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
        render(view: "show", model: [customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def edit(Long id) {
        Customer customer = customerService.get(id)
        if (!customer) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
            return
        }
        render(view: "edit", model: [customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def update() {
        try {
            Long id = params.long("id")
            Customer customer = customerService.update(id, params)
            flash.message = "Conta atualizada com sucesso"
            redirect(action: "show", id: customer.id)

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

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete(Long id) {
        try {
            customerService.delete(id)
            flash.message = "Conta removida com sucesso"
            redirect(action: "create")

        } catch (IllegalArgumentException illegalArgumentException) {
            flash.message = "Erro ao excluir conta"
            redirect(action: "show", id: id)
        } catch (Exception exception) {
            flash.message = "Erro ao excluir conta"
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore(Long id) {
        try {
            customerService.restore(id)
            flash.message = "Conta restaurada com sucesso"
            redirect(action: "show", id: id)

        } catch (IllegalArgumentException illegalArgumentException) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
        } catch (Exception exception) {
            flash.message = "Erro ao restaurar conta"
            redirect(action: "create")
        }
    }
}