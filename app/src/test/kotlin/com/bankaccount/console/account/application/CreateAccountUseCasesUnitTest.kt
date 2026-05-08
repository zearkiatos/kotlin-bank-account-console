package com.bankaccount.console.account.application

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic 
import io.mockk.mockkObject
import io.mockk.unmockkStatic
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.Assert.assertNotNull
import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.mapper.toDomain
import com.bankaccount.console.account.application.ports.input.CreateAccountInputPort
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.mock.account.MockAccountRepository
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.account.domain.utils.BankAccountNumberGenerator



class CreateAccountUseCasesUnitTest {

    private lateinit var createAccountUseCases: CreateAccountUseCases

    @Before
    fun setUp() {
        val accountRepository = MockAccountRepository()
        createAccountUseCases = CreateAccountUseCases(accountRepository)
    }

    @Test
    fun `Given an account When it is valid then create account successfully`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "test-account-id"
        
        val createAccountRequest = CreateAccountRequest(
            userId = "test-user-id",
            accountNumber = "1234567890",
            accountType = AccountType.DEBIT.name
        )

        val account = createAccountUseCases.create(createAccountRequest)

        assertNotNull(account)
        assertEquals("test-account-id", account.id)
        assertEquals("test-user-id", account.userId)
        assertEquals(AccountType.DEBIT.name, account.accountType)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `Given a checking account request with mixed case and spaces, when create is called, then account is created`() {
        mockkStatic(UUID::class)
        mockkObject(BankAccountNumberGenerator)
        every { UUID.randomUUID().toString() } returns "checking-id"
        every { BankAccountNumberGenerator.generate(AccountType.CHECKING) } returns "001-03-00000000-0"

        val createAccountRequest = CreateAccountRequest(
            userId = "test-user-id",
            accountNumber = "ignored",
            accountType = "  checking  "
        )

        val account = createAccountUseCases.create(createAccountRequest)

        assertNotNull(account)
        assertEquals("checking-id", account.id)
        assertEquals(AccountType.CHECKING.name, account.accountType)
        assertEquals("001-03-00000000-0", account.accountNumber)
        verify(exactly = 1) { BankAccountNumberGenerator.generate(AccountType.CHECKING) }
        unmockkObject(BankAccountNumberGenerator)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `Given a credit account request, when create is called, then account is created`() {
        mockkStatic(UUID::class)
        mockkObject(BankAccountNumberGenerator)
        every { UUID.randomUUID().toString() } returns "credit-id"
        every { BankAccountNumberGenerator.generate(AccountType.CREDIT) } returns "001-02-00000000-0"

        val createAccountRequest = CreateAccountRequest(
            userId = "test-user-id",
            accountNumber = "ignored",
            accountType = "CREDIT"
        )

        val account = createAccountUseCases.create(createAccountRequest)

        assertNotNull(account)
        assertEquals("credit-id", account.id)
        assertEquals(AccountType.CREDIT.name, account.accountType)
        assertEquals("001-02-00000000-0", account.accountNumber)
        verify(exactly = 1) { BankAccountNumberGenerator.generate(AccountType.CREDIT) }
        unmockkObject(BankAccountNumberGenerator)
        unmockkStatic(UUID::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank accountType, when create is called, then an IllegalArgumentException is thrown`() {
        val createAccountRequest = CreateAccountRequest(
            userId = "test-user-id",
            accountNumber = "ignored",
            accountType = ""
        )

        createAccountUseCases.create(createAccountRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank userId, when create is called, then an IllegalArgumentException is thrown`() {
        val createAccountRequest = CreateAccountRequest(
            userId = "",
            accountNumber = "ignored",
            accountType = AccountType.DEBIT.name
        )

        createAccountUseCases.create(createAccountRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given invalid accountType, when create is called, then an IllegalArgumentException is thrown`() {
        val createAccountRequest = CreateAccountRequest(
            userId = "test-user-id",
            accountNumber = "ignored",
            accountType = "SAVINGS"
        )

        createAccountUseCases.create(createAccountRequest)
    }
}