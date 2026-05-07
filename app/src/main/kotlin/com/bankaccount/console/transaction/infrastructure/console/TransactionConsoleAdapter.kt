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

class TransactionConsoleAdapter(
    private val createTransactionPort: CreateTransactionInputPort,
    private val transactionPort: TransactionInputPort
) {
    private val terminal = Terminal()
    fun run(accountId: String) {
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
                option == "1" -> {
                    terminal.println("Creating a transaction...")
                    // Implement the logic to create a transaction
                }
                option == "2" -> {
                    terminal.println("Getting transaction history...")
                    // Implement the logic to get transaction history
                }
                option == "3" -> {
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
}