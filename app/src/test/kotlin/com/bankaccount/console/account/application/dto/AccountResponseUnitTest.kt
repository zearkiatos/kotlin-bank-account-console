package com.bankaccount.console.account.application.dto

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AccountResponseUnitTest {

    @Test
    fun `Given an AccountResponse, when created a debit account response, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "DEBIT"

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = 0.0,
            transactions = listOf()
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(0.0, accountResponse.balance, 0.0)
    }

    @Test
    fun `Given an AccountResponse, when created a credit account response, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "CREDIT"
        val balance = 0.0

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = balance,
            transactions = listOf()
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(balance, accountResponse.balance, 0.0)
    }

    @Test
    fun `Given an AccountResponse, when created a checking account response, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "CHECKING"

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = 0.0,
            transactions = listOf()
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(0.0, accountResponse.balance, 0.0)
    }

}