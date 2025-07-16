package com.asaas.mini

import com.asaas.mini.enums.PaymentStatus
import com.asaas.mini.enums.PaymentType
import com.asaas.mini.utils.BaseEntity

import java.time.LocalDate

class Payment extends BaseEntity {

    Payer payer

    BigDecimal value

    PaymentType type

    PaymentStatus status = PaymentStatus.AGUARDANDO_PAGAMENTO

    LocalDate dueDate

    boolean confirmedInCash = false

    static constraints = {
        payer nullable: false
        value nullable: false, min: new BigDecimal("0.01")
        type nullable: false
        status nullable: false
        dueDate nullable: false
    }

}
