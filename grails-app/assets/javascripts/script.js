// Máscara para valores monetários no formato brasileiro (9999,99)
function formatCurrency(input) {
    let value = input.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    
    if (value === '') {
        input.value = '';
        return;
    }
    
    // Adiciona vírgula para centavos (sempre 2 dígitos)
    if (value.length === 1) {
        input.value = '0,0' + value;
    } else if (value.length === 2) {
        input.value = '0,' + value;
    } else {
        // Adiciona pontos para milhares e vírgula para centavos
        const integerPart = value.slice(0, -2);
        const decimalPart = value.slice(-2);
        
        // Remove zeros à esquerda da parte inteira, exceto se for apenas zero
        const cleanIntegerPart = integerPart.replace(/^0+/, '') || '0';
        
        // Formata a parte inteira com pontos para milhares
        const formattedInteger = cleanIntegerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        
        input.value = formattedInteger + ',' + decimalPart;
    }
}

// Função para converter valor brasileiro para formato americano antes do envio
function convertToAmericanFormat(input) {
    let value = input.value.replace(/\./g, '').replace(',', '.');
    input.value = value;
}

// Aplicar máscara quando a página carregar
document.addEventListener('DOMContentLoaded', function() {
    // Encontrar todos os campos de valor
    const valueInputs = document.querySelectorAll('input[name="value"]');
    
    valueInputs.forEach(function(input) {
        // Aplicar máscara enquanto digita
        input.addEventListener('input', function() {
            formatCurrency(this);
        });
        
        // Converter para formato americano antes do envio do formulário
        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function() {
                convertToAmericanFormat(input);
            });
        }
    });
});
