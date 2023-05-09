package com.rental.services

import com.rental.models.Customer
import com.rental.repositories.CustomerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun save(customer: Customer): Customer{
        return this.customerRepository.save(customer)
    }

    fun findByEmail(email: String): Customer?{
        return this.customerRepository.findByEmail(email)
    }

    fun findById(id: Int): Optional<Customer> {
        return this.customerRepository.findById(id)
    }
}