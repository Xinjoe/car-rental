package com.rental.controllers

import com.rental.models.Customer
import com.rental.repositories.CustomerRepository
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class CustomerController (private val customerRepository: CustomerRepository) {

    @GetMapping("/customers/detail")
    fun customerDetail(): ResponseEntity<List<Customer>>{
    return ResponseEntity.ok(this.customerRepository.findAll());
    }

    @GetMapping("/customers/findCustomer")
    fun findCustomer(
        @RequestParam("s", defaultValue = "")s:String,
        @RequestParam("sort", defaultValue = "")sort:String
    ): ResponseEntity<List<Customer>>{
        var direction = Sort.unsorted()

        if(sort == "asc"){
            direction = Sort.by(Sort.Direction.ASC,"name")
        }else if(sort == "desc"){
            direction = Sort.by(Sort.Direction.DESC,"name")
        }
        return ResponseEntity.ok(this.customerRepository.search(s, direction));
    }
}