package com.bankaccount.console.transaction.infrastructure.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.transaction.domain.model.Transaction
import com.bankaccount.console.shared.transaction.domain.model.TransactionType
import com.bankaccount.console.transaction.infrastructure.repository.InMemoryTransactionRepository

class InMemoryTransactionRepositoryUnitTest {
    private val transactionRepository: InMemoryTransactionRepository = InMemoryTransactionRepository()

    @Test
    fun `Given a Transaction, when created, then it can be retrieved by ID`() {
        val transaction = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = UUID.randomUUID().toString(),
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )

        transactionRepository.create(transaction)

        val retrievedTransaction = transactionRepository.get(transaction.id)
        assertEquals(transaction, retrievedTransaction)
    }

    @Test
    fun `Given multiple Transactions, when retrieved by account ID, then the correct transactions are returned`() {
        val transactionAccountId1 = UUID.randomUUID().toString()
        val transactionAccountId2 = UUID.randomUUID().toString()
        val transaction1 = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = transactionAccountId1,
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )
        val transaction2 = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = transactionAccountId1,
            amount = -50.0,
            balanceBefore = 1100.0,
            type = TransactionType.WITHDRAWAL,
            timestamp = System.currentTimeMillis()
        )
        val transaction3 = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = transactionAccountId2,
            amount = 200.0,
            balanceBefore = 500.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )

        transactionRepository.create(transaction1)
        transactionRepository.create(transaction2)
        transactionRepository.create(transaction3)

        val transactionsAccount1 = transactionRepository.getByAccountId(transaction1.accountId)
        assertEquals(2, transactionsAccount1.size)
        assertTrue(transactionsAccount1.contains(transaction1))
        assertTrue(transactionsAccount1.contains(transaction2))

        val transactionsAccount2 = transactionRepository.getByAccountId(transaction3.accountId)
        assertEquals(1, transactionsAccount2.size)
        assertTrue(transactionsAccount2.contains(transaction3))
    }

    @Test
    fun `Given Transactions, when it is getting, then it should return a list of transactions`() {
        val transaction1 = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = "account1",
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )
        val transaction2 = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = "account2",
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )
        transactionRepository.create(transaction1)
        transactionRepository.create(transaction2)

        val transactions = transactionRepository.get()
        
        assertEquals(2, transactions.size)
        assertTrue(transactions.contains(transaction1))
        assertTrue(transactions.contains(transaction2))
    }

    @Test
    fun `Given a non-existent Transaction ID, when get is called, then null is returned`() {
        val retrievedTransaction = transactionRepository.get("non-existent-id")
        
        assertNull(retrievedTransaction)
    }

    @Test
    fun `Given a trasactionId When it is getting Thet it should return the specific transaction`() {
        val transaction = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = "account1",
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )

        transactionRepository.create(transaction)
        val retrievedTransaction = transactionRepository.get(transaction.id)
        
        assertEquals(transaction, retrievedTransaction)
    }

    @Test
    fun `Given a trasactionId When it is getting with a wrong id Thet it should not return the specific transaction`() {
        val transaction = Transaction(
            id = UUID.randomUUID().toString(),
            accountId = "account1",
            amount = 100.0,
            balanceBefore = 1000.0,
            type = TransactionType.DEPOSIT,
            timestamp = System.currentTimeMillis()
        )

        transactionRepository.create(transaction)
        val retrievedTransaction = transactionRepository.get("wrong-id")
        
        assertNull(retrievedTransaction)
    }

    @Test
    fun `Given an account ID with no transactions, when getByAccountId is called, then an empty list is returned`() {
        val transactions = transactionRepository.getByAccountId("account-without-transactions")
        
        assertTrue(transactions.isEmpty())
    }
}