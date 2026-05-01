package com.backaccount.console.account.domain.model

sealed class Account {
    abstract val id: String
    abstract val userId: String
}