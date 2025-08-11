package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class UserController {

    static responseFormats = ['json']
    UserService userService

    @Secured(['ROLE_ADMINISTRADOR'])
    def save() {
        try {
            String username = params.username
            String password = params.password
            String role = params.role
            Long customerId = params.long('customerId')

            if (!username || !password || !role || !customerId) {
                render(status: 400, text: [error: "Parâmetros obrigatórios ausentes"].toString())
                return
            }

            Customer customer = Customer.findById(customerId)
            if (!customer) {
                render(status: 404, text: [error: "Customer não encontrado"].toString())
                return
            }

            User user = userService.createUser(username, password, role, customer)
            respond(user, [status: 201])

        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            log.error("Erro ao salvar usuário", exception)
            render(status: 500, contentType: 'application/json', text: [error: exception.message].toString())
        }
    }

    @Secured('permitAll')
    def delete() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            userService.delete(customer, id)
            render(status: 204)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            log.error("Erro ao deletar usuário", exception)
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            userService.restore(customer, id)
            render(status: 204)

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            log.error("Erro ao deletar usuário", exception)
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }
}
