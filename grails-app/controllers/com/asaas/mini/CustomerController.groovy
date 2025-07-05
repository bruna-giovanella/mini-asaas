package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

import java.util.function.LongConsumer

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService // injeção de dependência automática

    def save() {
        try {
            def customer = customerService.save(params)
            respond customer, [status: 201] // para retornar Json 201 (created)
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    def delete() {
        try {
            Long id = params.id as Long
            customerService.deleteCustomer(id)
            render(status: 204)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    def restore() {
        try {
            Long id = params.id as Long
            customerService.restoreCustomer(id)
            render(status: 200)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }
}
