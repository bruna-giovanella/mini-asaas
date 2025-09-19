package com.asaas.mini

import com.asaas.mini.auth.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMINISTRADOR','ROLE_VENDEDOR','ROLE_FINANCEIRO'])
class NotificationController {

    def springSecurityService
    NotificationService notificationService

    def recent(Integer max) {
        User user = springSecurityService.currentUser
        if (!user) {
            render status: 401, text: 'Usuário deslogado'
            return
        }
        max = Math.min(max ?: 10, 50)
        List<Notification> list = notificationService.listForUser(user, max)
        def payload = list.collect { notification ->
            [
                    id        : notification.id,
                    type      : notification.type?.name(),
                    message   : notification.message,
                    controller: notification.targetController,
                    action    : notification.targetAction,
                    targetId  : notification.targetId,
                    date      : notification.dateCreated,
                    read      : notification.read
            ]
        }
        render([notifications: payload, unreadCount: notificationService.unreadCount(user)] as JSON)
    }

    def goTo(Long id) {
        def user = springSecurityService.currentUser
        def notification = Notification.get(id)
        if (!notification || notification.receiver?.id != user?.id) {
            flash.message = "Ocorreu um problema inesperado"
            redirect(controller: 'customer', action: 'index')
            return
        }

        notification.read = true
        notification.save(flush: true)

        if (notification.targetController && notification.targetAction && notification.targetId) {
            redirect(controller: notification.targetController, action: notification.targetAction, id: notification.targetId)
        } else {
            redirect(controller: 'customer', action: 'index')
        }
    }
}
