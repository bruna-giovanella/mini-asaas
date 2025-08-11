package com.asaas.mini.auth

import com.asaas.mini.utils.BaseEntity

class Role extends BaseEntity implements Serializable {
    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
