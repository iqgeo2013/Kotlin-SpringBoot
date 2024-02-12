package com.tutorial.kotlin.spring.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.tutorial.kotlin.spring.thenewboston.model.Bank
import io.mockk.unmockkObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

//    @Autowired
//    lateinit var mockMvc: MockMvc

//    @Autowired
//    lateinit var objectMaper: ObjectMapper

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            // when/then
            mockMvc.get("/api/banks")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber") {
                        value("1234")
                    }
                }
        }

    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with given account number`() {
            // given
            val accountNumber = 1234

            // when/then
            mockMvc.get("/api/banks/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("1.0") }
                    jsonPath("$.accountNumber") { value("1234") }
                    jsonPath("$.transactionFee") { value("1") }
                }


        }

        @Test
        fun `should return NOT FOUND if the account number does not exist`() {
            //given
            val accountNumber = "does_not_exists"
            mockMvc.get("/api/banks/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class addBank {
        @Test
        fun `should add the new bank`() {
            // given
            val accountNumber = generateRandomText(10)
            val newBank = Bank(accountNumber, generateRandomDouble(), generateRandomInt());

            // when
            val performPost = mockMvc.post("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank);
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.accountNumber") {
                        value(accountNumber)
                    }

                }
        }

        @Test
        fun `should return BAD REQUEST if bank with given number already exists`() {
            // given
            val invalidBanks = Bank("1234", 0.0, 1)

            // when
            val performPost = mockMvc.post("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBanks);
            }

            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }

    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class patchEixistingBank {
        @Test
        fun `should update existing bank`() {
            // given
            val updateBank = Bank("1234", 0.0, 1)

            // when
            val performPatchRequest = mockMvc.patch("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank);
            }

            //then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updateBank))
                    }
                }

            mockMvc.get("/api/banks/${updateBank.accountNumber}")
                .andDo { print() }
                .andExpect { content { json(objectMapper.writeValueAsString(updateBank)) } }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // given
            val bankWithAccountNumberNotAvailable = Bank(generateRandomText(10), 0.0, 1)

            // when
            val performPatchRequest = mockMvc.patch("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bankWithAccountNumberNotAvailable);
            }

            //then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }

                }

        }

    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class deleteEixistingBank {
        @Test
        fun `should return NOT FOUND if no bank exit with accountNumber`() {
            // given
            val accountNumber = generateRandomText(10)

            // when/then
            mockMvc.delete("/api/banks/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

            mockMvc.get("/api/banks/$accountNumber")
                .andExpect { status { isNotFound() } }
        }

        @Test
        @DirtiesContext
        fun `should delete exiting bank`() {
            // given
            val accountNumber = 1234

            // when/then
            mockMvc.delete("/api/banks/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNoContent() } }

            mockMvc.get("/api/banks/$accountNumber")
                .andExpect { status { isNotFound() } }
        }
    }

    fun generateRandomText(length: Int): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { chars[Random.nextInt(chars.size)] }
            .joinToString("")
    }

    fun generateRandomDouble(min: Double, max: Double): Double {
        require(min < max) { "min must be less than max" }
        return Random.nextDouble(min, max)
    }

    fun generateRandomDouble(): Double {
        return generateRandomDouble(0.0, 20.0);
    }

    fun generateRandomInt(min: Int, max: Int): Int {
        require(min < max) { "min must be less than max" }
        return Random.nextInt(min, max)
    }

    fun generateRandomInt(): Int {
        return generateRandomInt(0, 20);
    }

}