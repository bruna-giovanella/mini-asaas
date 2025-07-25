package com.asaas.mini

import com.asaas.mini.utils.BaseEntity

class User extends BaseEntity {

    String username

    String password

    boolean enabled = true

    boolean accountExpired = false

    boolean accountLocked = false

    boolean passwordExpired = false

    static belongsTo = [customer: Customer]

    static constraints = {
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
    }
}
