package com.bankaccount.console.account.domain.model

import com.backaccount.console.account.domain.model.Account

data class DebitAccount(
    override val id: String,
    override val userId: String
): Account() {
    
}