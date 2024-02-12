package com.tutorial.kotlin.spring.thenewboston.service

import com.tutorial.kotlin.spring.thenewboston.datasource.BankDataSource
import com.tutorial.kotlin.spring.thenewboston.model.Bank
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
//class BankService(@Qualifier("network") private val dataSource: BankDataSource) {
class BankService(private val dataSource: BankDataSource) {

    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()
    fun getBank(accountNumber: String): Bank = dataSource.retrieveBank(accountNumber)
    fun addBank(bank: Bank): Bank = dataSource.addBank(bank)
    fun updateBank(bank: Bank): Bank = dataSource.updateBank(bank)
    fun deleteBank(accountNumber: String) = dataSource.deleteBank(accountNumber)

}