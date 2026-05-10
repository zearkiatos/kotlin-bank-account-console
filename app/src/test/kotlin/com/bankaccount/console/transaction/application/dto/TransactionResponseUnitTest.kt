package com.bankaccount.console.transaction.application.dto

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.shared.transaction.domain.model.TransactionType

class TransactionResponseUnitTest {
    @Test
    fun `Given a TransactionResponse, when created, then the properties are correctly assigned`() {
        val id = UUID.randomUUID().toString()
        val accountId = UUID.randomUUID().toString()
        val amount = 150.0
        val balanceBefore = 1000.0
        val type = TransactionType.DEPOSIT
        val timestamp = System.currentTimeMillis()

        val transactionResponse = TransactionResponse(
            id = id,
            accountId = accountId,
            amount = amount,
            balanceBefore = balanceBefore,
            type = type.name,
            timestamp = timestamp
        )

        assertEquals(id, transactionResponse.id)
        assertEquals(accountId, transactionResponse.accountId)
        assertEquals(amount, transactionResponse.amount, 0.0)
        assertEquals(balanceBefore, transactionResponse.balanceBefore, 0.0)
        assertEquals(type.name, transactionResponse.type)
        assertEquals(timestamp, transactionResponse.timestamp)
    }

    @Test
    fun `Given a TransactionResponse, when created with a different type, then the type is correctly assigned`() {
        val id = UUID.randomUUID().toString()
        val accountId = UUID.randomUUID().toString()
        val amount = -50.0
        val balanceBefore = 1000.0
        val type = TransactionType.WITHDRAWAL
        val timestamp = System.currentTimeMillis()

        val transactionResponse = TransactionResponse(
            id = id,
            accountId = accountId,
            amount = amount,
            balanceBefore = balanceBefore,
            type = type.name,
            timestamp = timestamp
        )

        assertEquals(type.name, transactionResponse.type)
    }
}