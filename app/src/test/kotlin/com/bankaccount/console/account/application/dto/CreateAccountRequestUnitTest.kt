package com.bankaccount.console.account.application.dto

import org.junit.Test
import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class CreateAccountRequestUnitTest {
    @Test
    fun `Given a CreateAccountRequest, when created a debit account request, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "DEBIT"

        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType
        )

        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
    }

    @Test
    fun `Given a CreateAccountRequest, when created a credit account request, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "CREDIT"
        
        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType
        )
        
        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
    }

    @Test
    fun `Given a CreateAccountRequest, when created a checking account request, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "CHECKING"
        
        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType
        )
        
        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)

    }

}