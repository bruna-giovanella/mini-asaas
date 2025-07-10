package com.asaas.mini.utils

class BaseEntity {
    Date dateCreated

    Date lastUpdated

    Boolean deleted = false

    static constraints = {
        lastUpdated nullable: true
    }

    static mapping = {
        deleted column: 'deleted', sqlType: 'tinyint(1)', defaultValue: 0
        tablePerHierarchy false
    }
}