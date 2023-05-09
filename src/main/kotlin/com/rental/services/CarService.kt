package com.rental.services

import com.rental.models.Car
import com.rental.repositories.CarRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarService (private val carRepository: CarRepository){

    fun findAll(): List<Car>{
        return this.carRepository.findAll()
    }

    fun save(car: Car): Car{
        return this.carRepository.save(car)
    }

    fun findById(carId: Int): Optional<Car> {
        return this.carRepository.findById(carId)
    }

    fun findExcept(carId: List<Int>): List<Car> {
        return this.carRepository.findCarsExcept(carId)
    }
}