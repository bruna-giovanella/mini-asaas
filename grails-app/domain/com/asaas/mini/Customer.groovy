package com.asaas.mini

import com.asaas.mini.utils.BaseEntity

class Customer extends BaseEntity{
    String name
    String email
    String cpfCnpj
    Address address

    static constraints = {
        name nullable: false, blank: false, maxSize: 255
        email nullable: false, blank: false, email: true, maxSize: 255,  unique: true
        cpfCnpj nullable: false, blank: false, matches: /\d{11}|\d{14}/, unique: true
        address nullable: false
    }
}
