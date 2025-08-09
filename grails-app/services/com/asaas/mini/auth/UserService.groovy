package com.asaas.mini.auth

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

@Transactional
class UserService {

    PasswordEncoder passwordEncoder

    public User createUser(String username, String password, List<String> roles) {
        User user = new User(username: username,
                             password: passwordEncoder.encode(password))

        if (!user.save(flush: true)) {
            throw new RuntimeException("Erro ao salvar usuário: ${user.errors}")
        }

        for (String roleName : roles) {
            Role role = Role.findByAuthority(roleName)
            if (!role) {
                throw new IllegalArgumentException("Role '${roleName}' não encontrada")
            }
            UserRole.create(user, role)
        }

        return user

    }

}
