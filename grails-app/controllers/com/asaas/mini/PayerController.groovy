package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PayerController {

    static responseFormats = ['json']
    PayerService payerService

    def save() {
        try {
            Customer customer = getCustomerLogged() // trocar pelo SpringSecurity
            Payer payer = payerService.save(params, customer)
            respond payer, [status: 201]

        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def show() {
        try {
            Customer customer = getCustomerLogged() // trocar pelo SpringSecurity
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
            Customer customer = getCustomerLogged() // trocar pelo SpringSecurity
            Long id = params.id as Long
            payerService.delete(id, customer)
            render(status: 204)

        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def restore() {
        try {
            Customer customer = getCustomerLogged() // trocar pelo SpringSecurity
            Long id = params.id as Long
            payerService.restore(id, customer)
            render(status: 200)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }

}
