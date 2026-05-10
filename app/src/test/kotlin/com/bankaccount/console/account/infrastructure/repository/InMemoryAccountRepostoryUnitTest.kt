package com.bankaccount.console.account.infrastructure.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import com.bankaccount.console.account.domain.model.CreditAccount

class InMemoryAccountRepositoryUnitTest {
    private lateinit var repository: InMemoryAccountRepository

    @Before
    fun setUp() {
        repository = InMemoryAccountRepository()
    }

    @Test
    fun `Given an Account, when create is called, then the account is stored in the repository`() {
        val account = CheckingAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "001-01-12345678-5",
            balance = 1000.0,
            transactions = mutableListOf()
        )

        repository.create(account)
        val retrievedAccount = repository.get(account.id)

        assertEquals(account, retrievedAccount)
    }

    @Test
    fun `Given an empty repository, when get is called, then it returns an empty list`() {
        val accounts = repository.get()

        assertTrue(accounts.isEmpty())
    }

    @Test
    fun `Given multiple accounts, when get is called, then it returns all accounts`() {
        val account1 = DebitAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-02-12345678-5", 500.0, mutableListOf())
        val account2 = CreditAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-03-87654321-9", 2000.0, mutableListOf())
        val account3 = CheckingAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-01-55555555-3", 1500.0, mutableListOf())

        repository.create(account1)
        repository.create(account2)
        repository.create(account3)
        val accounts = repository.get()

        assertEquals(3, accounts.size)
        assertTrue(accounts.contains(account1))
        assertTrue(accounts.contains(account2))
        assertTrue(accounts.contains(account3))
    }

    @Test
    fun `Given a non-existent account id, when get is called, then it returns null`() {
        val retrievedAccount = repository.get("non-existent-id")

        assertNull(retrievedAccount)
    }

    @Test
    fun `Given an Account, when update is called, then the changes are reflected when retrieved`() {
        val userId = UUID.randomUUID().toString()
        val account = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = userId,
            accountNumber = "001-02-12345678-5",
            balance = 1000.0,
            transactions = mutableListOf()
        )
        repository.create(account)

        val updatedAccount = account.copy(balance = 2000.0)
        repository.update(updatedAccount)
        val retrievedAccount = repository.get(account.id)

        assertEquals(updatedAccount, retrievedAccount)
        assertEquals(2000.0, retrievedAccount!!.balance, 0.0)
    }

    @Test
    fun `Given a non-existent account, when update is called, then the repository is unchanged`() {
        val account1 = CheckingAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-01-12345678-5", 1000.0, mutableListOf())
        val nonExistentAccount = CreditAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-03-87654321-9", 5000.0, mutableListOf())

        repository.create(account1)
        repository.update(nonExistentAccount)

        val accounts = repository.get()
        assertEquals(1, accounts.size)
        assertEquals(account1, accounts[0])
    }

    @Test
    fun `Given an Account, when delete is called, then the account is removed from the repository`() {
        val account = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "001-02-12345678-5",
            balance = 500.0,
            transactions = mutableListOf()
        )
        repository.create(account)

        repository.delete(account.id)
        val retrievedAccount = repository.get(account.id)

        assertNull(retrievedAccount)
    }

    @Test
    fun `Given a non-existent account id, when delete is called, then it does nothing`() {
        val account = CheckingAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-01-12345678-5", 1000.0, mutableListOf())
        repository.create(account)

        repository.delete("non-existent-id")
        val accounts = repository.get()

        assertEquals(1, accounts.size)
    }

    @Test
    fun `Given multiple accounts with same userId, when getByUserId is called, then it returns only those accounts`() {
        val sharedUserId = UUID.randomUUID().toString()
        val otherUserId = UUID.randomUUID().toString()

        val account1 = DebitAccount(UUID.randomUUID().toString(), sharedUserId, "001-02-12345678-5", 500.0, mutableListOf())
        val account2 = CheckingAccount(UUID.randomUUID().toString(), sharedUserId, "001-01-87654321-9", 1000.0, mutableListOf())
        val account3 = CreditAccount(UUID.randomUUID().toString(), otherUserId, "001-03-55555555-3", 2000.0, mutableListOf())

        repository.create(account1)
        repository.create(account2)
        repository.create(account3)

        val userAccount = repository.getByUserId(sharedUserId)

        assertEquals(account1, userAccount)
    }

    @Test
    fun `Given a userId with no accounts, when getByUserId is called, then it returns an empty list`() {
        val account = DebitAccount(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "001-02-12345678-5", 500.0, mutableListOf())
        repository.create(account)

        val userAccount = repository.getByUserId("non-existent-user-id")

        assertNull(userAccount)
    }

    @Test
    fun `Given multiple accounts, when getByUserId is called, then all accounts for that user are returned`() {
        val userId = UUID.randomUUID().toString()

        val account1 = CheckingAccount(UUID.randomUUID().toString(), userId, "001-01-12345678-5", 1000.0, mutableListOf())
        val account2 = DebitAccount(UUID.randomUUID().toString(), userId, "001-02-87654321-9", 500.0, mutableListOf())
        val account3 = CreditAccount(UUID.randomUUID().toString(), userId, "001-03-55555555-3", 2000.0, mutableListOf())

        repository.create(account1)
        repository.create(account2)
        repository.create(account3)

        val userAccount = repository.getByUserId(userId)

        assertEquals(account1, userAccount)
    }

    @Test
    fun `Given an account with transactions, when create is called, then transactions are preserved`() {
        val transactions = mutableListOf("T1", "T2", "T3")
        val account = DebitAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "001-02-12345678-5",
            balance = 1000.0,
            transactions = transactions
        )

        repository.create(account)
        val retrievedAccount = repository.get(account.id)

        assertEquals(3, retrievedAccount?.transactions?.size)
        assertEquals(transactions, retrievedAccount?.transactions)
    }

    @Test
    fun `Given an account list, When get is calling with a not exist account id, Then it should return null`() {
        val account = CheckingAccount(
            id = UUID.randomUUID().toString(),
            userId = UUID.randomUUID().toString(),
            accountNumber = "001-01-12345678-5",
            balance = 1000.0,
            transactions = mutableListOf()
        )
        repository.create(account)

        val retrievedAccount = repository.get("non-existent-id")

        assertNull(retrievedAccount)
    }
}