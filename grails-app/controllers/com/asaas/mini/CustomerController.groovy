package com.asaas.mini

import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService

    @Secured('permitAll')
    def save() {
        try {
            Customer customer = customerService.save(params)
            respond(customer, [status: 201])

        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            log.error("Erro ao salvar cliente", exception)
            render(status: 500, contentType: 'application/json', text: [error: exception.message].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show() {
        try {
            Long id = params.long("id")
            Customer customer = customerService.get(id)

            if (!customer) {
                render(status: 404, text: "Cliente não encontrado")
                return
            }
            respond customer

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
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