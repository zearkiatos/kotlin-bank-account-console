package com.bankaccount.console.account.domain.utils

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.account.domain.utils.BankAccountNumberGenerator

class BankAccountNumberGeneratorUnitTest {
    @Test
    fun `Given a BankAccountNumberGenerator, when generate is called, then it returns a valid account number`() {
        val accountType = AccountType.CHECKING
        val accountNumber = BankAccountNumberGenerator.generate(accountType)

        assertNotNull(accountNumber)
        assertTrue(BankAccountNumberGenerator.isValid(accountNumber))
    }
}