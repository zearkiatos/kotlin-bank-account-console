package com.bankaccount.console.user.domain.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.mock.user.MockUserRepository
import com.bankaccount.console.user.domain.model.User

class UserRepositoryUnitTest {
    private val userRepository: UserRepository = MockUserRepository()

    @Test
    fun `Given a User, when created, then it can be retrieved by ID`() {
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId,
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            passwordHash = "hashed_password"
        )
        userRepository.create(user)
        val retrievedUser = userRepository.get(userId)
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `Given a User, when deleted, then it cannot be retrieved by ID`() {
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId,
            firstName = "Alice",
            lastName = "Johnson",
            email = "alice.johnson@example.com",
            passwordHash = "hashed_password"
        )
        userRepository.create(user)
       
        userRepository.delete(userId)
       
        val retrievedUser = userRepository.get(userId)
        assertNull(retrievedUser)
    }

    @Test
    fun `Given a email, when getUserByEmail is called, then it returns the correct User`() {
        val userId = UUID.randomUUID().toString()
        val email = "alice.johnson@example.com"
        val user = User(
            id = userId,
            firstName = "Alice",
            lastName = "Johnson",
            email = email,
            passwordHash = "hashed_password"
        )
        userRepository.create(user)

        val retrievedUser = userRepository.getUserByEmail(email)
        
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `Given a non-existent email, when there are a user and getUserByEmail is called, then it returns null`() {
        val userId = UUID.randomUUID().toString()
        val email = "alice.johnson@example.com"
        val user = User(
            id = userId,
            firstName = "Alice",
            lastName = "Johnson",
            email = email,
            passwordHash = "hashed_password"
        )
        userRepository.create(user)
        
        val retrievedUser = userRepository.getUserByEmail("non.existent@example.com")
            
        assertNull(retrievedUser)
    }

    @Test
    fun `Given a non-existent email, when getUserByEmail is called, then it returns null`() {
            val retrievedUser = userRepository.getUserByEmail("non.existent@example.com")
            
            assertNull(retrievedUser)
    }

    @Test
    fun `Given a User, when updated, then the changes are reflected when retrieved`() {
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId,
            firstName = "Bob",
            lastName = "Brown",
            email = "bob.brown@example.com",
            passwordHash = "hashed_password"
        )
        userRepository.create(user)

        val updatedUser = user.copy(firstName = "Robert")
        
        userRepository.update(updatedUser)
        val retrievedUser = userRepository.get(userId)
        
        assertEquals(updatedUser, retrievedUser)
    }

    @Test
    fun `Given multiple Users, when get all, then it returns the list of Users`() {
        val user1 = User(
            id = UUID.randomUUID().toString(),
            firstName = "Charlie",
            lastName = "Davis",
            email = "charlie.davis@example.com",
            passwordHash = "hashed_password"
        )
        val user2 = User(
            id = UUID.randomUUID().toString(),
            firstName = "Diana",
            lastName = "Evans",
            email = "diana.evans@example.com",
            passwordHash = "hashed_password"
        )
        userRepository.create(user1)
        userRepository.create(user2)
        val users = userRepository.get()
        assertEquals(2, users.size)
    }

}