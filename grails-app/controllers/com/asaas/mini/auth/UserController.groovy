package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class UserController {

    UserService userService
    SecurityService securityService

    private Customer getCustomerLogged() {
        return securityService.getCurrentCustomer()
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def index() {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        int max = params.int('max') ?: 10
        int offset = params.int('offset') ?: 0
        String sort = params.sort ?: 'username'
        String order = params.order ?: 'asc'
        
        def result = userService.listPaginated(customer, max, offset, sort, order)
        
        render(view: "index", model: [
            userList: result.userList,
            customer: customer,
            totalCount: result.totalCount,
            max: max,
            offset: offset,
            sort: sort,
            order: order
        ])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def create() {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        List<Role> roles = Role.list()
        render(view: "create", model: [roles: roles, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
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
            redirect(action: "index")

        } catch (ValidationException validationException) {
            flash.message = "Erro ao salvar usuário: ${validationException.message}"
            redirect(action: "create")
        } catch (Exception exception) {
            flash.message = "Erro inesperado: ${exception.message}"
            redirect(action: "create")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def show(Long id) {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
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
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
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
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Long id = params.long("id")
            String username = params.username
            String password = params.password
            String role = params.role

            if (!username || !role) {
                flash.message = "Preencha todos os campos obrigatórios"
                redirect(action: "edit", id: id)
                return
            }

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
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
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
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            userService.restore(customer, id)
            flash.message = "Usuário restaurado com sucesso"
            redirect(action: "index")
        } catch (Exception exception) {
            flash.message = "Erro ao restaurar usuário"
            redirect(action: "index")
        }
    }
}
