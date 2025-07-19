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

    EmailService emailService

    public Payment save(Map params, Payer payer) {
        Payment payment = validateSave(params, payer)

        if (payment.hasErrors()) {
            throw new ValidationException("Error creating payment", payment.errors)
        }

        payment.value = new BigDecimal(params.value)
        payment.type = PaymentType.valueOf(params.type.toUpperCase())
        payment.payer = payer

        switch (payment.type) {
            case PaymentType.PIX:
                payment.dueDate = LocalDate.now().plusDays(1)
                break
            case PaymentType.CARTAO:
            case PaymentType.BOLETO:
                payment.dueDate = LocalDate.now().plusDays(30)
                break
        }

        payment.save(flush: true, failOnError: true)
        emailService.sendPaymentCreatedNotification(payment)
        return payment
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

    public Payment update(Long id, Map params, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Payment payment = Payment.createCriteria().get {
            eq("id", id)
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
        }

        if (!payment) {
            throw new IllegalArgumentException("Payment not found")
        }

        validateUpdate(payment, params)

        payment.value = new BigDecimal(params.value)
        payment.type = PaymentType.valueOf(params.type.toUpperCase())

        switch (payment.type) {
            case PaymentType.PIX:
                payment.dueDate = LocalDate.now().plusDays(1)
                break
            case PaymentType.CARTAO:
            case PaymentType.BOLETO:
                payment.dueDate = LocalDate.now().plusDays(30)
                break
        }

        payment.save(flush: true, failOnError: true)
        emailService.sendPaymentCreatedNotification(payment)
        return payment
    }

    private void validateUpdate(Payment payment, Map params) {
        if (payment.status != PaymentStatus.AGUARDANDO_PAGAMENTO) {
            payment.errors.rejectValue("status", "status.invalid", "Only pending payments can be updated")
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

        if (payment.hasErrors()) {
            throw new ValidationException("Error updating payment", payment.errors)
        }
    }

    public Payment delete(Long id, Customer customer) {
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

        emailService.sendPaymentDeletedNotification(payment)
        return payment
    }

    public Payment restore(Long id, Customer customer) {
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

        emailService.sendPaymentCreatedNotification(payment)
        return payment
    }

    public Payment confirmInCash(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("Payment ID is required")
        }

        Payment payment = Payment.findById(id)
        if (!payment || payment.status == PaymentStatus.EXCLUIDA) {
            throw new IllegalArgumentException("Payment not found")
        }

        if (payment.payer.customer.id != customer.id) {
            throw new SecurityException("Access denied: Payment does not belong to the logged customer")
        }

        if (payment.status != PaymentStatus.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Only payments with status 'Aguardando Pagamento' can be confirmed in cash")
        }

        payment.status = PaymentStatus.RECEBIDA
        payment.confirmedInCash = true
        payment.save(flush: true)

        emailService.sendPaymentPaidNotification(payment)
        return payment
    }

    public void markOverduePayments() {
        LocalDate today = LocalDate.now()

        List<Payment> paymentsToMarkOverdue = Payment.where {
            status == PaymentStatus.AGUARDANDO_PAGAMENTO &&
                    dueDate < today &&
                    deleted == false
        }.list()

        paymentsToMarkOverdue.each { payment ->
            payment.status = PaymentStatus.VENCIDA
            payment.save(flush: true)
            emailService.sendPaymentExpiredNotification(payment)
        }
    }
}