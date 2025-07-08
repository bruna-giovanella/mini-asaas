package com.asaas.mini

import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class CustomerService {

    public Customer save(Map params) {
        Customer customerValues = validateCustomerParams(params)

        if (customerValues.hasErrors()) {
            throw new ValidationException("Error creating customer", customerValues.errors)
        }
        if (Customer.findByCpfCnpj(params.cpfCnpj)) {
            customerValues.errors.rejectValue("cpfcnpj", "cpfCnpj.exists", "There is already a customer with this CPF/CNPJ")
            throw new ValidationException("Error creating customer", customerValues.errors)
        }
        if (Customer.findByEmail(params.email)) {
            customerValues.errors.rejectValue("email", "email.exists", "Já existe um customer com esse email")
            throw new ValidationException("Error creating customer", customerValues.errors)
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

    public Customer updateCustomer(Long id, Map params) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }

        Customer validateCustomer = validateCustomerParams(params)

        if (customer.hasErrors()) {
            throw new ValidationException("Error updating customer", customer.errors)
        }

        Customer existingCpfCnpj = Customer.findByCpfCnpj(params.cpfCnpj)

        if (existingCpfCnpj && existingCpfCnpj.id != customer.id) {
            validatedCustomer.errors.rejectValue("cpfCnpj", "cpfCnpj.exists", "There is already a customer with this CPF/CNPJ")
            throw new ValidationException("Error updating customer", validatedCustomer.errors)
        }
        Customer existingEmail = Customer.findByEmail(params.email)
        if (existingEmail && existingEmail.id != customer.id) {
            validatedCustomer.errors.rejectValue("email", "email.exists", "There is already a customer with this email")
            throw new ValidationException("Error updating customer", validatedCustomer.errors)
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

    private Customer validateCustomerParams(Map params) {
        Customer customer = new Customer();

        if (!params.name?.trim()) {
            customer.errors.rejectValue("name", "name.blank", "Name cannot be empty")
        } else if (params.name.length() > 255) {
            customer.errors.rejectValue("name", "name.maxSize", "Name must have a maximum of 255 characters")
        }

        if (!params.email?.trim()) {
            customer.errors.rejectValue("email", "email.blank", "Email cannot be empty")
        } else if (params.email.length() > 255) {
            customer.errors.rejectValue("email", "email.maxSize", "Email must have a maximum of 255 characters")
        } else if (!(params.email ==~ /^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            customer.errors.rejectValue("email", "email.invalid", "Email invalid")
        }

        if (!params.cpfCnpj?.trim()) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.blank", "CPF/CNPJ cannot be empty")
        } else if (!(params.cpfCnpj ==~ /\d{11}|\d{14}/)) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.invalidFormat", "CPF/CNPJ invalid")
        }

        if (!params.cep?.trim()) {
            customer.errors.rejectValue("address", "address.cep.blank", "CEP cannot be empty")
        }

        if (!params.city?.trim()) {
            customer.errors.rejectValue("address", "address.city.blank", "City cannot be empty")
        }

        if (!params.state?.trim()) {
            customer.errors.rejectValue("address", "address.state.blank", "State cannot be empty")
        }
        return customer
    }

    public Customer getCustomer(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }
        Customer customer = Customer.findByIdAndDeleted(id, false)
        return customer
    }

    public void deleteCustomer(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, false)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }
        validateDelete(customer)
        customer.deleted = true
        customer.markDirty('deleted')
        customer.save(flush:true, failOnError:true)
    }

    private validateDelete(Customer customer) {
        if (Payment.findByCustomerAndDeleted(customer, false)) {
            throw new IllegalArgumentException("Customer has active payments")
        }
    }

    public void restoreCustomer(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, true)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found or is not deleted")
        }

        customer.deleted = false
        customer.markDirty('deleted')
        customer.save(flush: true, failOnError: true)
    }
}
