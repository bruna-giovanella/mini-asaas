package com.asaas.mini.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class LoginController {

    def springSecurityService

    def index() {
        render(view: "index")
    }

    def auth() {
        if (request.method == 'GET') {
            redirect(action: 'index')
            return
        }
        
        String username = params.username
        String password = params.password

        if (!username || !password) {
            flash.message = "Preencha todos os campos"
            render(view: "index")
            return
        }

        try {
            User user = User.findByUsernameAndDeleted(username, false)
            
            if (!user) {
                flash.message = "Usuário não encontrado"
                render(view: "index")
                return
            }

            if (!user.enabled) {
                flash.message = "Usuário desabilitado"
                render(view: "index")
                return
            }

            if (!springSecurityService.passwordEncoder.matches(password, user.password)) {
                flash.message = "Senha incorreta"
                render(view: "index")
                return
            }

            Set<GrantedAuthority> authorities = new HashSet<>()
            user.authorities.each { role ->
                authorities.add(new SimpleGrantedAuthority(role.authority))
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, authorities
            )
            
            SecurityContextHolder.context.authentication = auth
            
            redirect(controller: 'customer', action: 'index', id: user.customer.id)

        } catch (Exception e) {
            flash.message = "Erro ao fazer login: ${e.message}"
            render(view: "index")
        }
    }

    def logout() {
        if (springSecurityService.isLoggedIn()) {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.context.authentication)
        }
        redirect(action: 'index')
    }
}