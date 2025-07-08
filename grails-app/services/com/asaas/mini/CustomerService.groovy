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
        customer.softDelete()
    }

    private validateDelete(Customer customer) {
        if (Payment.findByCustomerAndDeleted(customer, false)) {
            throw new IllegalArgumentException("Customer has active payments")
        }
    }

    public void restoreCustomer(Long id) {
        Customer customer = customer.findByIdAndDeleted(id, true)

        if (!customer) {
            throw new IllegalArgumentException("Customer not found or is not deleted")
        }

        customer.restore()
    }
}
