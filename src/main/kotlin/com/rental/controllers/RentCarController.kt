package com.rental.controllers

import com.rental.dtos.Message
import com.rental.models.Car
import com.rental.models.Customer
import com.rental.models.RentCar
import com.rental.services.CarService
import com.rental.services.CustomerService
import com.rental.services.RentCarService
import io.jsonwebtoken.Jwts
import org.hibernate.internal.util.collections.CollectionHelper
import org.hibernate.internal.util.collections.CollectionHelper.listOf
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("api")
class RentCarController(
    private val rentCarService: RentCarService,
    private val carService: CarService,
    private val customerService: CustomerService
    ) {

    @PostMapping("rentCar")
    fun saveRentCar(
        @CookieValue("jwt") jwt:String?,
        @RequestParam("car", defaultValue = "")carId:Int, //carId
        @RequestParam("start", defaultValue = "") start:String,
        @RequestParam("end", defaultValue = "") end:String
    ): ResponseEntity<Any> {

//      validate time
        if(start=="" && end==""){
            return ResponseEntity.badRequest().body(Message("Please enter start rental time and end rental time."))
        }else if(start==""){
            return ResponseEntity.badRequest().body(Message("Please enter start rental time."))
        }else if(end==""){
            return ResponseEntity.badRequest().body(Message("Please enter end rental time."))
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val startTime:LocalDateTime
        val endTime:LocalDateTime
        try {   //check the format of the date
            startTime=LocalDateTime.parse(start, formatter) //format to LocalDateTime
            endTime=LocalDateTime.parse(end, formatter) //format to LocalDateTime
        }catch (e: Exception){
            return ResponseEntity.badRequest().body(Message("Please enter rental time with format yyyy-MM-dd HH:mm."))
        }

//      validate customer
        val customer:Customer
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("Unauthenticated"))
            }
            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body //get issuer and expiration, issuer=id
            val tempCustomer=this.customerService.findById(body.issuer.toInt())
            if(!tempCustomer.isPresent){
                return ResponseEntity.badRequest().body(Message("No logged in customer"))
            }
            customer=tempCustomer.get()
        }catch (e: Exception){
            return ResponseEntity.status(401).body(Message("Unauthenticated"))
        }

//       validate car
        val tempCar=this.carService.findById(carId)
        if(!tempCar.isPresent){
            return ResponseEntity.badRequest().body(Message("car id not found"))
        }
        val car=tempCar.get()
        val isCarAvailable=this.rentCarService.checkCarAvailability(car.id,startTime,endTime)
        if(!isCarAvailable){
            return ResponseEntity.ok().body(Message("Car is not available in selected time slot"))
        }

        this.rentCarService.save(customer, car, startTime, endTime)
        return ResponseEntity.ok().body(Message("saved to database"))

    }

    @PostMapping("findRentCar")
    fun findByRentCarId(@RequestParam ("carId")carId:Int): ResponseEntity<List<RentCar>>{
        return ResponseEntity.ok().body(this.rentCarService.findByCarId(carId))
    }

    @GetMapping("viewAvailableCar")
    fun viewAvailableCar():ResponseEntity<List<Car>>{
        return ResponseEntity.ok().body(this.rentCarService.findAvailableCar())
    }

    @PostMapping("viewAvailableCarInRange")
    fun viewAvailableCarInRange(
        @RequestParam("start")start:String,
        @RequestParam("end")end:String
    ):ResponseEntity<Any>{
        if(start=="" || end==""){
            return ResponseEntity.badRequest().body(Message("Please enter both start rental time and end rental time."))
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val startTime:LocalDateTime
        val endTime:LocalDateTime
        try {
            startTime=LocalDateTime.parse(start, formatter)
            endTime=LocalDateTime.parse(end, formatter)
        }catch (e: Exception){
            return ResponseEntity.badRequest().body(Message("Please enter rental time with format yyyy-MM-dd HH:mm."))
        }
        return ResponseEntity.ok().body(this.rentCarService.findAvailableCarBetween(startTime,endTime))
    }

}