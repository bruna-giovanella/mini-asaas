package com.asaas.mini

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService

@Transactional
class UserService {

    SpringSecurityService springSecurityService

    User createUser(String email, String rawPassword, Customer customer, List<String> roles) {
        if (User.findByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso")
        }

        String encodedPassword = springSecurityService.encodePassword(rawPassword)

        User user = new User(email: email, password: encodedPassword, customer: customer)
        user.save(flush: true, failOnError: true)

        roles.each { roleName ->
            Role role = Role.findByAuthority(roleName) ?: new Role(authority: roleName).save(flush: true)
            UserRole.create(user, role, true)
        }

        return user
    }
}
