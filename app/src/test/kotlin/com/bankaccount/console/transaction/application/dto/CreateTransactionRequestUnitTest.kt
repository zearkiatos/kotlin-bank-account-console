package com.bankaccount.console.transaction.application.dto

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.shared.transaction.domain.model.TransactionType

class CreateTransactionRequestUnitTest {
    @Test
    fun `Given a CreateTransactionRequest, when created, then the properties are correctly assigned`() {
        val accountId = UUID.randomUUID().toString()
        val balanceBefore = 1000.0
        val amount = 150.0
        val type = TransactionType.DEPOSIT
        val timestamp = System.currentTimeMillis()

        val request = CreateTransactionRequest(
            accountId = accountId,
            balanceBefore = balanceBefore,
            amount = amount,
            type = type.name,
            timestamp = timestamp
        )

        assertEquals(accountId, request.accountId)
        assertEquals(balanceBefore, request.balanceBefore, 0.0)
        assertEquals(amount, request.amount, 0.0)
        assertEquals(type.name, request.type)
        assertEquals(timestamp, request.timestamp)
    }
}