package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class UserController {

    UserService userService

    private Customer getCustomerLogged() {
        return Customer.get(1L)
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def index() {
        Customer customer = getCustomerLogged()
        List<User> userList = userService.list(customer)
        render(view: "index", model: [userList: userList, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def create() {
        Customer customer = getCustomerLogged()
        List<Role> roles = Role.list()
        render(view: "create", model: [roles: roles, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            String username = params.username
            String password = params.password
            String role = params.role

            if (!username || !password || !role) {
                flash.message = "Preencha todos os campos obrigatórios"
                redirect(action: "create")
                return
            }

            User user = userService.createUser(username, password, role, customer)
            flash.message = "Usuário criado com sucesso"
            redirect(action: "index", id: user.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao salvar usuário"
            redirect(action: "create")
        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            render(view: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def show(Long id) {
        Customer customer = getCustomerLogged()
        User user = userService.get(customer, id)

        if (!user) {
            flash.message = "Usuário não encontrado"
            redirect(action: "index")
            return
        }
        render(view: "show", model: [user: user, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def edit(Long id) {
        Customer customer = getCustomerLogged()
        User user = userService.get(customer, id)
        if (!user) {
            flash.message = "Usuário não encontrado"
            redirect(action: "index")
            return
        }
        List<Role> roles = Role.list()
        render(view: "edit", model: [user: user, roles: roles, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def update() {
        try {
            Customer customer = getCustomerLogged()
            Long id = params.long("id")
            String username = params.username
            String password = params.password
            String role = params.role

            User user = userService.update(username, password, role, customer, id)
            flash.message = "Usuário atualizado com sucesso"
            redirect(action: "index", id: user.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao atualizar usuário"
            redirect(action: "edit", id: params.id)
        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            render(view: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete(Long id) {
        try {
            Customer customer = getCustomerLogged()
            userService.delete(customer, id)
            flash.message = "Usuário deletado com sucesso"
            redirect(action: "index")
        } catch (Exception exception) {
            flash.message = "Erro ao deletar usuário"
            redirect(action: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore(Long id) {
        try {
            Customer customer = getCustomerLogged()
            userService.restore(customer, id)
            flash.message = "Usuário restaurado com sucesso"
            redirect(action: "index")
        } catch (Exception exception) {
            flash.message = "Erro ao restaurar usuário"
            redirect(action: "index")
        }
    }
}
