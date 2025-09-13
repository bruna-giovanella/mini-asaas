package com.asaas.mini.auth

class LoginController {

    LoginService loginService

    def index() {
        render(view: "index")
    }

    def auth() {
        try {
            User user = loginService.authenticate(params.username, params.password)
            redirect(controller: 'customer', action: 'index', id: user.customer.id)
        } catch (Exception e) {
            flash.message = e.message
            render(view: "index")
        }
    }

    def logout() {
        loginService.logout(request, response)
        redirect(action: 'index')
    }
}
