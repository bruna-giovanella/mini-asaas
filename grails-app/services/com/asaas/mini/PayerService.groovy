package com.asaas.mini

import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class PayerService {

    public Payer save(Map params, Customer customer) {
        Payer payerValidate = validateSave(params)

        if (payerValidate.hasErrors()) {
            throw new ValidationException("Erro ao criar o pagador: ", payerValidate.errors)
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

    private Payer validateSave(Map params) {
        Payer payer = new Payer()

        if (Payer.findByCpfCnpjAndCustomer(params.cpfCnpj, customer)) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.exists", "There is already a payer with this CPF/CNPJ for this customer")
        }

        if (Payer.findByEmailAndCustomer(params.email, customer)) {
            payer.errors.rejectValue("email", "email.exists", "There is already a payer with this email for this customer")
        }

        if (!params.name?.trim()) {
            payer.errors.rejectValue("name", "name.blank", "Name cannot be empty")
        } else if (params.name.length() > 255) {
            payer.errors.rejectValue("name", "name.maxSize", "Name must have a maximum of 255 characters")
        }

        if (!params.email?.trim()) {
            payer.errors.rejectValue("email", "email.blank", "Email cannot be empty")
        } else if (params.email.length() > 255) {
            payer.errors.rejectValue("email", "email.maxSize", "Email must have a maximum of 255 characters")
        } else if (!(params.email ==~ /^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            payer.errors.rejectValue("email", "email.invalid", "Invalid email")
        }

        if (!params.contactNumber?.trim()) {
            payer.errors.rejectValue("contactNumber", "contactNumber.blank", "Contact number cannot be empty")
        } else if (!(params.contactNumber ==~ /^(1[1-9]|2[12478]|3[1234578]|4[12345789]|5[1345]|6[1]|7[134579]|8[1-9]|9[1-9])\d{8,9}$/)) {
            payer.errors.rejectValue("contactNumber", "contactNumber.invalidFormat", "Invalid contact number")
        }

        if (!params.cpfCnpj?.trim()) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.blank", "CPF/CNPJ cannot be empty")
        } else if (!(params.cpfCnpj ==~ /\d{11}|\d{14}/)) {
            payer.errors.rejectValue("cpfCnpj", "cpfCnpj.invalidFormat", "Invalid CPF/CNPJ")
        }

        if (!params.cep?.trim()) {
            payer.errors.rejectValue("address", "address.cep.blank", "CEP cannot be empty")
        }

        if (!params.city?.trim()) {
            payer.errors.rejectValue("address", "address.city.blank", "City cannot be empty")
        }

        if (!params.state?.trim()) {
            payer.errors.rejectValue("address", "address.state.blank", "State cannot be empty")
        }
        return payer
    }

    public void delete(Long id, Customer customer) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Payer payer = Payer.findByIdAndCustomerAndDeleted(id, customer, false)

        if (!payer) {
            throw new IllegalArgumentException("Payer not found for this customer")
        }

        payer.deleted = true
        payer.markDirty('deleted')
        payer.save(failOnError:true)    }


}
