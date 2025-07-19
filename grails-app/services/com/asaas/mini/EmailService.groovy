package com.asaas.mini

import grails.plugins.mail.MailService

import javax.transaction.Transactional

@Transactional
class EmailService {

    MailService mailService

    public void sendPaymentCreatedNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        mailService.sendMail {
            to payerEmail
            subject "Nova cobrança gerada"
            body
        }
    }

    public void sendPaymentPaidNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        mailService.sendMail {
            to payerEmail
            subject "Cobrança paga"
            body
        }
    }

    public void sendPaymentDeletedNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        mailService.sendMail {
            to payerEmail
            subject "Cobrança excluída"
            body
        }
    }

    public void sendPaymentExpiredNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        mailService.sendMail {
            to payerEmail
            subject "Cobrança vencida"
            body
        }
    }
}
