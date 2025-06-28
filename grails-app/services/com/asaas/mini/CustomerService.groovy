package com.asaas.mini

import javax.transaction.Transactional

@Transactional // faz com que o método aconteça por completo, ou de rollback
class CustomerService {

    //cadastro de customer

    //Atualização de customer

    //Listagem e filtros de customer

    //Busca por ID

    //Soft delete
    public void deleteCustomer(Long id) {
        Customer customer = customer.findByIdAndDeleted(id, false); // procura no banco pelo ID e nao deletado

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
}

/* Service:
    Camada responsável pelas regras de negócio e as operações específicas do domínio;

 */
