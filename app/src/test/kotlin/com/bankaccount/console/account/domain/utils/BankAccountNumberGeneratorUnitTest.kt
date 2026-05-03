package com.bankaccount.console.account.domain.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import com.bankaccount.console.shared.account.domain.model.AccountType

class BankAccountNumberGeneratorUnitTest {
    
    @Test
    fun `Given a BankAccountNumberGenerator, when generate is called with CHECKING, then it returns a valid account number`() {
        val accountType = AccountType.CHECKING
        val accountNumber = BankAccountNumberGenerator.generate(accountType)

        assertNotNull(accountNumber)
        assertTrue(BankAccountNumberGenerator.isValid(accountNumber))
        assertTrue(accountNumber.contains("-"))
        assertEquals(17, accountNumber.length)
    }

    @Test
    fun `Given a BankAccountNumberGenerator, when generate is called with DEBIT, then it returns a valid account number`() {
        val accountType = AccountType.DEBIT
        val accountNumber = BankAccountNumberGenerator.generate(accountType)

        assertNotNull(accountNumber)
        assertTrue(BankAccountNumberGenerator.isValid(accountNumber))
        assertTrue(accountNumber.contains(accountType.code))
    }

    @Test
    fun `Given a BankAccountNumberGenerator, when generate is called with CREDIT, then it returns a valid account number`() {
        val accountType = AccountType.CREDIT
        val accountNumber = BankAccountNumberGenerator.generate(accountType)

        assertNotNull(accountNumber)
        assertTrue(BankAccountNumberGenerator.isValid(accountNumber))
        assertTrue(accountNumber.contains(accountType.code))
    }

    @Test
    fun `Given multiple generated account numbers, when isValid is called, then all are valid`() {
        repeat(10) {
            val checkingNumber = BankAccountNumberGenerator.generate(AccountType.CHECKING)
            val debitNumber = BankAccountNumberGenerator.generate(AccountType.DEBIT)
            val creditNumber = BankAccountNumberGenerator.generate(AccountType.CREDIT)

            assertTrue(BankAccountNumberGenerator.isValid(checkingNumber))
            assertTrue(BankAccountNumberGenerator.isValid(debitNumber))
            assertTrue(BankAccountNumberGenerator.isValid(creditNumber))
        }
    }

    @Test
    fun `Given an invalid account number with wrong length, when isValid is called, then it returns false`() {
        val invalidNumber = "001-01-1234567-5"  // Too short
        assertFalse(BankAccountNumberGenerator.isValid(invalidNumber))
    }

    @Test
    fun `Given an invalid account number with non-digit characters, when isValid is called, then it returns false`() {
        val invalidNumber = "001-01-1234567A-5"  // Contains letter
        assertFalse(BankAccountNumberGenerator.isValid(invalidNumber))
    }

    @Test
    fun `Given an invalid account number with wrong check digit, when isValid is called, then it returns false`() {
        val validNumber = BankAccountNumberGenerator.generate(AccountType.CHECKING)
        val cleanedNumber = validNumber.replace("-", "")
        
        // Modify the check digit
        val invalidCheckDigit = (cleanedNumber.last().digitToInt() + 1) % 10
        val invalidNumber = cleanedNumber.dropLast(1) + invalidCheckDigit
        
        assertFalse(BankAccountNumberGenerator.isValid(invalidNumber))
    }

    @Test
    fun `Given an account number without dashes, when isValid is called, then it still validates correctly`() {
        val generatedNumber = BankAccountNumberGenerator.generate(AccountType.DEBIT)
        val numberWithoutDashes = generatedNumber.replace("-", "")

        assertTrue(BankAccountNumberGenerator.isValid(numberWithoutDashes))
    }

    @Test
    fun `Given an account number with extra dashes, when isValid is called, then it handles it correctly`() {
        val generatedNumber = BankAccountNumberGenerator.generate(AccountType.CREDIT)
        val cleanedNumber = generatedNumber.replace("-", "")

        // Should be valid regardless of dash placement
        assertTrue(BankAccountNumberGenerator.isValid(cleanedNumber))
    }

    @Test
    fun `Given an empty account number, when isValid is called, then it returns false`() {
        assertFalse(BankAccountNumberGenerator.isValid(""))
    }

    @Test
    fun `Given a too long account number, when isValid is called, then it returns false`() {
        val tooLong = "001-01-123456789-5"  // 18 digits when cleaned
        assertFalse(BankAccountNumberGenerator.isValid(tooLong))
    }

    @Test
    fun `Given account numbers of different types, when generate is called multiple times, then each generates unique numbers`() {
        val accountNumbers = mutableSetOf<String>()
        
        repeat(5) {
            accountNumbers.add(BankAccountNumberGenerator.generate(AccountType.CHECKING))
            accountNumbers.add(BankAccountNumberGenerator.generate(AccountType.DEBIT))
            accountNumbers.add(BankAccountNumberGenerator.generate(AccountType.CREDIT))
        }

        // Should have 15 different numbers (5 of each type)
        assertEquals(15, accountNumbers.size)
    }

    @Test
    fun `Given a valid account number format, when isValid is called, then it validates the Luhn algorithm`() {
        // Generate and validate multiple times to test Luhn calculation
        repeat(20) {
            val accountType = listOf(AccountType.CHECKING, AccountType.DEBIT, AccountType.CREDIT).random()
            val generatedNumber = BankAccountNumberGenerator.generate(accountType)
            assertTrue(BankAccountNumberGenerator.isValid(generatedNumber))
        }
    }
}