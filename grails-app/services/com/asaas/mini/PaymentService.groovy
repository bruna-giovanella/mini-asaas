package com.asaas.mini

import com.asaas.mini.enums.PaymentType
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Transactional
class PaymentService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Payment save(Map params, Payer payer) {
        Payment payment = validateSave(params, payer)

        if (payment.hasErrors()) {
            throw new ValidationException("Erro ao criar pagamento", payment.errors)
        }

        payment.value = new BigDecimal(params.value)
        payment.type = PaymentType.valueOf(params.type.toUpperCase())
        payment.dueDate = LocalDate.parse(params.dueDate, DATE_FORMATTER)
        payment.payer = payer

        payment.save(flush: true, failOnError: true)
    }

    private Payment validateSave(Map params, Payer payer) {
        Payment payment = new Payment()

        if (!payer) {
            payment.errors.rejectValue("payer", "payer.invalid", "Pagador não encontrado ou não pertence ao cliente logado")
            return payment
        }

        try {
            BigDecimal value = new BigDecimal(params.value)
            if (value <= 0) {
                payment.errors.rejectValue("value", "value.invalid", "O valor deve ser positivo")
            }
        } catch (Exception exception) {
            payment.errors.rejectValue("value", "value.invalid", "Valor inválido")
        }

        try {
            PaymentType.valueOf(params.type.toUpperCase())
        } catch (Exception exception) {
            payment.errors.rejectValue("type", "type.invalid", "Tipo de pagamento inválido")
        }

        try {
            LocalDate.parse(params.dueDate, DATE_FORMATTER)
        } catch (DateTimeParseException exception) {
            payment.errors.rejectValue("dueDate", "dueDate.invalid", "Formato de data de vencimento inválido. Formato esperado: dd/MM/yyyy")
        }

        return payment
    }

    public Payment get(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Payment.findByIdAndDeleted(id, false)?.with { payment ->
            if (payment.payer.customer.id == customer.id) {
                return payment
            } else {
                throw new IllegalArgumentException("Payment not found")
            }
        }
    }
}