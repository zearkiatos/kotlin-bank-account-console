package com.bankaccount.console.account.application.dto

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountResponseUnitTest {

    @Test
    fun `Given an AccountResponse debit account, when created, then all properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = "DEBIT"
        val transactions = mutableListOf("T1", "T2")

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = 1000.0,
            transactions = transactions
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(1000.0, accountResponse.balance, 0.0)
        assertEquals(transactions, accountResponse.transactions)
        assertEquals(2, accountResponse.transactions.size)
    }

    @Test
    fun `Given an AccountResponse credit account, when created, then all properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "0987654321"
        val accountType = "CREDIT"
        val balance = 5000.0
        val transactions = mutableListOf("T1", "T2", "T3")

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = balance,
            transactions = transactions
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(balance, accountResponse.balance, 0.0)
        assertEquals(transactions, accountResponse.transactions)
        assertEquals(3, accountResponse.transactions.size)
    }

    @Test
    fun `Given an AccountResponse checking account, when created, then all properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1111111111"
        val accountType = "CHECKING"
        val transactions = mutableListOf<String>()

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType,
            balance = 0.0,
            transactions = transactions
        )

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(accountType, accountResponse.accountType)
        assertEquals(0.0, accountResponse.balance, 0.0)
        assertEquals(transactions, accountResponse.transactions)
        assertTrue(accountResponse.transactions.isEmpty())
    }

    @Test
    fun `Given an AccountResponse with transactions, when getTransactions is called, then it returns the transaction list`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val transactions = mutableListOf("Transaction1", "Transaction2", "Transaction3")

        val accountResponse = AccountResponse(
            id = accountId,
            userId = userId,
            accountNumber = "5555555555",
            accountType = "DEBIT",
            balance = 2500.0,
            transactions = transactions
        )

        val retrievedTransactions = accountResponse.transactions

        assertEquals(3, retrievedTransactions.size)
        assertEquals("Transaction1", retrievedTransactions[0])
        assertEquals("Transaction2", retrievedTransactions[1])
        assertEquals("Transaction3", retrievedTransactions[2])
    }

    @Test
    fun `Given an AccountResponse with empty transactions, when getTransactions is called, then it returns an empty list`() {
        val accountResponse = AccountResponse(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "9999999999",
            accountType = "CHECKING",
            balance = 0.0,
            transactions = mutableListOf()
        )

        val retrievedTransactions = accountResponse.transactions

        assertTrue(retrievedTransactions.isEmpty())
        assertEquals(0, retrievedTransactions.size)
    }
}