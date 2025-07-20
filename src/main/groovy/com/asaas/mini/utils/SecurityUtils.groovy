package com.asaas.mini.utils

import com.asaas.mini.Customer
import com.asaas.mini.User
import grails.plugin.springsecurity.SpringSecurityService

class SecurityUtils {

    SpringSecurityService springSecurityService

    User getUserLogged() {
        return springSecurityService.currentUser as User
    }

    Customer getCustomerLogged() {
        return getUserLogged()?.customer
    }
}
