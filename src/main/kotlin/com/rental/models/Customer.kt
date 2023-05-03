package com.rental.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id=0

    @Column
    var name=""

    @Column
    var age=0

}