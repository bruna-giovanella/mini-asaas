package com.asaas.mini

import grails.gsp.PageRenderer
import grails.plugins.mail.MailService

import javax.transaction.Transactional

@Transactional
class EmailService {

    MailService mailService
    PageRenderer groovyPageRenderer

    private String getStyle() {
        return """
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            padding: 40px;
            border-radius: 12px;
            background-color: #ffffff;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
            color: #333333;
        }

        h1 {
            color: #2b2d42;
            font-size: 24px;
            margin-bottom: 20px;
            border-bottom: 2px solid #edf2f7;
            padding-bottom: 10px;
        }

        p {
            font-size: 16px;
            color: #4a4a4a;
            line-height: 1.6;
            margin-bottom: 16px;
        }

        .highlight {
            color: #0077cc;
            font-weight: 600;
        }

        .footer {
            margin-top: 40px;
            font-size: 13px;
            color: #9fa6b2;
            border-top: 1px solid #e5e7eb;
            padding-top: 20px;
            text-align: center;
        }

        .signature {
            margin-top: 30px;
            font-size: 15px;
            color: #6b7280;
        }

        a {
            color: #0077cc;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }
    """
    }

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