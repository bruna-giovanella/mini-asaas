<html>
<head>
    <g:render template="emails/style"/>
</head>
<body>
    <div class="container" role="main" aria-label="Cobrança criada">
    <h1>Cobrança Criada ✅</h1>
    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
    <p>Uma nova cobrança no valor de <strong>R$ ${payment.value}</strong> foi criada com vencimento para <strong>${payment.dueDate}</strong>.</p>
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