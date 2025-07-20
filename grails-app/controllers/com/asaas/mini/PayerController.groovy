package com.asaas.mini

import com.asaas.mini.utils.SecurityUtils
import org.grails.datastore.mapping.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class PayerController {

    static responseFormats = ['json']

    SecurityUtils securityUtils
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

    def show() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.id as Long

            Payer payer = payerService.get(id, customer)

            if (!payer) {
                render(status: 404, text: "Payer not found")
                return
            }
            respond payer
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<Payer> payerList = payerService.list(customer)
            respond payerList
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    def update() {
        try {
            Long id = params.id as Long
            Payer payer = payerService.update(id, params)
            respond payer, [status: 200]
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
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

    def restore() {
        try {
            Customer customer = getCustomerLogged()
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
        return securityUtils.getCustomerLogged()
    }
}
