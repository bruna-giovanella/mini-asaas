package com.asaas.mini

import com.asaas.mini.utils.SecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class UserController {

    static responseFormats = ['json']
    UserService userService

    @Secured(['ROLE_ADMINISTRADOR'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            User user = userService.save(customer, params)
            respond user, [status: 201]
        } catch (ValidationException e) {
            render(status: 400, contentType: 'application/json', text: [errors: e.errors.allErrors*.defaultMessage].toString())
        }
    }

    private Customer getCustomerLogged() {
        return securityUtils.customerLogged
    }
}
