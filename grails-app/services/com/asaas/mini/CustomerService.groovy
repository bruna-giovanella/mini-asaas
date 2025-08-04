package com.asaas.mini

import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class CustomerService {

    public Customer save(Map params) {
        Customer validateCustomer = validateParams(params)

        if (validateCustomer.hasErrors()) {
            throw new ValidationException("Erro ao criar cadastro: ", validateCustomer.errors)
        }

        Address address = new Address()
        address.cep = params.cep
        address.city = params.city
        address.state = params.state
        address.complement = params.complement

        Customer customer = new Customer()
        customer.name = params.name
        customer.email = params.email
        customer.cpfCnpj = params.cpfCnpj
        customer.address = address

        return customer.save(flush: true, failOnError: true)
    }

    public Customer update(Long id, Map params) {
        if (!id) {
            throw new IllegalArgumentException("ID é obrigatório")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)

        if (!customer) {
            throw new IllegalArgumentException("Conta não encontrada")
        }

        Customer validatedCustomer = validateParams(params, id)

        if (validatedCustomer.hasErrors()) {
            throw new ValidationException("Erro ao atualizar conta: ", validatedCustomer.errors)
        }

        customer.name = params.name
        customer.email = params.email
        customer.cpfCnpj = params.cpfCnpj

        customer.address.cep = params.cep
        customer.address.city = params.city
        customer.address.state = params.state
        customer.address.complement = params.complement

        return customer.save(flush: true, failOnError: true)
    }

    private Customer validateParams(Map params, Long id = null) {

        Customer customer = new Customer()

        Customer existingCpfCnpj = Customer.where {
            cpfCnpj == params.cpfCnpj
            if (id) {
                ne 'id', id
            }
        }.get()

        if (existingCpfCnpj) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.exists", "Já existe um customer com esse CPF/CNPJ")
        }

        Customer existingEmail = Customer.where {
            email == params.email
            if (id) {
                ne 'id', id
            }
        }.get()

        if (existingEmail) {
            customer.errors.rejectValue("email", "email.exists", "Já existe um customer com esse email")
        }

        if (!params.name?.trim()) {
            customer.errors.rejectValue("name", "name.blank", "Nome é obrigatório")
        } else if (params.name.length() > 255) {
            customer.errors.rejectValue("name", "name.maxSize", "O nome deve ter no máximo 255 caracteres")
        }

        if (!params.email?.trim()) {
            customer.errors.rejectValue("email", "email.blank", "Email é obrigatório")
        } else if (params.email.length() > 255) {
            customer.errors.rejectValue("email", "email.maxSize", "O email deve ter no máximo 255 caracteres")
        } else if (!(params.email ==~ /^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            customer.errors.rejectValue("email", "email.invalid", "Email inválido")
        }

        if (!params.cpfCnpj?.trim()) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.blank", "CPF/CNPJ é obrigatório")
        } else if (!(params.cpfCnpj ==~ /\d{11}|\d{14}/)) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.invalidFormat", "CPF/CNPJ inválido")
        }

        if (!params.cep?.trim()) {
            customer.errors.rejectValue("address", "address.cep.blank", "CEP é obrigatório")
        }

        if (!params.city?.trim()) {
            customer.errors.rejectValue("address", "address.city.blank", "Cidade é obrigatória")
        }

        if (!params.state?.trim()) {
            customer.errors.rejectValue("address", "address.state.blank", "Estado é obrigatório")
        }

        return customer
    }

    public Customer get(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID é necessário")
        }
        Customer customer = Customer.findByIdAndDeleted(id, false)
        return customer
    }

    public void delete(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID é necessário")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)
        if (!customer) {
            throw new IllegalArgumentException("Conta não encontrada")
        }

        validateDelete(customer)
        customer.deleted = true
        customer.markDirty('deleted')
        customer.save(failOnError:true)
    }

    private validateDelete(Customer customer) {
        List<Payment> activePayments = Payment.createCriteria().list {
            eq("deleted", false)
            eq("status", PaymentStatus.AGUARDANDO_PAGAMENTO)
            payer {
                eq("customer", customer)
                eq("deleted", false)
            }
        }

        if (activePayments) {
            throw new IllegalArgumentException("Existem pagamentos ativos")
        }
    }

    public void restore(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, true)

        if (!customer) {
            throw new IllegalArgumentException("Cliente não encontrado ou está ativo")
        }

        customer.deleted = false
        customer.markDirty('deleted')
        customer.save(failOnError: true)
    }
}