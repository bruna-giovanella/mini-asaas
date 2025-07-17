package com.asaas.mini

import com.asaas.mini.enums.PaymentType
import com.asaas.mini.enums.PaymentStatus
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
            throw new ValidationException("Error creating payment", payment.errors)
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
            payment.errors.rejectValue("payer", "payer.invalid", "Payer not found or does not belong to the logged-in customer")
            return payment
        }

        try {
            BigDecimal value = new BigDecimal(params.value)
            if (value <= 0) {
                payment.errors.rejectValue("value", "value.invalid", "Value must be positive")
            }
        } catch (Exception e) {
            payment.errors.rejectValue("value", "value.invalid", "Invalid value")
        }

        try {
            PaymentType.valueOf(params.type.toUpperCase())
        } catch (Exception e) {
            payment.errors.rejectValue("type", "type.invalid", "Invalid payment type")
        }

        try {
            LocalDate.parse(params.dueDate, DATE_FORMATTER)
        } catch (DateTimeParseException e) {
            payment.errors.rejectValue("dueDate", "dueDate.invalid", "Invalid due date format. Expected format: dd/MM/yyyy")
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

    public List<Payment> list(Customer customer) {
        return Payment.createCriteria().list {
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
        }
    }

    public void delete(Long id, Customer customer) {
        Payment payment = Payment.createCriteria().get {
            eq("id", id)
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
        }

        if (!payment) {
            throw new IllegalArgumentException("Payment not found for this customer")
        }

        if (payment.status in [PaymentStatus.RECEBIDA, PaymentStatus.EXCLUIDA]) {
            throw new ValidationException("Cannot delete payment with status ${payment.status}", payment.errors)
        }

        payment.status = PaymentStatus.EXCLUIDA
        payment.deleted = true
        payment.save(failOnError: true)
    }

    public void restore(Long id, Customer customer) {
        Payment payment = Payment.createCriteria().get {
            eq("id", id)
            eq("deleted", true)
            payer {
                eq("customer", customer)
            }
        }

        if (!payment) {
            throw new IllegalArgumentException("Payment not found for this customer")
        }

        if (payment.status != PaymentStatus.EXCLUIDA) {
            throw new ValidationException("Only deleted payments can be restored", payment.errors)
        }

        if (payment.dueDate.isBefore(LocalDate.now())) {
            payment.status = PaymentStatus.VENCIDA
        } else {
            payment.status = PaymentStatus.AGUARDANDO_PAGAMENTO
        }

        payment.deleted = false
        payment.save(failOnError: true)
    }
}