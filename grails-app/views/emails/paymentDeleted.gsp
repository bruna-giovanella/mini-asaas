<html>
<head>
    <g:render template="/emails/style"/>
</head>
<body>
<div class="container" role="main" aria-label="Cobrança excluída">
    <h1>Cobrança Excluída ❌</h1>
    <p>Olá <span class="highlight">${payment.payer.name}</span>,</p>
    <p>A cobrança no valor de <strong>R$ ${payment.value}</strong>, com vencimento para <strong>${payment.dueDate}</strong>, foi excluída conforme solicitado.</p>
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
