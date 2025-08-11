package com.asaas.mini.utils

import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity {
    Date dateCreated

    Date lastUpdated

    Boolean deleted = false

    static constraints = {
        lastUpdated nullable: true
        deleted nullable: false
    }

    def beforeInsert() {
        if (deleted == null) {
            deleted = false
        }
        if (!dateCreated) {
            dateCreated = new Date()
        }
    }

    def beforeUpdate() {
        lastUpdated = new Date()
    }

    static mapping = {
        deleted column: 'deleted', sqlType: 'tinyint(1)', defaultValue: 0
        tablePerHierarchy false
    }
}