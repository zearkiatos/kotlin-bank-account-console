package com.bankaccount.console.account.infrastructure.repository

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.account.domain.model.DebitAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.infrastructure.repository.InMemoryAccountRepository


class InMemoryAccountRepositoryUnitTest {

    @Test
    fun `Given an InMemoryAccountRepository, when create is called, then the account is stored correctly`() {
        val repository = InMemoryAccountRepository()
        val account = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
        )

        repository.create(account)
        val accountSaved = repository.get(account.id)

        assertEquals(account.id, accountSaved?.id)
        assertEquals(account.userId, accountSaved?.userId)
        assertEquals(account.accountNumber, accountSaved?.accountNumber)
    }

    @Test
    fun `Given an InMemoryAccountRepository, when get is called, then the list of accounts is returned correctly`() {
        val repository = InMemoryAccountRepository()
        val account1 = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
        )
        val account2 = CreditAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "0987654321",
        )

        repository.create(account1)
        repository.create(account2)
        val accountsSaved = repository.get()

        assertEquals(2, accountsSaved.size)
        for (account in accountsSaved) {
            when (account.id) {
                account1.id -> {
                    assertEquals(account1.userId, account.userId)
                    assertEquals(account1.accountNumber, account.accountNumber)
                }
                account2.id -> {
                    assertEquals(account2.userId, account.userId)
                    assertEquals(account2.accountNumber, account.accountNumber)
                }
                else -> throw IllegalStateException("Unexpected account id: ${account.id}")
            }
        }
    }

    @Test
    fun `Given an InMemoryAccountRepository, when delete is called, then the account is removed correctly`() {
        val repository = InMemoryAccountRepository()
        val account = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
        )

        repository.create(account)
        repository.delete(account.id)
        val accountDeleted = repository.get(account.id)

        assertNull(accountDeleted)
    }

    @Test
    fun `Given an InMemoryAccountRepository, when update is called, then the account is updated correctly`() {
        val repository = InMemoryAccountRepository()
        val account = CheckingAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "1234567890",
        )

        repository.create(account)
        val updatedAccount = account.copy(accountNumber = "0987654321")
        repository.update(updatedAccount)
        val accountUpdated = repository.get(account.id)

        assertEquals(updatedAccount.id, accountUpdated?.id)
        assertEquals(updatedAccount.userId, accountUpdated?.userId)
        assertEquals(updatedAccount.accountNumber, accountUpdated?.accountNumber)
    }

}