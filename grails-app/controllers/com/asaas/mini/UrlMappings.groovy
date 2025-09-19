package com.asaas.mini

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
            }
        }

        "/"(controller: "customer", action: "index")
        "/login"(controller: "login", action: "index")
        "/logout"(controller: "login", action: "logout")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
