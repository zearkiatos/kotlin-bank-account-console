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

        val creditAccount = CreditAccount(id = accountId, userId = userId, accountNumber = accountNumber)

        assertEquals(accountId, creditAccount.id)
        assertEquals(userId, creditAccount.userId)
        assertEquals(accountNumber, creditAccount.accountNumber)
    }
    
}