package com.rental.repositories

import com.rental.models.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CarRepository : JpaRepository<Car, Int> {

    @Query("select c from Car c where c.id not in ?1")
    fun findCarsExcept(rentedCars:List<Int>):List<Car>
}