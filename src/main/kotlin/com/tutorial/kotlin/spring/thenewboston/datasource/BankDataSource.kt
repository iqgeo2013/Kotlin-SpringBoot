package com.tutorial.kotlin.spring.thenewboston.datasource

import com.tutorial.kotlin.spring.thenewboston.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>
    fun retrieveBank(accountNumber: String): Bank
    fun addBank(bank: Bank): Bank
    fun updateBank(bank: Bank): Bank
    fun deleteBank(accountNumber: String): Unit
}