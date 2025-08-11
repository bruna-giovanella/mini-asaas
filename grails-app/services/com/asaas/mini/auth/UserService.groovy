package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

@Transactional
class UserService {

    PasswordEncoder passwordEncoder

    public User createUser(String username, String password, String role, Customer customer) {
        User user = new User(
                username: username,
                password: passwordEncoder.encode(password),
                customer: customer,
                enabled: true,
                accountExpired: false,
                accountLocked: false,
                passwordExpired: false
        )

        if (!user.save(flush: true)) {
            throw new RuntimeException("Erro ao salvar usuário: ${user.errors}")
        }

        Role roleObj = Role.findByAuthority(role)
        if (!roleObj) {
            throw new IllegalArgumentException("Role '${role}' não encontrada")
        }

        UserRole.create(user, roleObj)

        return user
    }

}
