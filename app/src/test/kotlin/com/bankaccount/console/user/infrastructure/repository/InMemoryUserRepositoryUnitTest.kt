package com.bankaccount.console.user.infrastructure.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.user.domain.model.User

class InMemoryUserRepositoryUnitTest {
    private lateinit var repository: InMemoryUserRepository

    @Before
    fun setUp() {
        repository = InMemoryUserRepository()
    }

    @Test
    fun `Given a User, when create is called, then the user is stored in the repository`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            passwordHash = "hashed_password"
        )
        
        repository.create(user)
        val retrievedUser = repository.get(user.id)
        
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `Given an empty repository, when get is called, then it returns an empty list`() {
        val users = repository.get()
        
        assertTrue(users.isEmpty())
    }

    @Test
    fun `Given multiple users, when get is called, then it returns all users`() {
        val user1 = User(UUID.randomUUID().toString(), "John", "Doe", "john@example.com", "pass1")
        val user2 = User(UUID.randomUUID().toString(), "Jane", "Smith", "jane@example.com", "pass2")
        
        repository.create(user1)
        repository.create(user2)
        val users = repository.get()
        
        assertEquals(2, users.size)
        assertTrue(users.contains(user1))
        assertTrue(users.contains(user2))
    }

    @Test
    fun `Given a non-existent user id, when get is called, then it returns null`() {
        val retrievedUser = repository.get("non-existent-id")
        
        assertNull(retrievedUser)
    }

    @Test
    fun `Given a User, when delete is called, then the user is removed from the repository`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        repository.delete(user.id)
        val retrievedUser = repository.get(user.id)
        
        assertNull(retrievedUser)
    }

    @Test
    fun `Given a non-existent user id, when delete is called, then it does nothing`() {
        repository.delete("non-existent-id")
        val users = repository.get()
        
        assertTrue(users.isEmpty())
    }

    @Test
    fun `Given a User, when authenticate is called with correct credentials, then it returns the User`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Alice",
            lastName = "Johnson",
            email = "alice.johnson@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        val authenticatedUser = repository.authenticate(user.email, user.passwordHash)
        
        assertEquals(user, authenticatedUser)
    }

    @Test
    fun `Given a User, when authenticate is called with incorrect password, then it returns null`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Bob",
            lastName = "Brown",
            email = "bob.brown@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        val authenticatedUser = repository.authenticate(user.email, "wrong_password")
        
        assertNull(authenticatedUser)
    }

    @Test
    fun `Given a User, when authenticate is called with non-existent email, then it returns null`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Charlie",
            lastName = "Davis",
            email = "charlie.davis@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        val authenticatedUser = repository.authenticate("non-existent@example.com", "hashed_password")
        
        assertNull(authenticatedUser)
    }

    @Test
    fun `Given a User, when update is called, then the changes are reflected when retrieved`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Charlie",
            lastName = "Davis",
            email = "charlie.davis@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        val updatedUser = user.copy(firstName = "Charles")
        
        repository.update(updatedUser)
        val retrievedUser = repository.get(user.id)
        
        assertEquals(updatedUser, retrievedUser)
    }

    @Test
    fun `Given a non-existent user, when update is called, then the repository is unchanged`() {
        val user1 = User(UUID.randomUUID().toString(), "John", "Doe", "john@example.com", "pass1")
        val nonExistentUser = User(UUID.randomUUID().toString(), "Jane", "Smith", "jane@example.com", "pass2")
        
        repository.create(user1)
        repository.update(nonExistentUser)
        
        val users = repository.get()
        assertEquals(1, users.size)
        assertEquals(user1, users[0])
    }

    @Test
    fun `Given users list When search by a wrong id Then it should be null`() {
        val user1 = User(UUID.randomUUID().toString(), "John", "Doe", "john@example.com", "pass1")
        repository.create(user1)
        
        val retrievedUser = repository.get("wrong-id")
        
        assertNull(retrievedUser)
    }
}