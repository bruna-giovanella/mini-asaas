package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.core.context.SecurityContextHolder

@Transactional
class SecurityService {

    SpringSecurityService springSecurityService

    Customer getCurrentCustomer() {
        User currentUser = getCurrentUser()
        return currentUser?.customer
    }

    User getCurrentUser() {
        if (!springSecurityService.isLoggedIn()) {
            return null
        }
        
        def principal = springSecurityService.principal
        if (principal instanceof User) {
            return principal
        }
        
        String username = principal?.username ?: principal?.toString()
        if (username) {
            return User.findByUsername(username)
        }
        
        return null
    }

    boolean canAccessCustomer(Long customerId) {
        Customer currentCustomer = getCurrentCustomer()
        return currentCustomer?.id == customerId
    }

    boolean canAccessCustomer(Customer customer) {
        Customer currentCustomer = getCurrentCustomer()
        return currentCustomer?.id == customer?.id
    }
}
