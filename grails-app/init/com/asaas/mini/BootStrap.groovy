package com.asaas.mini

import com.asaas.mini.auth.Role

class BootStrap {

    def init = { servletContext ->

        String[] roles = ['ROLE_ADMIN', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR']

        for (String authority : roles) {
            if (!Role.findByAuthority(authority)) {
                new Role(authority: authority).save(flush: true)
            }
        }

    }

    def destroy = {}
}
