package com.asaas.mini

import grails.gsp.PageRenderer
import grails.plugins.mail.MailService

import javax.transaction.Transactional

@Transactional
class EmailService {

    MailService mailService
    PageRenderer groovyPageRenderer

    void sendPaymentCreatedNotification(Payment payment) {
        sendEmail(payment.payer.email, "Cobrança criada", "/emails/paymentCreated", payment)
    }

    void sendPaymentPaidNotification(Payment payment) {
        sendEmail(payment.payer.email, "Cobrança paga", "/emails/paymentPaid", payment)
    }

    void sendPaymentDeletedNotification(Payment payment) {
        sendEmail(payment.payer.email, "Cobrança excluída", "/emails/paymentDeleted", payment)
    }

    void sendPaymentExpiredNotification(Payment payment) {
        sendEmail(payment.payer.email, "Cobrança vencida", "/emails/paymentExpired", payment)
    }

    private void sendEmail(String to, String subject, String templatePath, Payment payment) {
        if (!to) {
            log.warn("Pagador não possui email cadastrado")
            return
        }

        String htmlBody = groovyPageRenderer.render(
                template: templatePath,
                model: [payment: payment]
        )

        mailService.sendMail {
            to to
            subject subject
            html htmlBody
        }
    }
}