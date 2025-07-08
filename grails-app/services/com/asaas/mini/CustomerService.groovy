package com.asaas.mini

import javax.transaction.Transactional

@Transactional
class CustomerService {

    public void deleteCustomer(Long id) {
        Customer customer = customer.findByIdAndDeleted(id, false)

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
}
