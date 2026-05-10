package com.bankaccount.console.account.application.mapper

import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountMapperUnitTest {

    // ============ toDomain Tests - CreateAccountRequest to Account ============

    @Test
    fun `Given CreateAccountRequest with CHECKING type, when toDomain is called, then CheckingAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id1",
                        userId = "user1",
                        accountNumber = "acc1",
                        accountType = "CHECKING",
                        balance = 100.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
        assertEquals("id1", result.id)
        assertEquals(100.0, result.balance, 0.0)
    }

    @Test
    fun `Given CreateAccountRequest with DEBIT type, when toDomain is called, then DebitAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id2",
                        userId = "user2",
                        accountNumber = "acc2",
                        accountType = "DEBIT",
                        balance = 200.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
        assertEquals("id2", result.id)
        assertEquals(200.0, result.balance, 0.0)
    }

    @Test
    fun `Given CreateAccountRequest with CREDIT type, when toDomain is called, then CreditAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id3",
                        userId = "user3",
                        accountNumber = "acc3",
                        accountType = "CREDIT",
                        balance = 300.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
        assertEquals("id3", result.id)
        assertEquals(300.0, result.balance, 0.0)
    }

    @Test
    fun `Given CreateAccountRequest with lowercase checking, when toDomain is called, then CheckingAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id4",
                        userId = "user4",
                        accountNumber = "acc4",
                        accountType = "checking",
                        balance = 150.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
    }

    @Test
    fun `Given CreateAccountRequest with spaces and lowercase debit, when toDomain is called, then DebitAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id5",
                        userId = "user5",
                        accountNumber = "acc5",
                        accountType = "  debit  ",
                        balance = 250.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
    }

    @Test
    fun `Given CreateAccountRequest with mixed case credit, when toDomain is called, then CreditAccount is returned`() {
        val request =
                CreateAccountRequest(
                        id = "id6",
                        userId = "user6",
                        accountNumber = "acc6",
                        accountType = "CrEdIt",
                        balance = 350.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
    }

    @Test
    fun `Given CreateAccountRequest with null values, when toDomain is called, then defaults are applied`() {
        val request =
                CreateAccountRequest(
                        id = null,
                        userId = "user7",
                        accountNumber = null,
                        accountType = "CHECKING",
                )
        val result = request.toDomain()
        assertEquals("", result.id)
        assertEquals("", result.accountNumber)
        assertEquals(0.0, result.balance, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given CreateAccountRequest with invalid type SAVINGS, when toDomain is called, then exception is thrown`() {
        CreateAccountRequest(
                        id = "id",
                        userId = "user",
                        accountNumber = "acc",
                        accountType = "SAVINGS",
                        balance = 100.0
                )
                .toDomain()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given CreateAccountRequest with invalid type UNKNOWN, when toDomain is called, then exception is thrown`() {
        CreateAccountRequest(
                        id = "id",
                        userId = "user",
                        accountNumber = "acc",
                        accountType = "UNKNOWN",
                        balance = 100.0
                )
                .toDomain()
    }

    // ============ toResponse Tests - Account to AccountResponse ============

    @Test
    fun `Given CheckingAccount, when toResponse is called, then AccountResponse with CHECKING is returned`() {
        val account =
                CheckingAccount(
                        id = "check-id",
                        userId = "user-id",
                        accountNumber = "1111111111",
                        balance = 500.0,
                        transactions = mutableListOf("T1")
                )
        val response = account.toResponse()
        assertEquals("check-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("1111111111", response.accountNumber)
        assertEquals("CHECKING", response.accountType)
        assertEquals(500.0, response.balance, 0.0)
        assertEquals(1, response.transactions.size)
    }

    @Test
    fun `Given DebitAccount, when toResponse is called, then AccountResponse with DEBIT is returned`() {
        val account =
                DebitAccount(
                        id = "debit-id",
                        userId = "user-id",
                        accountNumber = "2222222222",
                        balance = 1000.0,
                        transactions = mutableListOf("T1", "T2")
                )
        val response = account.toResponse()
        assertEquals("debit-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("2222222222", response.accountNumber)
        assertEquals("DEBIT", response.accountType)
        assertEquals(1000.0, response.balance, 0.0)
        assertEquals(2, response.transactions.size)
    }

    @Test
    fun `Given CreditAccount, when toResponse is called, then AccountResponse with CREDIT is returned`() {
        val account =
                CreditAccount(
                        id = "credit-id",
                        userId = "user-id",
                        accountNumber = "3333333333",
                        balance = 5000.0,
                        transactions = mutableListOf()
                )
        val response = account.toResponse()
        assertEquals("credit-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("3333333333", response.accountNumber)
        assertEquals("CREDIT", response.accountType)
        assertEquals(5000.0, response.balance, 0.0)
        assertEquals(0, response.transactions.size)
    }

    @Test
    fun `Given CheckingAccount with spaces, when toResponse is called, then spaces are trimmed`() {
        val account =
                CheckingAccount(
                        id = "  check-id  ",
                        userId = "  user-id  ",
                        accountNumber = "  1111111111  ",
                        balance = 750.0,
                        transactions = mutableListOf()
                )
        val response = account.toResponse()
        assertEquals("check-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("1111111111", response.accountNumber)
    }

    @Test
    fun `Given DebitAccount with spaces, when toResponse is called, then spaces are trimmed`() {
        val account =
                DebitAccount(
                        id = "  debit-id  ",
                        userId = "  user-id  ",
                        accountNumber = "  2222222222  ",
                        balance = 2000.0,
                        transactions = mutableListOf()
                )
        val response = account.toResponse()
        assertEquals("debit-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("2222222222", response.accountNumber)
    }

    @Test
    fun `Given CreditAccount with spaces, when toResponse is called, then spaces are trimmed`() {
        val account =
                CreditAccount(
                        id = "  credit-id  ",
                        userId = "  user-id  ",
                        accountNumber = "  3333333333  ",
                        balance = 7500.0,
                        transactions = mutableListOf()
                )
        val response = account.toResponse()
        assertEquals("credit-id", response.id)
        assertEquals("user-id", response.userId)
        assertEquals("3333333333", response.accountNumber)
    }

    @Test
    fun `Given CheckingAccount with multiple transactions, when toResponse preserves transactions`() {
        val transactions = mutableListOf("T1", "T2", "T3", "T4")
        val account =
                CheckingAccount(
                        id = "id",
                        userId = "user",
                        accountNumber = "acc",
                        balance = 1000.0,
                        transactions = transactions
                )
        val response = account.toResponse()
        assertEquals(transactions, response.transactions)
        assertEquals(4, response.transactions.size)
    }

    @Test
    fun `Given multiple DebitAccounts, when toResponse is called for each, all return correct type`() {
        for (i in 1..5) {
            val account =
                    DebitAccount(
                            id = "debit-$i",
                            userId = "user-$i",
                            accountNumber = "acc-$i",
                            balance = (i * 100).toDouble(),
                            transactions = mutableListOf()
                    )
            val response = account.toResponse()
            assertEquals("DEBIT", response.accountType)
        }
    }

    @Test
    fun `Given multiple CreditAccounts, when toResponse is called for each, all return correct type`() {
        for (i in 1..5) {
            val account =
                    CreditAccount(
                            id = "credit-$i",
                            userId = "user-$i",
                            accountNumber = "acc-$i",
                            balance = (i * 500).toDouble(),
                            transactions = mutableListOf()
                    )
            val response = account.toResponse()
            assertEquals("CREDIT", response.accountType)
        }
    }

    // ============ toDomain Tests - CreateAccountRequest to Account ============

    @Test
    fun `toDomain CHECKING with all non-null values`() {
        val request =
                CreateAccountRequest(
                        id = "id1",
                        userId = "user1",
                        accountNumber = "acc1",
                        accountType = "CHECKING",
                        balance = 100.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
        assertEquals("id1", result.id)
        assertEquals("user1", result.userId)
        assertEquals("acc1", result.accountNumber)
        assertEquals(100.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain CHECKING with null id`() {
        val request =
                CreateAccountRequest(
                        id = null,
                        userId = "user1",
                        accountNumber = "acc1",
                        accountType = "CHECKING",
                        balance = 100.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
        assertEquals("", result.id)
    }

    @Test
    fun `toDomain CHECKING with null accountNumber`() {
        val request =
                CreateAccountRequest(
                        id = "id1",
                        userId = "user1",
                        accountNumber = null,
                        accountType = "CHECKING",
                        balance = 100.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
        assertEquals("", result.accountNumber)
    }

    @Test
    fun `toDomain CHECKING with null balance`() {
        val request =
                CreateAccountRequest(
                        id = "id1",
                        userId = "user1",
                        accountNumber = "acc1",
                        accountType = "CHECKING"
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
        assertEquals(0.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain DEBIT with all non-null values`() {
        val request =
                CreateAccountRequest(
                        id = "id2",
                        userId = "user2",
                        accountNumber = "acc2",
                        accountType = "DEBIT",
                        balance = 200.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
        assertEquals("id2", result.id)
        assertEquals("user2", result.userId)
        assertEquals("acc2", result.accountNumber)
        assertEquals(200.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain DEBIT with null id`() {
        val request =
                CreateAccountRequest(
                        id = null,
                        userId = "user2",
                        accountNumber = "acc2",
                        accountType = "DEBIT",
                        balance = 200.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
        assertEquals("", result.id)
    }

    @Test
    fun `toDomain DEBIT with null accountNumber`() {
        val request =
                CreateAccountRequest(
                        id = "id2",
                        userId = "user2",
                        accountNumber = null,
                        accountType = "DEBIT",
                        balance = 200.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
        assertEquals("", result.accountNumber)
    }

    @Test
    fun `toDomain DEBIT with null balance`() {
        val request =
                CreateAccountRequest(
                        id = "id2",
                        userId = "user2",
                        accountNumber = "acc2",
                        accountType = "DEBIT",
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
        assertEquals(0.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain CREDIT with all non-null values`() {
        val request =
                CreateAccountRequest(
                        id = "id3",
                        userId = "user3",
                        accountNumber = "acc3",
                        accountType = "CREDIT",
                        balance = 300.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
        assertEquals("id3", result.id)
        assertEquals("user3", result.userId)
        assertEquals("acc3", result.accountNumber)
        assertEquals(300.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain CREDIT with null id`() {
        val request =
                CreateAccountRequest(
                        id = null,
                        userId = "user3",
                        accountNumber = "acc3",
                        accountType = "CREDIT",
                        balance = 300.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
        assertEquals("", result.id)
    }

    @Test
    fun `toDomain CREDIT with null accountNumber`() {
        val request =
                CreateAccountRequest(
                        id = "id3",
                        userId = "user3",
                        accountNumber = null,
                        accountType = "CREDIT",
                        balance = 300.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
        assertEquals("", result.accountNumber)
    }

    @Test
    fun `toDomain CREDIT with null balance`() {
        val request =
                CreateAccountRequest(
                        id = "id3",
                        userId = "user3",
                        accountNumber = "acc3",
                        accountType = "CREDIT"
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
        assertEquals(0.0, result.balance, 0.0)
    }

    @Test
    fun `toDomain with spaces in accountType CHECKING`() {
        val request =
                CreateAccountRequest(
                        id = "id1",
                        userId = "user1",
                        accountNumber = "acc1",
                        accountType = "  CHECKING  ",
                        balance = 100.0
                )
        val result = request.toDomain()
        assertTrue(result is CheckingAccount)
    }

    @Test
    fun `toDomain with lowercase accountType debit`() {
        val request =
                CreateAccountRequest(
                        id = "id2",
                        userId = "user2",
                        accountNumber = "acc2",
                        accountType = "debit",
                        balance = 200.0
                )
        val result = request.toDomain()
        assertTrue(result is DebitAccount)
    }

    @Test
    fun `toDomain with mixed case accountType credit`() {
        val request =
                CreateAccountRequest(
                        id = "id3",
                        userId = "user3",
                        accountNumber = "acc3",
                        accountType = "CrEdIt",
                        balance = 300.0
                )
        val result = request.toDomain()
        assertTrue(result is CreditAccount)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toDomain with invalid type throws exception`() {
        CreateAccountRequest(
                        id = "id",
                        userId = "user",
                        accountNumber = "acc",
                        accountType = "INVALID",
                        balance = 100.0
                )
                .toDomain()
    }

    @Test
    fun `Given CreateAccountRequest with spaces, when toDomain is called, then fields are trimmed`() {
        val request =
                CreateAccountRequest(
                        id = "  id1  ",
                        userId = "  user1  ",
                        accountNumber = "  acc1  ",
                        accountType = "CHECKING",
                        balance = 100.0
                )

        val result = request.toDomain()

        assertEquals("id1", result.id)
        assertEquals("user1", result.userId)
        assertEquals("acc1", result.accountNumber)
    }
}
