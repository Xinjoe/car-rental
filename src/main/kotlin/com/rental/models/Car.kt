package com.rental.models

import jakarta.persistence.*


@Entity
class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id=0

    @Column
    var model=""

    @Column
    var year=0

    @Column
    var color=""

    @Column
    @OneToMany(mappedBy = "car", orphanRemoval = true)
    private val rentCar: Set<RentCar>? = null
}