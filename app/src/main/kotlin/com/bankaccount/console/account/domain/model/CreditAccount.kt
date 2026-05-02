package com.bankaccount.console.account.domain.model

import com.bankaccount.console.account.domain.model.Account

data class CreditAccount(
    override val id: String,
    override val userId: String,
    override val accountNumber: String
): Account() {
    
}