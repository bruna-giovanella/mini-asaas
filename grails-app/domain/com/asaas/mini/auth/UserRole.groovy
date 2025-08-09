package com.asaas.mini.auth

class UserRole implements Serializable {
    User user

    Role role

    static maping = {
        id composite: ['role', 'user']
        version false
    }

    static constraints = {
        role nullable: false
        user nallable: false
    }

    static UserRole create(User user, Role role, boolean flush = false) {
        def instance = new UserRole(user: user, role: role)
        instance.save(flush: flush)
        instance
    }

}
