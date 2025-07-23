package com.asaas.mini

import com.asaas.mini.enums.Role
import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.access.AccessDeniedException
import org.grails.datastore.mapping.validation.ValidationException

@Transactional
class CustomerService {

    PasswordEncoder passwordEncoder

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

        customer.save(flush: true, failOnError: true)

        createFirstUser(customer, params)

        return customer
    }

    private Customer validateSave(Map params) {
        Customer customer = new Customer()

        if (Customer.findByCpfCnpj(params.cpfCnpj)) {
            customer.errors.rejectValue("cpfcnpj", "cpfCnpj.exists", "There is already a customer with this CPF/CNPJ")
        }

        if (Customer.findByEmail(params.email)) {
            customer.errors.rejectValue("email", "email.exists", "There is already a customer with this email")
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

    private void createFirstUser(Customer customer, Map userParams) {
        String email = userParams.emailUsuario
        String password = userParams.password

        if (!email || !password) {
            throw new ValidationException("Email e senha do usuário administrador são obrigatórios", null)
        }

        User user = new User(
                email: email,
                password: passwordEncoder.encode(password),
                customer: customer
        )

        if (!user.validate()) {
            throw new ValidationException("Erro ao validar o usuário administrador", user.errors)
        }

        user.save(flush: true)

        new UserRole(user: user, role: Role.ROLE_ADMINISTRADOR).save(flush: true)
    }

        public Customer update(Long id, Map params) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }

        Customer validatedCustomer = validateUpdate(id, params)

        if (validatedCustomer.hasErrors()) {
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

    private Customer validateUpdate(Long id, Map params) {
        Customer customer = new Customer()

        Customer existingCustomer = Customer.get(id)

        Customer existingCpfCnpj = Customer.findByCpfCnpj(params.cpfCnpj)
        if (existingCpfCnpj && existingCpfCnpj.id != id) {
            customer.errors.rejectValue("cpfCnpj", "cpfCnpj.exists", "Já existe um customer com esse CPF/CNPJ")
        }

        Customer existingEmail = Customer.findByEmail(params.email)
        if (existingEmail && existingEmail.id != id) {
            customer.errors.rejectValue("email", "email.exists", "Já existe um customer com esse email")
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

    public Customer get(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }
        Customer customer = Customer.findByIdAndDeleted(id, false)
        return customer
    }

    public void delete(Long id) {
        if (!id) {
            throw new IllegalArgumentException("ID is required")
        }

        Customer customer = Customer.findByIdAndDeleted(id, false)
        if (!customer) {
            throw new IllegalArgumentException("Customer not found")
        }

        validateDelete(customer)

        List<Payer> payers = Payer.findAllByCustomerAndDeleted(customer, false)
        payers.each { payer ->
            payer.deleted = true
            payer.markDirty('deleted')
            payer.save(failOnError: true)

            List<Payment> payments = Payment.findAllByPayerAndDeleted(payer, false)
            payments.each { payment ->
                payment.status = PaymentStatus.EXCLUIDA
                payment.deleted = true
                payment.markDirty('deleted')
                payment.save(failOnError: true)
            }
        }

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
            throw new IllegalArgumentException("Customer has active payments")
        }
    }

    public void restore(Long id) {
        Customer customer = Customer.findByIdAndDeleted(id, true)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found or is not deleted")
        }

        customer.deleted = false
        customer.markDirty('deleted')
        customer.save(failOnError: true)
    }
}
