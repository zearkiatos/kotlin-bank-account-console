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
        val transactions = listOf<String>()

        val debitAccount = DebitAccount(id = accountId, userId = userId, accountNumber = accountNumber, balance = balance, transactions = transactions)

        assertEquals(accountId, debitAccount.id)
        assertEquals(userId, debitAccount.userId)
        assertEquals(accountNumber, debitAccount.accountNumber)
        assertEquals(balance, debitAccount.balance, 0.0)
        assertEquals(transactions, debitAccount.transactions)
    }
    
}