package com.asaas.mini

import com.asaas.mini.auth.SecurityService
import com.asaas.mini.enums.NotificationType
import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class PaymentController {

    NotificationService notificationService
    PaymentService paymentService
    SecurityService securityService

    private Customer getCustomerLogged() {
        return securityService.getCurrentCustomer()
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def index() {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        List<Payment> paymentList = paymentService.list(customer)
        render(view: "index", model: [paymentList: paymentList, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def show(Long id) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Payment payment = paymentService.get(id, customer)

            if (!payment) {
                flash.message = "Cobrança não encontrada"
                redirect(action: "index")
                return
            }

            render(view: "show", model: [payment: payment])

        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            render(view: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def create() {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            List<Payer> payers = Payer.findAllByCustomerAndDeleted(customer, false)
            render(view: "create", model: [payers: payers, customer: customer])
        } catch (Exception exception) {
            flash.message = "Um erro inesperado aconteceu"
            render(view: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def save() {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }

        Long payerId = params.long("payer.id")
        Payer payer = Payer.findByIdAndCustomerAndDeleted(payerId, customer, false)
        if (!payer) {
            flash.message = "Pagador não encontrado"
            redirect(action: "index")
            return
        }

        try {
            Payment payment = paymentService.save(params, payer)
            flash.message = "Cobrança criada com sucesso"
            redirect(action: "show", id: payment.id)

            notificationService.createNotification(
                    securityService.getCurrentUser(),
                    NotificationType.PAYMENT_CREATED,
                    "Cobrança criada com sucesso",
                    "payment", "show", payment.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro de validação: ${validationException.errors.allErrors*.defaultMessage.join(', ')}"
            redirect(action: "create")
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao salvar cobrança"
            redirect(action: "create")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def edit (Long id) {
        Customer customer = getCustomerLogged()
        if (!customer) {
            flash.message = "Usuário não autenticado"
            redirect(controller: "login", action: "auth")
            return
        }
        
        Payment payment = paymentService.get(id, customer)
        if (!payment) {
            flash.message = "Cobrança não encontrada"
            redirect(action: "index")
            return
        }
        List<Payer> payers = Payer.findAllByCustomerAndDeleted(customer, false)
        render(view: "edit", model: [payment: payment, payers: payers, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def update() {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Long id = params.long("id")
            Payment payment = paymentService.update(id, params, customer)
            flash.message = "Cobrança atualizada com sucesso"
            redirect(action: "show", id: payment.id)

            notificationService.createNotification(
                    securityService.getCurrentUser(),
                    NotificationType.PAYMENT_UPDATED,
                    "Cobrança editada com sucesso",
                    "payment", "show", payment.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao atualizar cobrança"
            redirect(action: "edit", id: params.id)
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao atualizar cobrança"
            redirect(action: "edit", id: params.id)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete(Long id ) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }

            Payment payment = paymentService.get(id, customer)
            paymentService.delete(id, customer)

            notificationService.createNotification(
                    securityService.getCurrentUser(),
                    NotificationType.PAYMENT_DELETED,
                    "Cobrança deletada com sucesso",
                    "payment", "show", payment.id)

            flash.message = "Cobrança removida com sucesso"
            redirect(action: "index")

        } catch (Exception exception) {
            flash.message = "Erro ao deletar cobrança"
            redirect(action: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore(Long id) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }

            Payment payment = paymentService.get(id, customer)
            paymentService.restore(id, customer)

            notificationService.createNotification(
                    securityService.getCurrentUser(),
                    NotificationType.PAYMENT_RESTORED,
                    "Cobrança criada com sucesso",
                    "payment", "show", payment.id)

            flash.message = "Cobrança restaurada com sucesso"
            redirect(action: "index")

        } catch (Exception exception) {
            flash.message = "Erro ao restaurar cobrança"
            redirect(action: "index")
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def confirmInCash(Long id) {
        try {
            Customer customer = getCustomerLogged()
            if (!customer) {
                flash.message = "Usuário não autenticado"
                redirect(controller: "login", action: "auth")
                return
            }
            
            Payment payment = paymentService.confirmInCash(id, customer)

            notificationService.createNotification(
                    securityService.getCurrentUser(),
                    NotificationType.PAYMENT_RECEIVED,
                    "Cobrança recebida",
                    "payment", "show", payment.id)

            flash.message = "Cobrança recebida com sucesso"
            redirect(action: "index")

        } catch (Exception exception) {
            flash.message = "Erro ao confirmar recebimento de cobrança"
            redirect(action: "index")
        }
    }
}