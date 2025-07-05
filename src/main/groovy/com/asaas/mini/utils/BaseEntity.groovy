package com.asaas.mini.utils

class BaseEntity {
    Date dateCreated
    Date lastUpdated
    Boolean deleted = false

    static mapping = {
        deleted column: 'deleted', sqlType: 'tinyint(1)', defaultValue: 0
        tablePerHierarchy false
    }

    void softDelete() {
        this.deleted = true
        this.markDirty('deleted')
        this.save(flush:true, failOnError:true)
    }

    void restore() {
        this.deleted = false
        this.markDirty('deleted')
        this.save(flush: true, failOnError: true)
    }

}