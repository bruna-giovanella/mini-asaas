package com.asaas.mini

import org.grails.datastore.mapping.validation.ValidationException

class CustomerController {

    static responseFormats = ['json']
    CustomerService customerService

    def save() {
        try {
            Customer customer = customerService.save(params)
            respond customer, [status: 201]
        } catch (ValidationException ignored) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception ignored) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def show() {
        try {
            Long id = params.long("id")
            Customer customer = customerService.getCustomer(id)

            if (!customer) {
                render(status: 404, text: "Cliente não encontrado")
                return
            }
            respond customer
        } catch (Exception ignored) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def delete() {
        try {
            Long id = params.long("id")
            customerService.deleteCustomer(id)
            render(status: 204)
        } catch (IllegalArgumentException ignored) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException ignored) {
            render(status: 400, contentType: 'application/json', text: [errors: "um erro inesperado aconteceu"].toString())
        } catch (Exception ignored) {
        render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    def restore() {
        try {
            Long id = params.long("id")
            customerService.restoreCustomer(id)
            render(status: 200)
        } catch (IllegalArgumentException ignored) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException ignored) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception ignored) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }
}
