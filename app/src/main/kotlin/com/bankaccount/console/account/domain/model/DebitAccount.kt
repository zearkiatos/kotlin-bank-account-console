package com.bankaccount.console.account.domain.model

import com.bankaccount.console.account.domain.model.Account

data class DebitAccount(
    override val id: String,
    override val userId: String,
    override val accountNumber: String,
    override val balance: Double,
    override val transactions: List<String>
): Account() {
    
}