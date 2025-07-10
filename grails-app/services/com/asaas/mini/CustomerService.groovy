package com.asaas.mini

import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class CustomerService {

    public Customer save(Map params) {
        Customer validateCustomer = validateSave(params)

        if (validateCustomer.hasErrors()) {
            throw new ValidationException("Error creating customer", validateCustomer.errors)
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

    private Customer validateSave(Map params) {
        Customer customer = new Customer();

        if (Customer.findByCpfCnjp(params.cpfCnpj)) {
            customerValues.errors.rejectValue("cpfcnpj", "cpfCnpj.exists", "There is already a customer with this CPF/CNPJ")
        }

        if (Customer.findByEmail(params.email)) {
            customerValues.errors.rejectValue("email", "email.exists", "Já existe um customer com esse email")
        }

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
            customer.errors.rejectValue("address.cep", "address.cep.blank", "CEP cannot be empty")
        } else if (!(params.cep ==~ /^\d{8}$/)) {
            customer.errors.rejectValue("address.cep", "address.cep.invalid", "CEP must have 8 digits")
        }

        if (!params.city?.trim()) {
            customer.errors.rejectValue("address.city", "address.city.blank", "City cannot be empty")
        }

        if (!params.state?.trim()) {
            customer.errors.rejectValue("address.state", "address.state.blank", "State cannot be empty")
        }

        return customer
    }



    public Customer getCustomer(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)
        return customer;
    }

    public void deleteCustomer(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, false)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }

        validateDelete(customer)
        customer.deleted = true
        customer.markDirty('deleted')
        customer.save(failOnError:true)
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
        customer.save(failOnError: true)
    }
}
