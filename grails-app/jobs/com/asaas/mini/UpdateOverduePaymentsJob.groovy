package com.asaas.mini

import com.asaas.mini.PaymentService
import grails.gorm.transactions.Transactional

class UpdateOverduePaymentsJob {

    static triggers = {
        cron name: 'overduePaymentTrigger', cronExpression: "0 15 01 * * ?"
    }

    PaymentService paymentService

    @Transactional
    def execute() {
        println "Starting overdue payment job..."

        paymentService.markOverduePayments()

        println "Overdue payment job completed."
    }
}