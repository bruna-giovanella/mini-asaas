package com.asaas.mini.auth

import com.asaas.mini.Customer

class User {
    String username

    String password

    boolean enabled = true

    boolean accountExpired

    boolean accountLocked

    boolean passwordExpired

    Customer customer

    static constraints = {
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role as Set
    }
}
