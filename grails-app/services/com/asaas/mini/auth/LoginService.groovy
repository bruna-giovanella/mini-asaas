package com.asaas.mini.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginService {

    def springSecurityService

    User authenticate(String username, String password) {
        if (!username || !password) {
            throw new IllegalArgumentException("Preencha todos os campos")
        }

        User user = User.findByUsernameAndDeleted(username, false)
        if (!user) throw new IllegalStateException("Usuário não encontrado")
        if (!user.enabled) throw new IllegalStateException("Usuário desabilitado")
        if (!springSecurityService.passwordEncoder.matches(password, user.password)) {
            throw new IllegalStateException("Senha incorreta")
        }

        def authorities = user.authorities.collect { new SimpleGrantedAuthority(it.authority) }
        SecurityContextHolder.context.authentication =
                new UsernamePasswordAuthenticationToken(user, null, authorities)

        return user
    }

    void logout(HttpServletRequest request, HttpServletResponse response) {
        if (springSecurityService.isLoggedIn()) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler()
                    .logout(request, response, SecurityContextHolder.context.authentication)
        }
    }
}
