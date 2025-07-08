package com.asaas.mini.utils

class BaseEntity {
    Date dateCreated

    Date lastUpdate

    Boolean deleted = false

    static mapping = {
        tablePerHierarchy false
    }
}
