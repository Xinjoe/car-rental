package com.rental.controllers

import com.rental.models.Customer
import com.rental.repositories.CustomerRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class CustomerController (private val customerRepository: CustomerRepository) {

    @GetMapping("/customers/detail")
    fun customerDetail(): ResponseEntity<List<Customer>>{
    return ResponseEntity.ok(this.customerRepository.findAll());
    }
}