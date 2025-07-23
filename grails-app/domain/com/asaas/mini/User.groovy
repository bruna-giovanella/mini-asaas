package com.asaas.mini

import com.asaas.mini.enums.Role
import com.asaas.mini.utils.BaseEntity

class User extends BaseEntity{

    String email

    String password

    boolean enabled = true

    boolean accountExpired = false

    boolean accountLocked = false

    boolean passwordExpired = false

    Customer customer

    static constraints = {
        email blank: false, email: true
        password blank: false
        customer nullable: false
    }
}
