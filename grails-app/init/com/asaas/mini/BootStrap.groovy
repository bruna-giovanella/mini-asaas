package com.asaas.mini

import com.asaas.mini.auth.Role

class BootStrap {

    def init = { servletContext ->
        // Criar roles básicas se não existirem
        createRoles()
    }

    def destroy = {
    }

    private void createRoles() {
        Role.withTransaction { status ->
            def roles = ['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR']

            roles.each { roleName ->
                if (!Role.findByAuthority(roleName)) {
                    def role = new Role(authority: roleName)
                    role.save(flush: true)
                    println "Role criada: ${roleName}"
                }
            }
        }
    }
}
