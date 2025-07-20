package com.asaas.mini

import grails.plugins.mail.MailService

import javax.transaction.Transactional

@Transactional
class EmailService {

    MailService mailService

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

    public void sendPaymentCreatedNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        String htmlBody = """
            <html>
            <head>
                <style>${getStyle()}</style>
            </head>
            <body>
                <div class="container" role="main" aria-label="Cobrança criada">
                    <h1>Cobrança Criada ✅</h1>
                    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
                    <p>Uma nova cobrança no valor de <strong>R\$ ${payment.value}</strong> foi criada com vencimento para <strong>${payment.dueDate}</strong>.</p>
                    <p>Fique à vontade para entrar em contato caso tenha dúvidas ou precise de ajuda.</p>
                    <div class="signature">
                        Com carinho,<br>
                        <strong>Equipe MiniAsaas</strong><br>
                        <a href="https://www.miniasaas.com.br" target="_blank" rel="noopener noreferrer">www.miniasaas.com.br</a><br>
                        <em>Mini Asinhas te da asonas ✨</em>
                    </div>
                </div>
            </body>
            </html>
        """

        mailService.sendMail {
            to payerEmail
            subject "Cobrança criada"
            html htmlBody
        }
    }

    public void sendPaymentPaidNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        String htmlBody = """
            <html>
            <head>
                <style>${getStyle()}</style>
            </head>
            <body>
                <div class="container" role="main" aria-label="Cobrança paga">
                    <h1>Cobrança Paga 🎉</h1>
                    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
                    <p>Recebemos o pagamento no valor de <strong>R\$ ${payment.value}</strong>. Muito obrigada pela confiança!</p>
                    <p>Estamos sempre aqui para ajudar você.</p>
                    <div class="signature">
                        Com carinho,<br>
                        <strong>Equipe MiniAsaas</strong><br>
                        <a href="https://www.miniasaas.com.br" target="_blank" rel="noopener noreferrer">www.miniasaas.com.br</a><br>
                        <em>Mini Asinhas te da asonas ✨</em>
                    </div>
                </div>
            </body>
            </html>
        """

        mailService.sendMail {
            to payerEmail
            subject "Cobrança paga"
            html htmlBody
        }
    }

    public void sendPaymentDeletedNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        String htmlBody = """
            <html>
            <head>
                <style>${getStyle()}</style>
            </head>
            <body>
                <div class="container" role="main" aria-label="Cobrança excluída">
                    <h1>Cobrança Excluída ❌</h1>
                    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
                    <p>A cobrança no valor de <strong>R\$ ${payment.value}</strong>, com vencimento para <strong>${payment.dueDate}</strong>, foi excluída conforme solicitado.</p>
                    <p>Se precisar de qualquer assistência, estamos à disposição.</p>
                    <div class="signature">
                        Com carinho,<br>
                        <strong>Equipe MiniAsaas</strong><br>
                        <a href="https://www.miniasaas.com.br" target="_blank" rel="noopener noreferrer">www.miniasaas.com.br</a><br>
                        <em>Mini Asinhas te da asonas ✨</em>
                    </div>
                </div>
            </body>
            </html>
        """

        mailService.sendMail {
            to payerEmail
            subject "Cobrança excluída"
            html htmlBody
        }
    }

    public void sendPaymentExpiredNotification(Payment payment) {
        def payerEmail = payment.payer?.email
        if (!payerEmail) {
            log.warn("Payer without email, cannot send notification.")
            return
        }

        String htmlBody = """
            <html>
            <head>
                <style>${getStyle()}</style>
            </head>
            <body>
                <div class="container" role="main" aria-label="Cobrança vencida">
                    <h1>Cobrança Vencida ⏰</h1>
                    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
                    <p>A cobrança no valor de <strong>R\$ ${payment.value}</strong>, com vencimento em <strong>${payment.dueDate}</strong>, está vencida.</p>
                    <p>Por favor, entre em contato para regularizar e evitar transtornos.</p>
                    <div class="signature">
                        Com carinho,<br>
                        <strong>Equipe MiniAsaas</strong><br>
                        <a href="https://www.miniasaas.com.br" target="_blank" rel="noopener noreferrer">www.miniasaas.com.br</a><br>
                        <em>Mini Asinhas te da asonas ✨</em>
                    </div>
                </div>
            </body>
            </html>
        """

        mailService.sendMail {
            to payerEmail
            subject "Cobrança vencida"
            html htmlBody
        }
    }
}
