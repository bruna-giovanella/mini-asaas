package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class PaymentController {

    static responseFormats = ['json']

    PaymentService paymentService

    def save() {
        Customer customer = getCustomerLogged()

        Long payerId = params.long('payerId')
        if (!payerId) {
            render(status: 400, contentType: 'application/json', text: [errors: ["Payer ID is required"]])
            return
        }

        Payer payer = Payer.findByIdAndCustomerAndDeleted(payerId, customer, false)
        if (!payer) {
            render(status: 400, contentType: 'application/json', text: [errors: ["Payer not found or does not belong to the logged-in customer"]])
            return
        }

        try {
            Payment payment = paymentService.save(params, payer)
            respond payment, [status: 201]
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage])
        } catch (Exception e) {
            render(status: 500, contentType: 'application/json', text: [errors: ["Internal Server Error: ${e.message}"]])
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
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<Payment> paymentList = paymentService.list(customer)
            respond paymentList
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def delete() {
        try {
            Customer customer = getCustomerLogged()
            Payment deletedPayment = paymentService.delete(params.long('id'), customer)
            render(status: 204)

        } catch (IllegalArgumentException e) {
            render(status: 400, text: e.message)
        } catch (ValidationException e) {
            render(status: 422, text: e.message)
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    private Customer getCustomerLogged() {
        return Customer.get(2L)
    }
}
