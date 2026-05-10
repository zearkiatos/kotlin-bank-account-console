package com.bankaccount.console.account.infrastructure.console

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
import com.bankaccount.console.account.application.ports.input.AccountInputPort

class AccountBalanceConsoleAdapter(
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
        while (option != "1") {
            option = menu()

            if (option == "1") {
                terminal.danger("Returning to main menu...")
            }
        }
    }

     private fun menu(): String? {
        val userChoices = listOf("Return to Main Menu")
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