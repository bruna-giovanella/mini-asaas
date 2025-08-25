package com.asaas.mini

import grails.plugin.springsecurity.annotation.Secured
import org.grails.datastore.mapping.validation.ValidationException

class PayerController {

    PayerService payerService

    private Customer getCustomerLogged() {
        return Customer.get(7L)
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def index() {
        Customer customer = getCustomerLogged()
        List<Payer> payerList = payerService.list(customer);
        render(view: "index", model: [payerList: payerList, customer: customer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def show(Long id) {
        Customer customer = getCustomerLogged()
        Payer payer = payerService.get(id, customer)

        if (!payer) {
            flash.message = "Pagador não encontrado"
            redirect(action: "index")
            return
        }
        render(view: "show", model: [payer: payer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def create() {
        render(view: "create")
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO', 'ROLE_VENDEDOR'])
    def save() {
        try {
            Customer customer = getCustomerLogged()
            Payer payer = payerService.save(params, customer)
            flash.message = "Pagador criado com sucesso"
            redirect(action: "show", id: payer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao criar pagador"
            render(view: "create", model: [payer: params])
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao criar pagador"
            render(view: "create", model: [payer: params])
        }
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def edit(Long id) {
        Customer customer = getCustomerLogged()
        Payer payer = payerService.get(id, customer)
        if (!payer) {
            flash.message = "Pagador não encontrado"
            redirect(action: "index")
            return
        }
        render(view: "edit", model: [payer: payer])
    }

    @Secured(['ROLE_ADMINISTRADOR', 'ROLE_FINANCEIRO'])
    def update() {
        try {
            Long id = params.long("id")
            Payer payer = payerService.update(id, params)
            flash.message = "Pagador atualizado com sucesso"
            redirect(action: "show", id: payer.id)

        } catch (ValidationException validationException) {
            flash.message = "Erro ao atualizar pagador"
            redirect(action: "edit", id: params.id)
        } catch (Exception exception) {
            flash.message = "Erro inesperado ao atualizar pagador"
            redirect(action: "edit", id: params.id)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def delete(Long id) {
        try {
            Customer customer = getCustomerLogged()
            payerService.delete(id, customer)
            flash.message = "Pagador removido com sucesso"
            redirect(action: "index")

        } catch (Exception exception) {
            flash.message = "Erro ao excluir pagador"
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_ADMINISTRADOR'])
    def restore(Long id) {
        try {
            Customer customer = getCustomerLogged()
            payerService.restore(id, customer)
            flash.message = "Pagador restaurado com sucesso"
            redirect(action: "index")

        } catch (Exception exception) {
            flash.message = "Erro ao restaurar pagador"
            redirect(action: "index")
        }
    }
}