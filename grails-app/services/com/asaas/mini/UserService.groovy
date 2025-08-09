package com.asaas.mini;


import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.ValidationException
import org.hibernate.mapping.Map;

//ler sobre verificação de login no grails
@Transactional
public class UserService {
    SpringSecurityService springSecurityService

//    classe de teste de usuario - login permissao


    public User save(Map params) {
        validateParams(params)

        User user = new User();

        user.username = params.username;
        user.password = springSecurityService.encodePassword(params.password)
        user.role = params.role;

        user.save()
    }


    private void validateParams(Map params) {
        if (!params.username) {
            throw new ValidationException("O nome de usuário é obrigatório")
        }

        if (!params.password) {
            throw new ValidationException("A senha é obrigatória")
        }

        if (params.password.size() < 6) {
            throw new ValidationException("A senha deve ter pelo menos 6 caracteres")
        }

        if (!params.role) {
            throw new ValidationException("O papel é obrigatório")
        }

        boolean usernameExists = User.where {
            username == params.username
        }.count() > 0

        if (usernameExists) {
            throw new ValidationException("Usuário inválido")
        }
    }

    // show
    // update senha
    // delete
    // restore

}
