package com.asaas.mini.auth

import com.asaas.mini.Customer
import com.asaas.mini.Payer
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
            Customer customer = getCustomerLogged()

            if (!username || !password || !role || !customerId) {
                render(status: 400, text: [error: "Parâmetros obrigatórios ausentes"].toString())
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

    @Secured(['ROLE_ADMINISTRADOR'])
    def show() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            User user = userService.get(customer, id)

            if (!user) {
                render(status: 404, text: "Usuário não encontrado")
                return
            }
            respond user

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def list() {
        try {
            Customer customer = getCustomerLogged()
            List<User> userList = userService.list(customer)
            respond userList

        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured('permitAll')
    def update() {
        try {
            Customer customer = getCustomerLogged()
            String username = params.username
            String password = params.password
            String role = params.role

            if (!username || !role || !customer) {
                render(status: 400, contentType: 'application/json', text: [error: "Parâmetros obrigatórios ausentes"].toString())
                return
            }

            Long id = params.long("id")
            User user = userService.update(username, password, role, customer, id)
            respond(user, [status: 200])

        } catch (IllegalArgumentException illegalArgumentException) {
            render(status: 404, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        } catch (ValidationException validationException) {
            render(status: 400, contentType: 'application/json', text: [errors: "Um erro inesperado aconteceu"].toString())
        } catch (Exception exception) {
            render(status: 500, contentType: 'application/json', text: [error: "Um erro inesperado aconteceu"].toString())
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
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
