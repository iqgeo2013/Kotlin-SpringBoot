package com.tutorial.kotlin.spring.thenewboston.datasource.mock

import com.tutorial.kotlin.spring.thenewboston.datasource.BankDataSource
import com.tutorial.kotlin.spring.thenewboston.model.Bank
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class MockBankDataSource : BankDataSource {
    companion object {
        private val logger = LoggerFactory.getLogger(BankDataSource::class.java)
    }

    val banks = mutableListOf(
        Bank("1234", 1.0, 1),
        Bank("3435", 3.14, 0),
        Bank("5678", 2.0, 2)
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank {
        logger.info("accountNumber {}", accountNumber)
        return banks.firstOrNull() { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find bank by account number $accountNumber")
    }

    override fun addBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank is already available !")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull {it.accountNumber == bank.accountNumber}
            ?: throw NoSuchElementException("Could not find bank by account number ${bank.accountNumber}")
        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val currentBank = banks.firstOrNull {it.accountNumber == accountNumber}
            ?: throw NoSuchElementException("Could not find bank by account number ${accountNumber}")
        banks.remove(currentBank)
    }

}