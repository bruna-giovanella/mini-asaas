package com.asaas.mini.auth

import java.io.Serializable

class UserRole implements Serializable {
    User user
    Role role

    static mapping = {
        id composite: ['role', 'user']
        version false
    }

    static constraints = {
        role nullable: false
        user nullable: false
    }

    static UserRole create(User user, Role role, boolean flush = false) {
        def instance = new UserRole(user: user, role: role)
        instance.save(flush: flush)
        instance
    }

    @Override
    boolean equals(other) {
        if (!(other instanceof UserRole)) {
            return false
        }
        other.user?.id == user?.id && other.role?.id == role?.id
    }

    @Override
    int hashCode() {
        def builder = new org.apache.commons.lang.builder.HashCodeBuilder()
        if (user) builder.append(user.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }
}
