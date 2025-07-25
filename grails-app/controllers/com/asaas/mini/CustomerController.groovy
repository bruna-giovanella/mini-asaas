package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

import java.util.function.LongConsumer

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService

    def save() {
        try {
            Customer customer = customerService.save(params)
            respond customer, [status: 201]
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception ignored) {
            render(status: 500, contentType: 'application/json', text: [error: "Internal server error"].toString())
        }
    }

    def delete() {
        try {
            Long id = params.long("id")
            customerService.deleteCustomer(id)
            render(status: 204)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception ignored) {
        render(status: 500, contentType: 'application/json', text: [error: "Internal server error"].toString())
    }
    }

    def restore() {
        try {
            Long id = params.long("id")
            customerService.restoreCustomer(id)
            render(status: 200)
        } catch (IllegalArgumentException e) {
            render(status: 404, contentType: 'application/json', text: [error: e.message].toString())
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception ignored) {
            render(status: 500, contentType: 'application/json', text: [error: "Internal server error"].toString())
        }
    }
}
