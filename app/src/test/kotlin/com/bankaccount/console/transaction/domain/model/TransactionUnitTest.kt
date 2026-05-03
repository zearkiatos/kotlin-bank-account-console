package com.bankaccount.console.transaction.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.shared.transaction.domain.model.TransactionType

class TransactionUnitTest {
    @Test
    fun `Given a Transaction, when created, then the properties are correctly assigned`() {
        val transactionId = UUID.randomUUID().toString()
        val accountId = UUID.randomUUID().toString()
        val amount = -100.0
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

        assertEquals(transactionId, transaction.id)
        assertEquals(accountId, transaction.accountId)
        assertEquals(amount, transaction.amount, 0.0)
        assertEquals(balanceBefore, transaction.balanceBefore, 0.0)
        assertEquals(type, transaction.type)
        assertEquals(timestamp, transaction.timestamp)
    }
}