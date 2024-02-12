package com.tutorial.kotlin.spring.thenewboston.controller

import com.tutorial.kotlin.spring.thenewboston.model.Bank
import com.tutorial.kotlin.spring.thenewboston.service.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/banks")
class BankController(private val bankService: BankService) {

//    @ExceptionHandler(NoSuchElementException::class)
//    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
//        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @GetMapping
    fun getBanks(): Collection<Bank> = bankService.getBanks();

    @GetMapping("/{accountNumber}")
    fun getBank(@PathVariable accountNumber: String): Bank = bankService.getBank(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank):Bank = bankService.addBank(bank)

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateBank(@RequestBody bank: Bank): Bank = bankService.updateBank(bank)

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable accountNumber: String): Unit = bankService.deleteBank(accountNumber)


}