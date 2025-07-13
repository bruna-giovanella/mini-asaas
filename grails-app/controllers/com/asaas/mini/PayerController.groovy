package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PayerController {

    static responseFormats = ['json']
    PayerService payerService

    def save() {
        try {
            Customer customer = getCustomerLogged()
            Payer payer = payerService.save(params, customer)
            respond payer, [status: 201]

        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def delete() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.id as Long
            payerService.delete(id, customer)
            render(status: 204)

        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }


}
