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

    public void delete(Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        User user = User.findByIdAndCustomerAndDeleted(id, customer, false)
        if (!user) {
            throw new IllegalArgumentException("Usuário não encontrado")
        }
        UserRole userRole = UserRole.findByUserAndDeleted(user, false)


        user.deleted = true
        user.markDirty('deleted')
        user.save(failOnError:true)

        userRole.deleted = true
        userRole.markDirty('deleted')
        userRole.save(failOnError:true)

    }

    public void restore(Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        User user = User.findByIdAndCustomerAndDeleted(id, customer, true)
        if (!user) {
            throw new IllegalArgumentException("Usuário não encontrado")
        }
        UserRole userRole = UserRole.findByUserAndDeleted(user, true)


        user.deleted = false
        user.markDirty('deleted')
        user.save(failOnError:true)

        userRole.deleted = false
        userRole.markDirty('deleted')
        userRole.save(failOnError:true)

    }


}
