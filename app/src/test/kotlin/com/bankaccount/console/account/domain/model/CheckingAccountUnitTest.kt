package com.bankaccount.console.account.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID


class CheckingAccountUnitTest {
    @Test
    fun `Given a CheckingAccount, when created, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val balance = 1000.0
        val transactions = mutableListOf<String>()

        val checkingAccount = CheckingAccount(id = accountId, userId = userId, accountNumber = accountNumber, balance = balance, transactions = transactions)

        assertEquals(accountId, checkingAccount.id)
        assertEquals(userId, checkingAccount.userId)
        assertEquals(accountNumber, checkingAccount.accountNumber)
        assertEquals(balance, checkingAccount.balance, 0.0)
        assertEquals(transactions, checkingAccount.transactions)
    }

    @Test
    fun `Given a CheckingAccount, when withdraw is called with an amount less than balance, then the balance is reduced by that amount`() {
        val checkingAccount = CheckingAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
            balance = 1000.0,
            transactions = mutableListOf()
        )

        val newBalance = checkingAccount.withdraw(500)

        assertEquals(500, newBalance)
    }
}