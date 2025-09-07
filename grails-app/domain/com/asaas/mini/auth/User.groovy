package com.asaas.mini.auth

import com.asaas.mini.Customer
import com.asaas.mini.utils.BaseEntity

class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1

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
        customer nullable: false
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role as Set
    }
}