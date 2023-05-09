package com.rental.models

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
class RentCar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id=0

    @Column(columnDefinition = "TIMESTAMP")
    var startRentTime: LocalDateTime?=null

    @Column
    var endRentTime: LocalDateTime?=null

    @ManyToOne
    @JoinColumn(name="customerId", referencedColumnName = "id")
    var customer= Customer()

    @ManyToOne
    @JoinColumn(name="carId", referencedColumnName = "id")
    var car = Car()

}