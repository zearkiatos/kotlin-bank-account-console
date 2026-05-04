package com.bankaccount.console.transaction.application

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.mock.account.MockAccountRepository
import com.bankaccount.console.mock.transaction.MockTransactionRepository
import com.bankaccount.console.account.domain.model.CheckingAccount


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
}