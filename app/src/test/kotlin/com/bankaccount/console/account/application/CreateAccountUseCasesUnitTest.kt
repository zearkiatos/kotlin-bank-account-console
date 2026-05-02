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
import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.mapper.toDomain
import com.bankaccount.console.account.application.ports.input.CreateAccountInputPort
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.mock.account.MockAccountRepository
import com.bankaccount.console.shared.account.domain.model.AccountType



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
}