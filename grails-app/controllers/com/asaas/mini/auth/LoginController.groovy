package com.asaas.mini.auth

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.WebAttributes

import javax.servlet.http.HttpServletResponse

@Secured(['permitAll'])
class LoginController {

    def index() {
        if (isLoggedIn()) {
            redirect(uri: '/customer/index')
            return
        }
        render view: '/login/index'
    }

    def auth() {
        if (isLoggedIn()) {
            redirect(uri: '/customer/index')
            return
        }

        // Se chegou aqui, é porque o Spring Security não processou o login
        // Redireciona de volta para a página de login com mensagem de erro
        flash.message = "Usuário ou senha inválidos"
        redirect(action: 'index')
    }

    def authfail() {
        String msg = ''
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = message(code: 'springSecurity.errors.login.expired')
            } else if (exception instanceof CredentialsExpiredException) {
                msg = message(code: 'springSecurity.errors.login.expired')
            } else if (exception instanceof DisabledException) {
                msg = message(code: 'springSecurity.errors.login.disabled')
            } else if (exception instanceof LockedException) {
                msg = message(code: 'springSecurity.errors.login.locked')
            } else {
                msg = message(code: 'springSecurity.errors.login.fail')
            }
        }

        if (springSecurityService.isAjax(request)) {
            render(contentType: 'text/json') {
                [success: false, message: msg]
            }
        } else {
            flash.message = msg
            redirect(action: 'index', params: params)
        }
    }

    def denied() {
        if (springSecurityService.isAjax(request)) {
            render(contentType: 'text/json') {
                [success: false, message: message(code: 'springSecurity.errors.access.denied')]
            }
        } else {
            render view: '/login/denied'
        }
    }

    def full() {
        render view: '/login/full'
    }

    def ajaxSuccess() {
        render(contentType: 'text/json') {
            [success: true, username: springSecurityService.authentication.name]
        }
    }

    def ajaxDenied() {
        render(contentType: 'text/json') {
            [success: false, message: message(code: 'springSecurity.errors.access.denied')]
        }
    }

    boolean isLoggedIn() {
        return springSecurityService.isLoggedIn()
    }
}
