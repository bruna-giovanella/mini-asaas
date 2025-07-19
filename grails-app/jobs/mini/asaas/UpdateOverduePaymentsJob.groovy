package mini.asaas

import grails.gorm.transactions.Transactional

class UpdateOverduePaymentsJob {

    static triggers = {
        cron name: 'overduePaymentTrigger', cronExpression: "0 15 1 * * ?"
    }

    def paymentService

    @Transactional
    def execute() {
        log.info "Starting overdue payment job..."

        paymentService.markOverduePayments()

        log.info "Overdue payment job completed."
    }
}