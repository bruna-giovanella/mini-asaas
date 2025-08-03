package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService

    def save() {
        try {
            Customer customer = customerService.save(params)
            respond customer, [status: 201]
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception e) {
            render(status: 500, contentType: 'application/json', text: [error: "Internal server error"].toString())
        }
    }

    def delete() {
        try {
            Long id = params.long("id")
            customerService.deleteCustomer(id)
            render(status: 204)
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        } catch (Exception e) {
            render(status: 500, contentType: 'application/json', text: [error: "Internal server error"].toString())
        }
    }
}
