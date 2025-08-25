package com.asaas.mini

import com.asaas.mini.auth.Role

class BootStrap {

    def init = { servletContext ->
        Role.withTransaction { status ->
            if (Role.count() == 0) {
                new Role(authority: 'ROLE_ADMINISTRADOR ').save(flush: true, failOnError: true)
                new Role(authority: 'ROLE_FINANCEIRO').save(flush: true, failOnError: true)
                new Role(authority: 'ROLE_VENDEDOR').save(flush: true, failOnError: true)
            }
        }
    }
    def destroy = {
    }
}