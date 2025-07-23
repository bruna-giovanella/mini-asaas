package com.asaas.mini

class   Address {
    String cep

    String city

    String state

    String complement

    static constraints = {
        cep nullable: false, blank: false, matches: /^\d{5}-?\d{3}$/
        city nullable: false, blank: false, maxSize: 200
        state nullable: false, blank: false, maxSize: 200
    }

}
