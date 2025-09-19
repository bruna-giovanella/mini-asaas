package com.asaas.mini.auth

import com.asaas.mini.Customer
import com.asaas.mini.Payer
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.validation.ValidationException
import org.springframework.security.crypto.password.PasswordEncoder

@Transactional
class UserService {

    PasswordEncoder passwordEncoder

    public User createUser(String username, String password, String role, Customer customer) {
        User user = new User(
                username: username,
                password: passwordEncoder.encode(password),
                customer: customer,
                enabled: true,
                accountExpired: false,
                accountLocked: false,
                passwordExpired: false
        )

        if (!user.save(flush: true)) {
            throw new RuntimeException("Erro ao salvar usuário: ${user.errors.allErrors}")
        }

        Role roleObj = Role.findByAuthority(role)
        if (!roleObj) {
            throw new IllegalArgumentException("Role '${role}' não encontrada")
        }

        try {
            // Verificar se já existe um UserRole para este usuário e role
            UserRole existingUserRole = UserRole.findByUserAndRole(user, roleObj)
            if (!existingUserRole) {
                UserRole.create(user, roleObj, true)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar UserRole: ${e.message}")
        }

        return user
    }

    public User get(Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }
        User user = User.findByIdAndCustomer(id, customer)

        return user
    }

    public List<User> list(Customer customer) {
        return User.findAllByCustomer(customer)
    }

    public Map listPaginated(Customer customer, int max, int offset, String sortField, String sortOrder) {
        def validSortFields = ['username', 'dateCreated', 'lastUpdated', 'deleted']
        if (!validSortFields.contains(sortField)) {
            sortField = 'username'
        }
        
        def validOrders = ['asc', 'desc']
        if (!validOrders.contains(sortOrder)) {
            sortOrder = 'asc'
        }
        
        int totalCount = User.createCriteria().count {
            eq('customer', customer)
        }
        
        def criteria = User.createCriteria()
        List<User> userList = criteria.list {
            eq('customer', customer)
            order(sortField, sortOrder)
            maxResults(max)
            firstResult(offset)
        }
        
        return [
            userList: userList,
            totalCount: totalCount
        ]
    }

    public User update(String username, String password, String role, Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        User user = User.findByIdAndCustomer(id, customer)
        if (!user) {
            throw new IllegalArgumentException("Usuário não encontrado")
        }

        user.username = username
        user.customer = customer
        if (password) {
            user.password = passwordEncoder.encode(password)
        }

        Role roleObj = Role.findByAuthority(role)
        if (!roleObj) {
            throw new IllegalArgumentException("Role '${role}' não encontrada")
        }

        UserRole.findAllByUser(user).collect { it.delete(flush: true) }
        UserRole.create(user, roleObj, true)

        return user.save(flush: true, failOnError: true)
    }

    public void delete(Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        User user = User.findByIdAndCustomer(id, customer)
        if (!user) {
            throw new IllegalArgumentException("Usuário não encontrado")
        }
        UserRole userRole = UserRole.findByUserAndDeleted(user, false)

        user.deleted = true
        user.enabled = false
        user.markDirty('deleted')
        user.save(failOnError:true)

        if (userRole) {
            userRole.deleted = true
            userRole.markDirty('deleted')
            userRole.save(failOnError:true)
        }
    }

    public void restore(Customer customer, Long id) {
        if (!id) {
            throw new IllegalArgumentException("O ID é obrigatório")
        }

        User user = User.findByIdAndCustomer(id, customer)
        if (!user) {
            throw new IllegalArgumentException("Usuário não encontrado")
        }
        UserRole userRole = UserRole.findByUserAndDeleted(user, true)

        user.deleted = false
        user.enabled = true
        user.markDirty('deleted')
        user.save(failOnError:true)

        if (userRole) {
            userRole.deleted = false
            userRole.markDirty('deleted')
            userRole.save(failOnError:true)
        }
    }
}