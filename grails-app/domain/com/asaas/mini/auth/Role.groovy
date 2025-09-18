package com.asaas.mini.auth

import com.asaas.mini.utils.BaseEntity
import org.springframework.security.core.GrantedAuthority

class Role extends BaseEntity implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = 1

    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }

    @Override
    String getAuthority() {
        return authority
    }
}
