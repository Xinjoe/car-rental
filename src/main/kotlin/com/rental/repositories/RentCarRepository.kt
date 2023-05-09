package com.rental.repositories

import com.rental.models.RentCar
import org.springframework.data.jpa.repository.JpaRepository

interface RentCarRepository : JpaRepository<RentCar, Int>{
    fun findByCarId(carId: Int):List<RentCar>
}