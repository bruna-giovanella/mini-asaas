package com.asaas.mini.auth

class Role implements Serializable {
    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
