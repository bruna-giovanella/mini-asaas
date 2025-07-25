package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PayerController {

    static responseFormats = ['json']
    PayerService payerService

    def save() {
        try {
            Customer customer = getCustomerLogged()
            Payer payer = payerService.save(params, customer)
            respond(payer, [status: 201])

        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def show() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.id as Long

            def payer = payerService.get(id, customer)

            if (!payer) {
                render(status: 404, text: "Payer not found")
                return
            }
            respond payer
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def delete() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            payerService.delete(id, customer)
            render(status: 204)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def restore() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            payerService.restore(id, customer)
            render(status: 200)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }

}
