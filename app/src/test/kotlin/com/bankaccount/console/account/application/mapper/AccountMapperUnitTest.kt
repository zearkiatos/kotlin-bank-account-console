package com.bankaccount.console.account.application.mapper

import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class AccountMapperUnitTest {
    @Test
    fun `Given a CreateAccountRequest, when toDomain is called, then the correct Account domain model is returned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = AccountType.DEBIT.name

        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType
        )

        val accountDomainModel = createAccountRequest.toDomain()

        assertEquals(accountId, accountDomainModel.id)
        assertEquals(userId, accountDomainModel.userId)
        assertEquals(accountNumber, accountDomainModel.accountNumber)
        assertTrue(accountDomainModel is DebitAccount)
    }

    @Test
    fun `Given a CreateAccountRequest, when toDomain is called with a CREDIT account type, then the correct CreditAccount domain model is returned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"
        val accountType = AccountType.CREDIT.name

        val createAccountRequest = CreateAccountRequest(
            id = accountId,
            userId = userId,
            accountNumber = accountNumber,
            accountType = accountType
        )

        val accountDomainModel = createAccountRequest.toDomain()

        assertEquals(accountId, accountDomainModel.id)
        assertEquals(userId, accountDomainModel.userId)
        assertEquals(accountNumber, accountDomainModel.accountNumber)
        assertTrue(accountDomainModel is CreditAccount)
    }

    @Test
    fun `Given a CreateAccountRequest, when toDomain is called with a CHECKING account type, then the correct CheckingAccount domain model is returned`() {
            val accountId = UUID.randomUUID().toString()
            val userId = UUID.randomUUID().toString()
            val accountNumber = "1234567890"
            val accountType = AccountType.CHECKING.name
    
            val createAccountRequest = CreateAccountRequest(
                id = accountId,
                userId = userId,
                accountNumber = accountNumber,
                accountType = accountType
            )
    
            val accountDomainModel = createAccountRequest.toDomain()
    
            assertEquals(accountId, accountDomainModel.id)
            assertEquals(userId, accountDomainModel.userId)
            assertEquals(accountNumber, accountDomainModel.accountNumber)
            assertTrue(accountDomainModel is CheckingAccount)
        }

    @Test
    fun `Given an Account domain model, when toResponse is called, then the correct AccountResponse is returned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"

        val checkingAccount = CheckingAccount(id = accountId, userId = userId, accountNumber = accountNumber)

        val accountResponse = checkingAccount.toResponse()

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(AccountType.CHECKING.name, accountResponse.accountType)
    }

    @Test
    fun `Given a CreditAccount domain model, when toResponse is called, then the correct AccountResponse is returned`() {
        val accountId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val accountNumber = "1234567890"

        val creditAccount = CreditAccount(id = accountId, userId = userId, accountNumber = accountNumber)

        val accountResponse = creditAccount.toResponse()

        assertEquals(accountId, accountResponse.id)
        assertEquals(userId, accountResponse.userId)
        assertEquals(accountNumber, accountResponse.accountNumber)
        assertEquals(AccountType.CREDIT.name, accountResponse.accountType)
    }

    @Test
    fun `Given a DebitAccount domain model, when toResponse is called, then the correct AccountResponse is returned`() {
            val accountId = UUID.randomUUID().toString()
            val userId = UUID.randomUUID().toString()
            val accountNumber = "1234567890"
    
            val debitAccount = DebitAccount(id = accountId, userId = userId, accountNumber = accountNumber)
    
            val accountResponse = debitAccount.toResponse()
    
            assertEquals(accountId, accountResponse.id)
            assertEquals(userId, accountResponse.userId)
            assertEquals(accountNumber, accountResponse.accountNumber)
            assertEquals(AccountType.DEBIT.name, accountResponse.accountType)
        }
}