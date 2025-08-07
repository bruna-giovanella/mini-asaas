package com.asaas.mini

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured

class PayerController {

    static responseFormats = ['json']

    PayerService payerService
    SpringSecurityService springSecurityService

    private Customer getCustomerLogged() {
        User user = springSecurityService.currentUser as User
        return user?.customer
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            Payer payer = payerService.save(params, customer)
            respond(payer, [status: 201])

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: illegalArgumentException.message].toString())
        }
        catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: validationException.errors.allErrors*.defaultMessage].toString())
        }
        catch (Exception exception) {
            exception.printStackTrace() // log para debug
            render(status: 500, contentType: 'application/json', text: [error: exception.message].toString())
        }

    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            Payer payer = payerService.get(id, customer)

            if (!payer) {
                render(status: 404, text: "Pagador não encontrado")
                return
            }
            respond payer

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<Payer> payerList = payerService.list(customer)
            respond payerList

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def update() {
        try {
            Long id = params.long("id")
            Payer payer = payerService.update(id, params)
            respond(payer, [status: 200])

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def delete() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            payerService.delete(id, customer)
            render(status: 204)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
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
}
