package com.asaas.mini

import javax.transaction.Transactional
import javax.xml.bind.ValidationException

@Transactional // faz com que o método aconteça por completo, ou de rollback
class CustomerService {

    //Cadastro de customer
    public Customer save(Map params) {
        Customer customerValues = validateCustomerParams(params)

        if (customerValues.hasErrors()) {
            throw new ValidationException("Error creating customer", customerValues.errors)
        }
        if (Customer.findByCpfCnjp(params.cpfCnpj)) {
            // verificar duplicidade de documento
            customerValues.errors.rejectValue("cpfcnpj", "cpfCnpj.exists", "There is already a customer with this CPF/CNPJ")
            throw new ValidationException("Error creating customer", customerValues.errors)
        }
        if (Customer.findByEmail(params.email)) {
            // verificar duplicidade de email
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

        if (!params.address) {
            customer.errors.rejectValue("address", "address.required", "Address is required")
        }

        return customer
    }

    //Atualização de customer

    //Listagem e filtros de customer

    //Busca por ID

    //Soft delete
    public void deleteCustomer(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, false); // procura no banco pelo ID e nao deletado

        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }

        validateDelete(customer)
        customer.softDelete()
    }

    private validateDelete(Customer customer) {
        if (Payment.findByCustomerAndDeleted(customer, false)) {
            throw new IllegalArgumentException("Customer has active payments")
        }
    }

    //restore
    public void restoreCustomer(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, true)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found or is not deleted")
        }

        customer.restore()
    }
}

/* Service:
    Camada responsável pelas regras de negócio e as operações específicas do domínio;
 */
