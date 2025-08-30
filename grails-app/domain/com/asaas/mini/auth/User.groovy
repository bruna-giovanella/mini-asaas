package com.asaas.mini.auth

import com.asaas.mini.Customer
import com.asaas.mini.utils.BaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User extends BaseEntity implements UserDetails {
    String username

    String password

    boolean enabled = true

    boolean accountExpired

    boolean accountLocked

    boolean passwordExpired

    Customer customer

    static constraints = {
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return UserRole.findAllByUser(this)*.role as Collection<GrantedAuthority>
    }

    @Override
    String getPassword() {
        return password
    }

    @Override
    String getUsername() {
        return username
    }

    @Override
    boolean isAccountNonExpired() {
        return !accountExpired
    }

    @Override
    boolean isAccountNonLocked() {
        return !accountLocked
    }

    @Override
    boolean isCredentialsNonExpired() {
        return !passwordExpired
    }

    @Override
    boolean isEnabled() {
        return enabled && !deleted
    }
}
