package com.bankaccount.console.transaction.application.ports.input

import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse

interface TransactionInputPort {
    fun getByAccountId(accountId: String): List<TransactionResponse>?
}