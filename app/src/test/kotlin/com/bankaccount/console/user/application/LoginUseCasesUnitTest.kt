package com.bankaccount.console.user.application

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic 
import io.mockk.unmockkStatic
import org.junit.Assert.assertNotNull

import com.bankaccount.console.mock.user.MockUserRepository
import com.bankaccount.console.user.domain.model.User
import com.bankaccount.console.user.application.LoginUseCases
import com.bankaccount.console.user.domain.utils.PasswordHasher

class LoginUseCasesUnitTest {
    @Test
    fun `Given a userId, when login is called, then a UserResponse is returned with correct properties`() {
        val userRepository = MockUserRepository()
        val userId = UUID.randomUUID().toString()
        val passwordHash = PasswordHasher.hash("password")
        userRepository.create(
            User(
                id = userId,
                firstName = "Test User",
                lastName = "",
                email = "testuser@example.com",
                passwordHash = passwordHash
            )
        )
        
        val loginUseCases = LoginUseCases(userRepository)
        val id = loginUseCases.login("testuser@example.com", "password")
        
        assertNotNull(id)
        assertEquals(userId, id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a userId that does not exist, when login is called, then an IllegalArgumentException is thrown`() {
        val userRepository = MockUserRepository()
        val loginUseCases = LoginUseCases(userRepository)
        
        loginUseCases.login("non-existent-user-id", "password")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank email, when login is called, then an IllegalArgumentException is thrown`() {
        val userRepository = MockUserRepository()
        val loginUseCases = LoginUseCases(userRepository)

        loginUseCases.login("", "password")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given blank password, when login is called, then an IllegalArgumentException is thrown`() {
        val userRepository = MockUserRepository()
        val loginUseCases = LoginUseCases(userRepository)

        loginUseCases.login("testuser@example.com", "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given wrong password, when login is called, then an IllegalArgumentException is thrown`() {
        val userRepository = MockUserRepository()
        val userId = UUID.randomUUID().toString()
        val passwordHash = PasswordHasher.hash("password")
        userRepository.create(
            User(
                id = userId,
                firstName = "Test User",
                lastName = "",
                email = "testuser@example.com",
                passwordHash = passwordHash
            )
        )

        val loginUseCases = LoginUseCases(userRepository)

        loginUseCases.login("testuser@example.com", "wrong-password")
    }
}