package com.bankaccount.console.user.application.ports.input

interface LoginInputPort {
    fun login(email: String, password: String): String?
}