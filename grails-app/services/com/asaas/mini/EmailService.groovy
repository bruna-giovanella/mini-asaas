package com.asaas.mini

import grails.gsp.PageRenderer
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

import javax.mail.internet.MimeMessage
import javax.transaction.Transactional

@Service
@Transactional
class EmailService {

    JavaMailSender mailSender
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

    private void sendEmail(String recipientEmail, String emailSubject, String templatePath, Payment payment) {
        if (!recipientEmail) {
            log.warn("Pagador não possui email cadastrado")
            return
        }

        try {
            String htmlBody = groovyPageRenderer.render(
                    template: templatePath,
                    model: [payment: payment]
            )

            MimeMessage message = mailSender.createMimeMessage()
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8")

            helper.setTo(recipientEmail)
            helper.setSubject(emailSubject)
            helper.setText(htmlBody, true)

            mailSender.send(message)
        } catch (Exception e) {
            log.error("Erro ao enviar email para $recipientEmail: ${e.message}", e)
        }
    }
}
