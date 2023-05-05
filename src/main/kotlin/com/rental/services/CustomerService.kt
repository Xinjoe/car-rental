package com.rental.services

import com.rental.models.Customer
import com.rental.repositories.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun save(customer: Customer): Customer{
        return this.customerRepository.save(customer)
    }

    fun findByEmail(email: String): Customer?{
        return this.customerRepository.findByEmail(email)
    }

    fun getById(id: Int): Customer{
        return this.customerRepository.getById(id)
    }
}