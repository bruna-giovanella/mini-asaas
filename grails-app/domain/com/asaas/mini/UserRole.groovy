package com.asaas.mini

class UserRole implements Serializable{

    User user

    Role role

    static constraints = {
        user nullable: false
        role nullable: false
    }

    static mapping = {
        id composite: ['user', 'role']
        version false
    }
}
