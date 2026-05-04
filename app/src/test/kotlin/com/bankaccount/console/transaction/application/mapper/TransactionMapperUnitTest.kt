package com.bankaccount.console.transaction.application.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.domain.model.Transaction
import com.bankaccount.console.shared.transaction.domain.model.TransactionType


class TransactionMapperUnitTest {
    @Test
    fun `Given a Transaction, when mapped to TransactionResponse, then the properties are correctly assigned`() {
        val transactionId = UUID.randomUUID().toString()
        val accountId = UUID.randomUUID().toString()
        val amount = 150.0
        val balanceBefore = 1000.0
        val type = TransactionType.DEPOSIT
        val timestamp = System.currentTimeMillis()

        val transaction = Transaction(
            id = transactionId,
            accountId = accountId,
            amount = amount,
            balanceBefore = balanceBefore,
            type = type,
            timestamp = timestamp
        )

        val transactionResponse = transaction.toResponse()

        assertEquals(transactionId, transactionResponse.id)
        assertEquals(accountId, transactionResponse.accountId)
        assertEquals(amount, transactionResponse.amount, 0.0)
        assertEquals(balanceBefore, transactionResponse.balanceBefore, 0.0)
        assertEquals(type.name, transactionResponse.type)
        assertEquals(timestamp, transactionResponse.timestamp)
    }

    @Test
    fun `Given a Transaction with a different type, when mapped to TransactionResponse, then the type is correctly assigned`() {
            val transactionId = UUID.randomUUID().toString()
            val accountId = UUID.randomUUID().toString()
            val amount = -50.0
            val balanceBefore = 1000.0
            val type = TransactionType.WITHDRAWAL
            val timestamp = System.currentTimeMillis()
    
            val transaction = Transaction(
                id = transactionId,
                accountId = accountId,
                amount = amount,
                balanceBefore = balanceBefore,
                type = type,
                timestamp = timestamp
            )
    
            val transactionResponse = transaction.toResponse()
    
            assertEquals(type.name, transactionResponse.type)
        }
}