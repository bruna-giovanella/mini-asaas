package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PaymentController {

    static responseFormats = ['json']

    PaymentService paymentService

    def save() {
        Customer customer = getCustomerLogged()

        Long payerId = params.long('payerId')
        if (!payerId) {
            render(status: 400, contentType: 'application/json', text: [errors: ["ID do pagamento é obrigatório"]])
            return
        }

        Payer payer = Payer.findByIdAndCustomerAndDeleted(payerId, customer, false)
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
            Long id = params.id as Long
            Customer customer = getCustomerLogged()
            Payment payment = paymentService.get(id, customer)

            if (!payment) {
                render(status: 404, text: "Payment not found")
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

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }
}
