package com.asaas.mini

import com.asaas.mini.enums.Role
import com.asaas.mini.utils.BaseEntity

class User extends BaseEntity {

    String username

    String password

    Role role

    boolean enabled = true

    boolean accountExpired = false

    boolean accountLocked = false

    boolean passwordExpired = false

    static belongsTo = [customer: Customer]

    static constraints = {
        username blank: false, unique: true
        password blank: false
        role nullable: false
    }

    static mapping = {
        password column: '`password`'
        role enumType: "string"
    }
}
