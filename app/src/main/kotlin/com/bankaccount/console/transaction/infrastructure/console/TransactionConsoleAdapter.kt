package com.bankaccount.console.transaction.infrastructure.console

import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.terminal.success
import com.github.ajalt.mordant.widgets.Panel
import com.github.ajalt.mordant.widgets.Text
import com.bankaccount.console.transaction.application.ports.input.CreateTransactionInputPort
import com.bankaccount.console.transaction.application.ports.input.TransactionInputPort
import com.bankaccount.console.account.application.ports.input.AccountInputPort
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.shared.transaction.domain.model.TransactionType

class TransactionConsoleAdapter(
    private val createTransactionPort: CreateTransactionInputPort,
    private val transactionPort: TransactionInputPort,
    private val accountPort: AccountInputPort
) {
    private val terminal = Terminal()
    fun run(userId: String) {
        val account = accountPort.getByUserId(userId)

        val text =
                """
                For your ${account.accountType} account: ${account.accountNumber}
        Current Balance: ${account.balance}
            """.trimIndent()

        terminal.println(
                Panel(
                        content = Text(text, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Your Account Balance"),
                )
        )
        var option: String? = null
        while (option != "3") {
            option = menu()

            when(option) {
                "1" -> {
                    terminal.println("Creating a transaction...")
                    createTransaction(account.id, account.balance)
                }
                "2" -> {
                    terminal.println("Getting transaction history...")
                    // Implement the logic to get transaction history
                }
                "3" -> {
                    terminal.danger("Returning to main menu...")
                }
                else -> {
                    terminal.danger("Invalid option, please try again.")
                }
            }
        }
    }

    private fun menu(): String? {
        val userChoices = listOf("1. Create a transaction", "2. Get transaction history", "Return to Main Menu")
        val selection =
                terminal.interactiveSelectList(
                        userChoices,
                        "You could return to the main menu",
                )

        if (selection == null) {
            terminal.danger("Aborted account creation")
            return null
        }

        val index = userChoices.indexOf(selection)
        terminal.success("You chose option $index: $selection")
        return (index + 1).toString()
    }

    private fun createTransaction(
        accountId: String,
        currentBalance: Double,
    ) {
        val transactionOption = transactionMenu()
        val transactionType = when (transactionOption) {
            "1" -> TransactionType.DEPOSIT.name
            "2" -> TransactionType.WITHDRAWAL.name
            "3" -> {
                terminal.danger("Returning to main menu...")
                return
            }
            else -> {
                terminal.danger("Invalid option, returning to transaction menu.")
                return
            }
        }
        val yourCurrentBalanceIs = "Your current balance is: ${currentBalance}"
        terminal.println(
                Panel(
                        content = Text(yourCurrentBalanceIs, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Your balance"),
                )
        )
        if (transactionType == TransactionType.WITHDRAWAL.name && currentBalance <= 0) {
            terminal.danger("Your balance is zero or negative, you cannot make a withdrawal.")
            return
        }
        val amountInput = askRequiredText("What is your ${transactionType.lowercase()} amount?:")
        val amount = amountInput!!.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            terminal.danger("Invalid amount, it must be a number greater than zero.")
            return
        }
        if (transactionType == TransactionType.WITHDRAWAL.name && amount > currentBalance) {
            terminal.danger("You cannot withdraw more than your current balance.")
            return
        }
        val newBalance = if (transactionType == TransactionType.DEPOSIT.name) {
            currentBalance + amount
        } else {
            currentBalance - amount
        }
        val request =
                CreateTransactionRequest(
                        accountId = accountId,
                        balanceBefore = currentBalance,
                        amount = amount,
                        type = transactionType,
                        timestamp = System.currentTimeMillis()
                        )
        
        val transactionResponse = createTransactionPort.create(request)

        accountPort.updateBalance(accountId, newBalance)

        terminal.success("Your transaction was successful! Transaction ID: ${transactionResponse.id}")

        val yourNewBalanceIs = "Your new balance is: ${newBalance}"
        terminal.println(
                Panel(
                        content = Text(yourNewBalanceIs, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Transaction Successful"),
                )
        )
    }

    fun transactionMenu(): String? {
        val userChoices = listOf("1. Deposit", "2. Withdraw", "Return to Main Menu")
        val selection =
                terminal.interactiveSelectList(
                        userChoices,
                        "You could return to the main menu",
                )

        if (selection == null) {
            terminal.danger("Aborted account creation")
            return null
        }

        val index = userChoices.indexOf(selection)
        terminal.success("You chose option $index: $selection")
        return (index + 1).toString()
    }

    private fun askRequiredText(
            label: String,
            errorMessage: String = "Invalid input",
            validator: (String) -> Boolean = { true }
    ): String? {
        while (true) {
            val input = terminal.prompt(label)?.trim()

            if (input == null) {
                terminal.danger("Operation cancelled")
                return null
            }

            if (input.equals("cancelar", ignoreCase = true)) {
                terminal.danger("Operation cancelled")
                return null
            }

            if (input.isBlank()) {
                terminal.println(red("This field is required"))
                continue
            }

            if (!validator(input)) {
                terminal.println(red(errorMessage))
                continue
            }

            return input
        }
    }
}