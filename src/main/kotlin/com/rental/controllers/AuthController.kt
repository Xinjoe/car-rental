package com.rental.controllers

import com.rental.dtos.LoginDTO
import com.rental.dtos.Message
import com.rental.dtos.RegisterDTO
import com.rental.models.Customer
import com.rental.services.CustomerService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api")
class AuthController (private val customerService: CustomerService){

    @PostMapping("register")
    fun register(@RequestBody body:RegisterDTO): ResponseEntity<Customer>{
        val customer = Customer()
        customer.name=body.name
        customer.email=body.email
        customer.age=body.age
        customer.password=body.password
        return ResponseEntity.ok(this.customerService.save(customer))
    }

    @PostMapping("login")
    fun login(@RequestBody body:LoginDTO, response: HttpServletResponse):ResponseEntity<Any>{
        val customer=this.customerService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found!"))//if customer ==null

        if(!customer.comparePassword(body.password)){
            return ResponseEntity.badRequest().body(Message("Invalid password!"))
        }

        val issuer = customer.id.toString()

        val jwt=Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis()+60*60*24*1000)) //1 day
            .signWith(SignatureAlgorithm.HS512,"secret").compact()

        var cookie=Cookie("jwt", jwt)
        cookie.isHttpOnly = true //used by backend

        response.addCookie(cookie)
        return ResponseEntity.ok(Message("Success"))
    }

    @GetMapping("customer")
    fun customer(@CookieValue("jwt") jwt:String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("Unauthenticated"))
            }
            val body =
                Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body //get issuer and expiration, issuer=id

            return ResponseEntity.ok(this.customerService.findById(body.issuer.toInt()))

        }catch (e: Exception){
            return ResponseEntity.status(401).body(Message("Unauthenticated"))
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse):ResponseEntity<Any>{
        var cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Log out successfully!"))
    }
}