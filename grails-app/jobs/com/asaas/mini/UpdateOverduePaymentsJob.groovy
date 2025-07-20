package mini.asaas

import com.asaas.mini.PaymentService
import grails.gorm.transactions.Transactional

class UpdateOverduePaymentsJob {

    static triggers = {
        cron name: 'overduePaymentTrigger', cronExpression: "0 45 11 * * ?"
    }

    PaymentService paymentService

    @Transactional
    def execute() {
        log.info "Starting overdue payment job..."

        paymentService.markOverduePayments()

        log.info "Overdue payment job completed."
    }
}