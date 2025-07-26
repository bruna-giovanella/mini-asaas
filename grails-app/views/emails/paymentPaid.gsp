<html>
<head>
    <g:render template="/emails/style"/>
</head>
<body>
<div class="container" role="main" aria-label="Cobrança paga">
    <h1>Cobrança Paga 🎉</h1>
    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
    <p>Recebemos o pagamento no valor de <strong>R$ ${payment.value}</strong>. Muito obrigada pela confiança!</p>
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
