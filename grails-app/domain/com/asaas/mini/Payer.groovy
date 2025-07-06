package com.asaas.mini

import com.asaas.mini.utils.BaseEntity

class Payer extends BaseEntity{
    String name
    String email
    String contactNumber
    String cpfCnpj
    Address address
    Customer customer

    static belongsTo = [customer: Customer]

    static constraints = {
        name nullable: false, blank: false, maxSize: 255
        email nullable: false, blank: false, email: true, maxSize: 255
        contactNumber nullable: false, blank: false, matches: /^(1[1-9]|2[12478]|3[1234578]|4[12345789]|5[1345]|6[1]|7[134579]|8[1-9]|9[1-9])\d{8,9}$/
        cpfCnpj nullable: false, blank: false, matches: /\d{11}|\d{14}/
        address nullable: false
        customer nullable: false
    }
}
