package com.asaas.mini

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.validation.ValidationException

import javax.transaction.Transactional

@Transactional
class UserService {

    SpringSecurityService springSecurityService

    public User create(Map params, Customer customer) {
        if (!params.email || !params.password) {
            throw new IllegalArgumentException("Email and password are required")
        }

        User user = new User(
                email: params.email,
                password: springSecurityService.encodePassword(params.password),
                customer: customer
        )

        if (!user.validate()) {
            throw new ValidationException("Invalid user", user.errors)
        }

        user.save(flush: true)

        Role role = Role.findByAuthority("ROLE_USER") ?: new Role(authority: "ROLE_USER").save()
        new UserRole(user: user, role: role).save(flush: true)

        return user
    }
}
