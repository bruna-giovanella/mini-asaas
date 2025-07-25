package com.asaas.mini

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured

class PaymentController {

    static responseFormats = ['json']

    PaymentService paymentService
    SpringSecurityService springSecurityService

    private Customer getCustomerLogged() {
        User user = springSecurityService.currentUser as User
        return user?.customer
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
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

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
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

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<Payment> paymentList = paymentService.list(customer)
            respond paymentList
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def update() {
        try {
            Customer customer = getCustomerLogged()
            Payment updatedPayment = paymentService.update(params.long('id'), params, customer)
            respond updatedPayment, [status: 200]
        } catch (IllegalArgumentException e) {
            render status: 400, text: e.message
        } catch (ValidationException e) {
            render status: 422, text: e.message
        } catch (Exception e) {
            render status: 500, text: "Internal Server Error: ${e.message}"
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
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

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def restore() {
        try {
            Customer customer = getCustomerLogged()
            Payment restoredPayment = paymentService.restore(params.long('id'), customer)
            render(status: 200)

        } catch (IllegalArgumentException e) {
            render(status: 400, text: e.message)
        } catch (ValidationException e) {
            render(status: 422, text: e.message)
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def confirmInCash() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long('id')

            Payment payment = paymentService.confirmInCash(id, customer)
            respond payment, [status: 200]
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            render status: 400, text: e.message
        }
    }
}
