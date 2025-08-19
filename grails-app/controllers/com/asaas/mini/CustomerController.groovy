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
    def show() {
        Customer customer = customerService.get(params.id as Long)
        if (!customer) {
            flash.message = "Conta não encontrada"
            redirect(action: "create")
            return
        }
        render(view: "show", model: [customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def update() {
        try {
            Long id = params.long("id")
            Customer customer = customerService.update(id, params)
            respond(customer, [status: 200])

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete() {
        try {
            Long id = params.long("id")
            customerService.delete(id)
            render(status: 204)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
        render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore() {
        try {
            Long id = params.long("id")
            customerService.restore(id)
            render(status: 200)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }
}