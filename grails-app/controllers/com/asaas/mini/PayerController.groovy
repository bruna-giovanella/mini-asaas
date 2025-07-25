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

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }


}
