package com.asaas.mini.auth

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

@Secured(['permitAll'])
class LogoutController {

    def index() {
        if (!springSecurityService.isLoggedIn()) {
            redirect(uri: '/')
            return
        }
        render view: '/logout/index'
    }

    def logout() {
        SecurityContextHolder.clearContext()
        session.invalidate()
        redirect(uri: '/')
    }
}
