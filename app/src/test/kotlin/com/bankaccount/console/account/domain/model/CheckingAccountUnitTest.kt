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

        val checkingAccount = CheckingAccount(id = accountId, userId = userId, accountNumber = accountNumber)

        assertEquals(accountId, checkingAccount.id)
        assertEquals(userId, checkingAccount.userId)
        assertEquals(accountNumber, checkingAccount.accountNumber)
    }
    
}