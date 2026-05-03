package com.bankaccount.console.account.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID


class CreditAccountUnitTest {
    @Test
    fun `Given a CreditAccount, when created, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val balance = 1000.0
        val transactions = mutableListOf<String>()

        val creditAccount = CreditAccount(id = accountId, userId = userId, accountNumber = accountNumber, balance = balance, transactions = transactions)

        assertEquals(accountId, creditAccount.id)
        assertEquals(userId, creditAccount.userId)
        assertEquals(accountNumber, creditAccount.accountNumber)
        assertEquals(balance, creditAccount.balance, 0.0)
        assertEquals(transactions, creditAccount.transactions)
    }
    
}