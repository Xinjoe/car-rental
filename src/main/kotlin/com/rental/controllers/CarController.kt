package com.rental.controllers

import com.rental.dtos.Message
import com.rental.models.Car
import com.rental.services.CarService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api")
class CarController (private val carService: CarService){
    @GetMapping("car")
    fun carDetails (): ResponseEntity<List<Car>>{
        return ResponseEntity.ok(this.carService.findAll());
    }

    @PostMapping("findCar")
    fun findCar(
        @RequestParam("car", defaultValue = "")carId: Int
        ):ResponseEntity<Any>{
        val car= this.carService.findById(carId)
        if(!car.isPresent) {
            return ResponseEntity.badRequest().body(Message("No such Id"))
        }
        return ResponseEntity.ok().body(car)
    }

}