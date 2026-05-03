package com.bankaccount.console.transaction.application.ports.input

import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse

interface CreateTransactionInputPort {
    fun create(request: CreateTransactionRequest): TransactionResponse
}