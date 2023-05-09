package com.rental.services

import com.rental.models.Car
import com.rental.models.Customer
import com.rental.models.RentCar
import com.rental.repositories.CarRepository
import com.rental.repositories.RentCarRepository
import org.hibernate.internal.util.collections.CollectionHelper.listOf
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RentCarService (private val rentCarRepository: RentCarRepository,
                      private val carRepository: CarRepository) {

    fun save(customer: Customer, car:Car,start: LocalDateTime, end: LocalDateTime ) :RentCar{
        val rentCar= RentCar()
        rentCar.car=car
        rentCar.customer=customer
        rentCar.startRentTime= start
        rentCar.endRentTime= end
        return this.rentCarRepository.save(rentCar)
    }

    fun findByCarId(carId:Int):List<RentCar>{
        return this.rentCarRepository.findByCarId(carId)
    }

    fun findAvailableCar():List<Car>{
        val rentedCars = listOf(0)
        rentedCars.remove(0)
        val rentCars=this.rentCarRepository.findAll()
        for (rentCar in rentCars){
            if(isBetween(rentCar.startRentTime,rentCar.endRentTime,LocalDateTime.now())){
                rentedCars.add(rentCar.car.id)
            }
        }

        if(rentedCars.isEmpty())
            return this.carRepository.findAll()
        else
            return this.carRepository.findCarsExcept(rentedCars)
    }
    //find available cars in a time range
    fun findAvailableCarBetween(start: LocalDateTime,end: LocalDateTime):List<Car>{
        val rentedCars = listOf(0)
        rentedCars.remove(0)
        val rentCars=this.rentCarRepository.findAll()
        for (rentCar in rentCars){
            if(isTwoTimeRangeOverlap(start,end,rentCar.startRentTime,rentCar.endRentTime)){
                rentedCars.add(rentCar.car.id)
            }
        }
        if(rentedCars.isEmpty())
            return this.carRepository.findAll()
        else
            return this.carRepository.findCarsExcept(rentedCars)
    }

    private fun isBetween(start:LocalDateTime?,end: LocalDateTime?,target: LocalDateTime?): Boolean{
        if (target != null) {
            return target.isAfter(start) && target.isBefore(end)
        }
        return false
    }

    fun isTwoTimeRangeOverlap(start1:LocalDateTime?,end1: LocalDateTime?,start2:LocalDateTime?,end2: LocalDateTime?):Boolean{
//        return true(overlaps) if any of the condition matches
        return isBetween(start1, end1, start2)||
                isBetween(start1, end1, end2)||
                isBetween(start2, end2, start1)||
                isBetween(start2, end2, end1)||
                start1==start2||
                start1==end2||
                end1==start2||
                end1==end2
    }

    fun checkCarAvailability(carId:Int, start:LocalDateTime,end:LocalDateTime):Boolean{
        val carRentals=findByCarId(carId)
        for (carRental in carRentals){ //car not available if any of the rental time overlaps
            if(isTwoTimeRangeOverlap(start,end,carRental.startRentTime,carRental.endRentTime)){
                return false
            }
        }
        return true
    }

}