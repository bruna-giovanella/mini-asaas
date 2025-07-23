package com.asaas.mini

import com.asaas.mini.enums.Role

class UserRole implements Serializable{

    User user

    Role role

    static constraints = {
        user nullable: false
        role nullable: false
    }

    static mapping = {
        id composite: ['user', 'role']
        role enumType: "string"
    }
}
