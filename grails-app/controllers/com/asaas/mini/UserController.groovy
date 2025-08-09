package com.asaas.mini

class UserController {

    private Customer getCustomerLogged() {
        User user = springSecurityService.currentUser as User
        return user?.customer
    }



}
