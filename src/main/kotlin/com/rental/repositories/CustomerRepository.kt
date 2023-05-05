package com.rental.repositories

import com.rental.models.Customer
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomerRepository :JpaRepository<Customer, Int>{
    fun findByEmail(email:String): Customer?

    @Query("select c from Customer c where c.name like %?1% ")
    fun search(s:String, sort:Sort):List<Customer>
}