package com.bankaccount.console.account.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID


class DebitAccountUnitTest {
    @Test
    fun `Given a DebitAccount, when created, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val balance = 1000.0
        val transactions = mutableListOf<String>()

        val debitAccount = DebitAccount(id = accountId, userId = userId, accountNumber = accountNumber, balance = balance, transactions = transactions)

        assertEquals(accountId, debitAccount.id)
        assertEquals(userId, debitAccount.userId)
        assertEquals(accountNumber, debitAccount.accountNumber)
        assertEquals(balance, debitAccount.balance, 0.0)
        assertEquals(transactions, debitAccount.transactions)
    }

    @Test
    fun `Given a DebitAccount, when withdraw is called with an amount less than balance, then the balance is reduced by that amount`() {
        val debitAccount = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
            balance = 1000.0,
            transactions = mutableListOf()
        )

        val newBalance = debitAccount.withdraw(500)

        assertEquals(500, newBalance)
    }
    
}