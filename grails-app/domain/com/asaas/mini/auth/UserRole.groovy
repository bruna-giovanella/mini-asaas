package com.asaas.mini.auth

import com.asaas.mini.utils.BaseEntity

class UserRole extends BaseEntity implements Serializable {
    User user

    Role role

    static mapping = {
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
