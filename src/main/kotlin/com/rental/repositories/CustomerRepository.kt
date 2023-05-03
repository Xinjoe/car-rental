package com.rental.repositories

import com.rental.models.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository :JpaRepository<Customer, Int>{
}