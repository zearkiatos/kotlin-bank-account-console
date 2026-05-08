package com.bankaccount.console.account.application

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic 
import io.mockk.unmockkStatic
import org.junit.Assert.assertNotNull

import com.bankaccount.console.mock.account.MockAccountRepository
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import com.bankaccount.console.account.application.AccountUseCases

class AccountUseCasesUnitTest {
    @Test
    fun `Given a userId, when getByUserId is called, then an AccountResponse is returned with correct properties`() {
        val accountRepository = MockAccountRepository()
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        accountRepository.create(
            CheckingAccount(
                id = accountId,
                userId = userId,
                accountNumber = "1234567890",
                balance = 1000.0,
                transactions = mutableListOf()
            )
        )
        val accountUseCases = AccountUseCases(accountRepository)

        val response = accountUseCases.getByUserId(userId)

        assertNotNull(response)
        assertEquals(accountId, response!!.id)
        assertEquals(userId, response.userId)
        assertEquals("1234567890", response.accountNumber)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank userId, when getByUserId is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val accountUseCases = AccountUseCases(accountRepository)

        accountUseCases.getByUserId("")
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `Given an accountId and a new balance, when updateBalance is called with a negative new balance, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            CheckingAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "1234567890",
                balance = 1000.0,
                transactions = mutableListOf()
            )
        )
        val accountUseCases = AccountUseCases(accountRepository)
        accountUseCases.updateBalance(accountId, -500.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given an accountId that does not exist and a new balance, when updateBalance is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val accountUseCases = AccountUseCases(accountRepository)
        accountUseCases.updateBalance("non-existent-account-id", 500.0)
    }

    @Test
    fun `Given an accountId and a new balance, when updateBalance is called with a valid new balance, then the account balance is updated successfully`() {
        val accountRepository = MockAccountRepository()
        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            CheckingAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "1234567890",
                balance = 1000.0,
                transactions = mutableListOf()
            )
        )
        val accountUseCases = AccountUseCases(accountRepository)
        accountUseCases.updateBalance(accountId, 1500.0)
        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(1500.0, updatedAccount!!.balance, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank accountId, when updateBalance is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val accountUseCases = AccountUseCases(accountRepository)

        accountUseCases.updateBalance("", 100.0)
    }

    @Test
    fun `Given a DebitAccount, when updateBalance is called, then the debit balance is updated`() {
        val accountRepository = MockAccountRepository()
        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            DebitAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "9876543210",
                balance = 200.0,
                transactions = mutableListOf()
            )
        )
        val accountUseCases = AccountUseCases(accountRepository)

        accountUseCases.updateBalance(accountId, 350.0)

        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(350.0, updatedAccount!!.balance, 0.0)
    }

    @Test
    fun `Given a CreditAccount, when updateBalance is called, then the credit balance is updated`() {
        val accountRepository = MockAccountRepository()
        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            CreditAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "5555555555",
                balance = 1000.0,
                transactions = mutableListOf()
            )
        )
        val accountUseCases = AccountUseCases(accountRepository)

        accountUseCases.updateBalance(accountId, 1250.0)

        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(1250.0, updatedAccount!!.balance, 0.0)
    }
}