package com.bankaccount.console.account.application.dto

import org.junit.Test
import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class CreateAccountRequestUnitTest {
    @Test
    fun `Given a CreateAccountRequest debit account, when created with all properties, then all are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "DEBIT"
        val balance = 1000.0
        val transactions = mutableListOf("T1", "T2")

        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = balance,
            transactions = transactions
        )

        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
        assertEquals(balance, createAccountRequest.balance, 0.0)
        assertEquals(transactions, createAccountRequest.transactions)
        assertEquals(2, createAccountRequest.transactions.size)
    }

    @Test
    fun `Given a CreateAccountRequest credit account, when created with all properties, then all are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "0987654321"
        val accountType = "CREDIT"
        val balance = 5000.0
        val transactions = mutableListOf("T1", "T2", "T3")
        
        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = balance,
            transactions = transactions
        )
        
        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
        assertEquals(balance, createAccountRequest.balance, 0.0)
        assertEquals(transactions, createAccountRequest.transactions)
        assertEquals(3, createAccountRequest.transactions.size)
    }

    @Test
    fun `Given a CreateAccountRequest checking account, when created with all properties, then all are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1111111111"
        val accountType = "CHECKING"
        val balance = 2500.50
        val transactions = mutableListOf<String>()
        
        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = balance,
            transactions = transactions
        )
        
        assertEquals(accountId, createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals(accountNumber, createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
        assertEquals(balance, createAccountRequest.balance, 0.0)
        assertEquals(transactions, createAccountRequest.transactions)
        assertTrue(createAccountRequest.transactions.isEmpty())
    }

    @Test
    fun `Given a CreateAccountRequest with default values, when created without optional parameters, then defaults are used`() {
        val userId = UUID.randomUUID().toString()
        val accountType = "DEBIT"

        val createAccountRequest = CreateAccountRequest(
            userId = userId,
            accountType = accountType
        )

        assertEquals("", createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals("", createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
        assertEquals(0.0, createAccountRequest.balance, 0.0)
        assertTrue(createAccountRequest.transactions.isEmpty())
    }

    @Test
    fun `Given a CreateAccountRequest with only required and partial optional parameters, then they are correctly assigned`() {
        val userId = UUID.randomUUID().toString()
        val accountType = "CREDIT"
        val balance = 7500.0

        val createAccountRequest = CreateAccountRequest(
            userId = userId,
            accountType = accountType,
            balance = balance
        )

        assertEquals("", createAccountRequest.id)
        assertEquals(userId, createAccountRequest.userId)
        assertEquals("", createAccountRequest.accountNumber)
        assertEquals(accountType, createAccountRequest.accountType)
        assertEquals(balance, createAccountRequest.balance, 0.0)
        assertTrue(createAccountRequest.transactions.isEmpty())
    }

    @Test
    fun `Given a CreateAccountRequest with transactions, when getTransactions is called, then it returns the list`() {
        val userId = UUID.randomUUID().toString()
        val accountType = "DEBIT"
        val transactions = mutableListOf("Transaction1", "Transaction2")

        val createAccountRequest = CreateAccountRequest(
            userId = userId,
            accountType = accountType,
            transactions = transactions
        )

        val retrievedTransactions = createAccountRequest.transactions

        assertEquals(2, retrievedTransactions.size)
        assertEquals("Transaction1", retrievedTransactions[0])
        assertEquals("Transaction2", retrievedTransactions[1])
    }

    @Test
    fun `Given a CreateAccountRequest with null balance, when balance is accessed, then it returns the value`() {
        val userId = UUID.randomUUID().toString()
        val accountType = "CHECKING"

        val createAccountRequest = CreateAccountRequest(
            userId = userId,
            accountType = accountType,
        )

        assertEquals(0.0, createAccountRequest.balance, 0.0)
    }
}