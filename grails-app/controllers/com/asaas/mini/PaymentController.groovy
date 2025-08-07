package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PaymentController {

    static responseFormats = ['json']

    PaymentService paymentService

    def save() {
        Customer customer = getCustomerLogged()

        Long id = params.long("id")
        if (!id) {
            render(status: 400, contentType: 'application/json', text: [errors: ["ID do pagamento é obrigatório"]])
            return
        }

        Payer payer = Payer.findByIdAndCustomerAndDeleted(id, customer, false)
        if (!payer) {
            render(status: 400, contentType: 'application/json', text: [errors: ["Pagador não encontrado"]])
            return
        }

        try {
            Payment payment = paymentService.save(params, payer)
            respond(payment, [status: 201])

        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def show() {
        try {
            Long id = params.long("id")
            Customer customer = getCustomerLogged()
            Payment payment = paymentService.get(id, customer)

            if (!payment) {
                render(status: 404, text: "Pagador não encontrado")
                return
            }
            respond payment

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<Payment> paymentList = paymentService.list(customer)
            respond paymentList

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def update() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            Payment updatedPayment = paymentService.update(id, params, customer)
            respond(updatedPayment, [status: 200])

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
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
            paymentService.delete(id, customer)
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
            Payment restoredPayment = paymentService.restore(id, customer)
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
