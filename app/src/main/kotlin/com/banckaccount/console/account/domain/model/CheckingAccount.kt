package com.backaccount.console.account.domain.model

import com.backaccount.console.account.domain.model.Account

data class CheckingAccount(
    override val id: String,
    override val userId: String): Account() 
{
    
}