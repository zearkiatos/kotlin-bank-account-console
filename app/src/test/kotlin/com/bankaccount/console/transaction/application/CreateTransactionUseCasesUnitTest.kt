package com.bankaccount.console.transaction.application

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.mock.account.MockAccountRepository
import com.bankaccount.console.mock.transaction.MockTransactionRepository
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import org.junit.Assert.assertNotNull


class CreateTransactionUseCasesUnitTest {
    @Test
    fun `Given a CreateTransactionRequest, when createTransaction is called, then a TransactionResponse is returned with correct properties`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()

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
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = accountId,
            amount = 100.0,
            type = "DEPOSIT",
            balanceBefore = 1000.0,
            timestamp = System.currentTimeMillis()
        )

        val response = createTransactionUseCases.create(request)

        assertEquals(request.accountId, response.accountId)
        assertEquals(request.amount, response.amount, 0.0)
        assertEquals(request.type, response.type)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a request with non-positive amount, when create is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = UUID.randomUUID().toString(),
            amount = 0.0,
            type = "DEPOSIT",
            balanceBefore = 100.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a request with blank accountId, when create is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = "",
            amount = 10.0,
            type = "DEPOSIT",
            balanceBefore = 100.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a request with blank type, when create is called, then an IllegalArgumentException is thrown`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = UUID.randomUUID().toString(),
            amount = 10.0,
            type = "",
            balanceBefore = 100.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)
    }

    @Test
    fun `Given a debit account, when deposit is created, then debit balance is updated`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()

        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            DebitAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "2222222222",
                balance = 200.0,
                transactions = mutableListOf()
            )
        )
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = accountId,
            amount = 50.0,
            type = "DEPOSIT",
            balanceBefore = 200.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)

        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(250.0, updatedAccount!!.balance, 0.0)
        assertEquals(1, updatedAccount.transactions.size)
    }

    @Test
    fun `Given a credit account, when deposit is created, then credit balance is updated`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()

        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            CreditAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "3333333333",
                balance = 500.0,
                transactions = mutableListOf()
            )
        )
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = accountId,
            amount = 100.0,
            type = "DEPOSIT",
            balanceBefore = 500.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)

        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(600.0, updatedAccount!!.balance, 0.0)
        assertEquals(1, updatedAccount.transactions.size)
    }

    @Test
    fun `Given a checking account with sufficient funds, when withdrawal is created, then balance is updated`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()

        val accountId = UUID.randomUUID().toString()
        accountRepository.create(
            CheckingAccount(
                id = accountId,
                userId = UUID.randomUUID().toString(),
                accountNumber = "1234567890",
                balance = 300.0,
                transactions = mutableListOf()
            )
        )
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = accountId,
            amount = 120.0,
            type = "withdrawal",
            balanceBefore = 300.0,
            timestamp = System.currentTimeMillis()
        )

        createTransactionUseCases.create(request)

        val updatedAccount = accountRepository.get(accountId)
        assertNotNull(updatedAccount)
        assertEquals(180.0, updatedAccount!!.balance, 0.0)
        assertEquals(1, updatedAccount.transactions.size)
    }

    @Test
    fun `Given a request for a non-existent account, when create is called, then a response is returned`() {
        val accountRepository = MockAccountRepository()
        val transactionRepository = MockTransactionRepository()
        val createTransactionUseCases = CreateTransactionUseCases(transactionRepository, accountRepository)

        val request = CreateTransactionRequest(
            accountId = UUID.randomUUID().toString(),
            amount = 25.0,
            type = "DEPOSIT",
            balanceBefore = 0.0,
            timestamp = System.currentTimeMillis()
        )

        val response = createTransactionUseCases.create(request)

        assertEquals(request.accountId, response.accountId)
        assertEquals(request.amount, response.amount, 0.0)
        assertEquals(request.type, response.type)
    }
}