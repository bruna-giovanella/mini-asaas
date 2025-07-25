package com.asaas.mini

import com.asaas.mini.enums.Role
import org.grails.datastore.mapping.validation.ValidationException
import org.springframework.security.crypto.password.PasswordEncoder

import javax.transaction.Transactional

@Transactional
class UserService {

    PasswordEncoder passwordEncoder

    public void save(Customer customer, Map params) {
        String username = params.username
        String password = params.password
        String role = params.role

        if (!username || !password) {
            throw new ValidationException("Email e senha do usuário administrador são obrigatórios", null)
        }

        User user = new User(
                username: username,
                password: passwordEncoder.encode(password),
                customer: customer
        )

        user.save(flush: true, failOnError: true)

        new UserRole(user: user, role: Role.valueOf(role)).save(flush: true)
    }
}
