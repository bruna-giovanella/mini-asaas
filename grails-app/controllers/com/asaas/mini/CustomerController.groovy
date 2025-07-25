package com.asaas.mini

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService
    SpringSecurityService springSecurityService

    private Customer getCustomerLogged() {
        User user = springSecurityService.currentUser as User
        return user?.customer
    }

    @Secured('permitAll')
    def save() {
        try {
            Customer customer = customerService.save(params)
            respond customer, [status: 201]
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show() {
        try {
            Customer loggedCustomer = getCustomerLogged()

            if (!loggedCustomer) {
                render(status: 404, text: "Customer not found")
                return
            }

            Customer customer = customerService.get(Long.valueOf(loggedCustomer.id))
            respond customer
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def update() {
        try {
            Customer loggedCustomer = getCustomerLogged()

            if (!loggedCustomer) {
                render(status: 404, text: "Customer not found")
                return
            }

            Customer customer = customerService.update(Long.valueOf(loggedCustomer.id), params)
            respond customer, [status: 200]
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception e) {
            render(status: 500, text: "Internal Server Error: ${e.message}")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete() {
        try {
            Customer loggedCustomer = getCustomerLogged()

            if (!loggedCustomer) {
                render(status: 404, text: "Customer not found")
                return
            }

            customerService.delete(Long.valueOf(loggedCustomer.id))
            render(status: 204)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore() {
        try {
            Customer loggedCustomer = getCustomerLogged()

            if (!loggedCustomer) {
                render(status: 404, text: "Customer not found")
                return
            }

            customerService.restore(Long.valueOf(loggedCustomer.id))
            render(status: 200)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }
}
