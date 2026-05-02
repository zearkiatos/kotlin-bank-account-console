package com.bankaccount.console.user.domain.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
    private const val COST = 12

    fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(COST))
    }

    fun verify(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}