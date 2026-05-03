package com.bankaccount.console.user.application

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.domain.repository.UserRepository

class CreateUserUseCasesUnitTest {

    private lateinit var createUserUseCases: CreateUserUseCases
    private lateinit var mockUserRepository: UserRepository

    @Before
    fun setUp() {
        mockUserRepository = mockk(relaxed = true)
        createUserUseCases = CreateUserUseCases(mockUserRepository)
    }

    @Test
    fun `Given a valid user request, when create is called, then user is created successfully`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "test-user-id"
        
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        val userId = createUserUseCases.create(createUserRequest)
        
        assertNotNull(userId)
        assertEquals("test-user-id", userId)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `Given a valid user with different names, when create is called, then user is created`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "user-id-2"
        
        val createUserRequest = CreateUserRequest(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            password = "securePass"
        )
        
        val userId = createUserUseCases.create(createUserRequest)
        
        assertEquals("user-id-2", userId)
        unmockkStatic(UUID::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank firstName, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank firstName with spaces, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "   ",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank lastName, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank lastName with spaces, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "   ",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank email, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank email with spaces, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "   ",
            password = "password123"
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank password, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = ""
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given a user with blank password with spaces, when create is called, then IllegalArgumentException is thrown`() {
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "   "
        )
        
        createUserUseCases.create(createUserRequest)
    }

    @Test
    fun `Given multiple valid user requests, when create is called for each, then all return unique IDs`() {
        mockkStatic(UUID::class)
        
        val ids = mutableListOf<String>()
        for (i in 1..3) {
            every { UUID.randomUUID().toString() } returns "user-id-$i"
            
            val createUserRequest = CreateUserRequest(
                firstName = "User$i",
                lastName = "Last$i",
                email = "user$i@example.com",
                password = "pass$i"
            )
            
            val userId = createUserUseCases.create(createUserRequest)
            ids.add(userId)
        }
        
        assertEquals(3, ids.size)
        unmockkStatic(UUID::class)
    }
}