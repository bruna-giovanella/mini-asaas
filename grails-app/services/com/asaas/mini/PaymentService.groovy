package com.asaas.mini

import com.asaas.mini.enums.PaymentType
import com.asaas.mini.enums.PaymentStatus
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException
import java.time.LocalDate

@Transactional
class PaymentService {

    EmailService emailService

    public Payment save(Map params, Payer payer) {
        Payment payment = validateParams(params, payer)

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

    public Payment get(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID é obrigatório")
        }

        Long idParam = id

        Payment payment = Payment.where {
            id == idParam &&
                    payer.customer == customer
        }.get()

        if (!payment) {
            throw new IllegalArgumentException("Pagamento não encontrado para essa conta")
        }

        return payment
    }

    public List<Payment> list(Customer customer) {
        return Payment.createCriteria().list {
            payer {
                eq("customer", customer)
            }
        }
    }

    public Map listPaginated(Customer customer, int max, int offset, String sortField, String sortOrder) {
        def validSortFields = ['value', 'dueDate', 'status', 'type', 'dateCreated', 'lastUpdated', 'deleted']
        if (!validSortFields.contains(sortField)) {
            sortField = 'dueDate'
        }
        
        def validOrders = ['asc', 'desc']
        if (!validOrders.contains(sortOrder)) {
            sortOrder = 'desc'
        }
        
        int totalCount = Payment.createCriteria().count {
            payer {
                eq('customer', customer)
            }
        }
        
        def criteria = Payment.createCriteria()
        List<Payment> paymentList = criteria.list {
            payer {
                eq('customer', customer)
            }
            order(sortField, sortOrder)
            maxResults(max)
            firstResult(offset)
        }
        
        return [
            paymentList: paymentList,
            totalCount: totalCount
        ]
    }

    public Payment update(Long id, Map params, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID é obrigatório")
        }

        Payment payment = Payment.createCriteria().get {
            eq("id", id)
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
        }

        if (!payment) {
            throw new IllegalArgumentException("Pagamento não encontrado")
        }

        validateParams(params, null, payment)

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

    private Payment validateParams(Map params, Payer payer = null, Payment payment = null) {
        Payment tempPayment = new Payment()

        if (payment && payment.status != PaymentStatus.AGUARDANDO_PAGAMENTO) {
            tempPayment.errors.rejectValue("status", "status.invalid", "Somente pagamentos pendentes podem ser atualizados")
        }

        if (!payment && !payer) {
            tempPayment.errors.rejectValue("payer", "payer.invalid", "Pagador não encontrado")
        }

        try {
            BigDecimal value = new BigDecimal(params.value)
            if (value <= 0) {
                tempPayment.errors.rejectValue("value", "value.invalid", "O valor deve ser positivo")
            }
        } catch (Exception exception) {
            tempPayment.errors.rejectValue("value", "value.invalid", "Valor inválido")
        }

        try {
            PaymentType.valueOf(params.type.toUpperCase())
        } catch (Exception exception) {
            tempPayment.errors.rejectValue("type", "type.invalid", "Tipo de pagamento inválido")
        }

        if (tempPayment.hasErrors()) {
            throw new ValidationException("Erro nos dados passados", tempPayment.errors)
        }

        if (payment != null) {
            return payment
        }
        return new Payment()    }

    public Payment delete(Long id, Customer customer) {
        Payment payment = Payment.createCriteria().get {
            eq("id", id)
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
        }

        if (!payment) {
            throw new IllegalArgumentException("Pagamento não encontrado para essa conta")
        }

        if (payment.status == PaymentStatus.RECEBIDA) {
            throw new ValidationException("Não é possível excluir pagamentos recebidos", payment.errors)
        }

        if (payment.deleted) {
            throw new ValidationException("Pagamento já está excluído", payment.errors)
        }

        payment.deleted = true
        payment.markDirty('deleted')
        payment.save(failOnError:true)

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
            throw new IllegalArgumentException("Pagamento não encontrado")
        }

        if (Payment.findByIdAndDeleted(id, false)) {
            throw new ValidationException("Apenas pagamentos deletados podem ser restaurados", payment.errors)
        }

        if (payment.dueDate.isBefore(LocalDate.now())) {
            payment.status = PaymentStatus.VENCIDA
        } else {
            payment.status = PaymentStatus.AGUARDANDO_PAGAMENTO
        }

        payment.deleted = false
        payment.markDirty('deleted')
        payment.save(failOnError: true)

        emailService.sendPaymentCreatedNotification(payment)
        return payment
    }

    public Payment confirmInCash(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID é obrigatório")
        }

        Payment payment = Payment.findById(id)
        if (payment.payer.customer.id != customer.id) {
            throw new SecurityException("Pagamento não encontrado")
        }

        if (!payment || payment.deleted == true) {
            throw new IllegalArgumentException("Pagamento não encontrado")
        }

        if (payment.status != PaymentStatus.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Apenas pagamentos 'aguardando pagamento' podem ser confirmadas em dinheiro")
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

    public BigDecimal totalReceived(Customer customer) {
        List<Payment> paymentsReceived = Payment.createCriteria().list {
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
            eq("status", PaymentStatus.RECEBIDA)
        }

        return (paymentsReceived.sum {it.value} ?: 0) as BigDecimal
    }

    public int totalPayersWithPaymentsReceived(Customer customer) {
        List<Payer> payers = Payer.findAllByCustomer(customer)

        int count = (int) payers.count { payer ->
            List<Payment> payments = Payment.findAllByPayer(payer)
            payments && payments.every { it.status == PaymentStatus.RECEBIDA }
        }

       return count
    }

    public int totalPaymentsWithPaymentsReceived(Customer customer) {
        List<Payment> payments = Payment.findAllByCustomer(customer)

        int count = (int) payments.count { payment ->
            payment.status == PaymentStatus.RECEBIDA
        }

        return count
    }

    public BigDecimal totalOverdue(Customer customer) {
        List<Payment> overduePayments = Payment.createCriteria().list {
            eq("deleted", false)
            payer {
                eq("customer", customer)
            }
            eq("status", PaymentStatus.VENCIDA)
        }

        return (overduePayments.sum { it.value } ?: 0) as BigDecimal
    }

    public int totalPaymentsOverdue(Customer customer) {
        List<Payment> payments = Payment.findAllByCustomer(customer)
        int count = (int) payments.count { payment ->
            payment.status == PaymentStatus.VENCIDA
        }
        return count
    }

    public int totalPayersWithPaymentsOverdue(Customer customer) {
        List<Payer> payers = Payer.findAllByCustomer(customer)

        int count = (int) payers.count { payer ->
            List<Payment> payments = Payment.findAllByPayer(payer)
            payments.any { it.status == PaymentStatus.VENCIDA }
        }

        return count
    }

}