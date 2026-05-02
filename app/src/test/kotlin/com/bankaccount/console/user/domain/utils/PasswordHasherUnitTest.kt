package com.bankaccount.console.user.domain.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class PasswordHasherUnitTest {
    @Test
    fun `Given a password, when hash is called, then the password is hashed correctly`() {
        val password = "my_secure_password"
        val hash = PasswordHasher.hash(password)

        assertEquals(60, hash.length)
    }

    @Test
    fun `Given a password and its hash, when verify is called, then the password is verified correctly`() {
        val password = "my_secure_password"
        val hash = PasswordHasher.hash(password)
        
        val isValid = PasswordHasher.verify(password, hash)

        assertTrue(isValid)
    }

    @Test
    fun `Given a password and its hash, when password is incorrect, then the password is verified as invalid`() {
        val original = "my_secure_password"
        val hash = PasswordHasher.hash(original)
        val incorrectPassword = "wrong_password"

        val isValid = PasswordHasher.verify(incorrectPassword, hash)

        assertFalse(isValid)
    }
}