package com.asaas.mini.auth

import com.asaas.mini.Customer
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder

import java.security.Principal

@Transactional
class AuthenticationService {

    SpringSecurityService springSecurityService
    AuthenticationManager authenticationManager
    PasswordEncoder passwordEncoder

    User authenticateUser(String username, String password) {
        try {
            User user = User.findByUsername(username)
            if (!user) {
                throw new IllegalArgumentException("Usuário não encontrado")
            }

            if (!user.enabled) {
                throw new IllegalArgumentException("Usuário não encontrado")
            }

            if (user.deleted) {
                throw new IllegalArgumentException("Usuário não encontrado")
            }

            if (!passwordEncoder.matches(password, user.password)) {
                throw new IllegalArgumentException("Senha incorreta")
            }

            try {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password)
                Authentication authentication = authenticationManager.authenticate(authToken)
                
                if (authentication.authenticated) {
                    SecurityContextHolder.context.authentication = authentication
                    return user
                }
            } catch (Exception exception) {
                log.error("Erro na autenticação do Spring Security: ${exception.message}")
                SecurityContextHolder.context.authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.authorities)
                throw new IllegalArgumentException("Falha na autenticação")
            }

            return null
        } catch (Exception exception) {
            log.error("Erro na autenticação do usuário ${username}: ${exception.message}", exception)
            throw new IllegalArgumentException("Falha na autenticação")
        }
    }


    User getCurrentUser() {
        def principal = springSecurityService.principal
        if (principal instanceof User) {
            return principal
        }
        return null
    }

    Customer getCurrentCustomer() {
        User user = getCurrentUser()
        return user?.customer
    }

    boolean hasRole(String role) {
        return springSecurityService.ifAnyGranted(role)
    }

    boolean isLoggedIn() {
        return springSecurityService.isLoggedIn()
    }

    void logout() {
        springSecurityService.logout()
    }
}
