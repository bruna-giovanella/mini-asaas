package com.asaas.mini

import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class PayerService {

    public Payer save(Map params, Customer customer) {
        Payer payerValidate = validateParams(params, customer)

        if (payerValidate.hasErrors()) {
            throw new ValidationException("Erro ao criar pagador: ", payerValidate.errors)
        }

        Address address = new Address()
        address.cep = params.cep
        address.city = params.city
        address.state = params.state
        address.complement = params.complement

        Payer payer = new Payer()
        payer.name = params.name
        payer.email = params.email
        payer.contactNumber = params.contactNumber
        payer.cpfCnpj = params.cpfCnpj
        payer.address = address
        payer.customer = customer

        return payer.save(flush: true, failOnError: true)
    }

    public Payer get(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }
        Payer payer = Payer.findByIdAndCustomerAndDeleted(id, customer, false)

        return payer
    }

    public List<Payer> list(Customer customer) {
        return Payer.findAllByCustomerAndDeleted(customer, false)
    }

    public Payer update(Long id, Map params) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        Payer payer = Payer.findByIdAndDeleted(id, false)

        if (!payer) {
            throw new IllegalArgumentException("Pagador não encontrado")
        }

        Payer validatedPayer = validateParams(params, payer.customer, id)

        if (validatedPayer.hasErrors()) {
            throw new ValidationException("Erro ao atualizar pagador", validatedPayer.errors)
        }

        payer.name = params.name
        payer.email = params.email
        payer.contactNumber = params.contactNumber
        payer.cpfCnpj = params.cpfCnpj

        payer.address.cep = params.cep
        payer.address.city = params.city
        payer.address.state = params.state
        payer.address.complement = params.complement

        return payer.save(flush: true, failOnError: true)
    }

    private Payer validateParams(Map params, Customer customer, Long id = null) {
        Payer payer = new Payer()

        if (!customer || customer.deleted) {
            payer.errors.rejectValue("customer", "customer.deleted", "O cliente está excluído e não pode ter pagadores")
            return payer
        }

        Payer existingCpfCnpj = Payer.where {
            cpfCnpj == params.cpfCnpj
            if (id) {
                ne 'id', id
            }
        }.get()

        if (existingCpfCnpj) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.exists", "Já existe um pagador com este CPF/CNPJ para este cliente")
        }

        Payer existingEmail = Payer.where {
            email == params.email
            if (id) {
                ne 'id', id
            }
        }.get()

        if (existingEmail) {
            payer.errors.rejectValue("email", "email.exists", "Já existe um pagador com este e-mail para este cliente")
        }

        if (!params.name?.trim()) {
            payer.errors.rejectValue("name", "name.blank", "O nome é obrigatório")
        } else if (params.name.length() > 255) {
            payer.errors.rejectValue("name", "name.maxSize", "O nome deve ter no máximo 255 caracteres")
        }

        if (!params.email?.trim()) {
            payer.errors.rejectValue("email", "email.blank", "O e-mail é obrigatório")
        } else if (params.email.length() > 255) {
            payer.errors.rejectValue("email", "email.maxSize", "O e-mail deve ter no máximo 255 caracteres")
        } else if (!(params.email ==~ /^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            payer.errors.rejectValue("email", "email.invalid", "E-mail inválido")
        }

        if (!params.contactNumber?.trim()) {
            payer.errors.rejectValue("contactNumber", "contactNumber.blank", "O número de contato é obrigatório")
        } else if (!(params.contactNumber ==~ /^(1[1-9]|2[12478]|3[1234578]|4[12345789]|5[1345]|6[1]|7[134579]|8[1-9]|9[1-9])\d{8,9}$/)) {
            payer.errors.rejectValue("contactNumber", "contactNumber.invalidFormat", "Número de contato inválido")
        }

        if (!params.cpfCnpj?.trim()) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.blank", "O CPF/CNPJ é obrigatório")
        } else if (!(params.cpfCnpj ==~ /\d{11}|\d{14}/)) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.invalidFormat", "CPF/CNPJ inválido")
        }

        if (!params.cep?.trim()) {
            payer.errors.rejectValue("address", "address.cep.blank", "O CEP é obrigatório")
        }
        if (!params.city?.trim()) {
            payer.errors.rejectValue("address", "address.city.blank", "A cidade é obrigatória")
        }
        if (!params.state?.trim()) {
            payer.errors.rejectValue("address", "address.state.blank", "O estado é obrigatório")
        }

        return payer
    }

    public void delete(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        Payer payer = Payer.findByIdAndCustomerAndDeleted(id, customer, false)

        if (!payer) {
            throw new IllegalArgumentException("Pagador não encontrado para este cliente")
        }

        payer.deleted = true
        payer.markDirty('deleted')
        payer.save(failOnError:true)
    }

    public void restore(Long id, customer) {
        Payer payer = Payer.findByIdAndCustomerAndDeleted(id, customer, true)

        if (!payer) {
            throw new IllegalArgumentException("Pagador não encontrado para este cliente ou já está ativo")
        }

        payer.deleted = false
        payer.markDirty('deleted')
        payer.save(failOnError: true)
    }

}