package com.asaas.mini

import com.asaas.mini.utils.BaseEntity

class User extends BaseEntity{

    String email

    String password

    boolean enabled = true

    boolean accountExpired = false

    boolean accountLocked = false

    boolean passwordExpired = false

    Customer customer

    static transients = ['authorities']

    static constraints = {
        email blank: false, unique: true, email: true
        password blank: false
        customer nullable: false
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }
}
