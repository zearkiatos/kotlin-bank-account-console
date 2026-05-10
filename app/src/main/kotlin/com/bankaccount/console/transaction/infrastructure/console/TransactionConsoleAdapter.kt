package com.bankaccount.console.transaction.infrastructure.console

import com.bankaccount.console.account.application.mapper.toDomain
import com.bankaccount.console.account.application.ports.input.AccountInputPort
import com.bankaccount.console.account.domain.handleError.BalanceEqualZeroException
import com.bankaccount.console.account.domain.handleError.NotEnoughBalanceException
import com.bankaccount.console.account.domain.handleError.UnecessaryPayOffException
import com.bankaccount.console.account.domain.handleError.PayOffAmountGraterThanCreditBalanceException
import com.bankaccount.console.account.domain.handleError.PendingPayOffException
import com.bankaccount.console.shared.transaction.domain.model.TransactionType
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.application.ports.input.CreateTransactionInputPort
import com.bankaccount.console.transaction.application.ports.input.TransactionInputPort
import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.rendering.BorderType.Companion.SQUARE_DOUBLE_SECTION_SEPARATOR
import com.github.ajalt.mordant.rendering.TextAlign.LEFT
import com.github.ajalt.mordant.rendering.TextAlign.RIGHT
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextColors.brightBlue
import com.github.ajalt.mordant.rendering.TextColors.green
import com.github.ajalt.mordant.rendering.TextColors.red
import com.github.ajalt.mordant.rendering.TextColors.yellow
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.dim
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.table.Borders.ALL
import com.github.ajalt.mordant.table.Borders.BOTTOM
import com.github.ajalt.mordant.table.Borders.LEFT_BOTTOM
import com.github.ajalt.mordant.table.Borders.NONE
import com.github.ajalt.mordant.table.Borders.TOP_BOTTOM
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.terminal.success
import com.github.ajalt.mordant.widgets.Panel
import com.github.ajalt.mordant.widgets.Text
import java.time.Instant

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

            when (option) {
                "1" -> {
                    terminal.println("Creating a transaction...")
                    createTransaction(account.id, userId)
                }
                "2" -> {
                    terminal.println("Getting transaction history...")
                    val transactions = transactionPort.getByAccountId(account.id)
                    transactionTable(transactions)
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
        val userChoices =
                listOf(
                        "1. Create a transaction",
                        "2. Get transaction history",
                        "Return to Main Menu"
                )
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
            userId: String,
    ) {
        val account = accountPort.getByUserId(userId)
        val currentBalance = account.balance
        val transactionOption = transactionMenu()
        val transactionType =
                when (transactionOption) {
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
                        content =
                                Text(yourCurrentBalanceIs, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Your balance"),
                )
        )
        val amountInput = askRequiredText("What is your ${transactionType.lowercase()} amount?:")
        val amount = amountInput?.toDoubleOrNull()
        if (amount == null) {
            terminal.danger("Invalid amount, returning to transaction menu.")
            return
        }
        val newBalance =
                if (transactionType == TransactionType.DEPOSIT.name) {
                    try {
                        account.toDomain().deposit(amount.toInt()).toDouble()
                    }
                    catch(ex: UnecessaryPayOffException) {
                        terminal.danger(ex.message)
                        return
                    }
                    catch(ex: PayOffAmountGraterThanCreditBalanceException) {
                        terminal.danger(ex.message)
                        return
                    }
                    catch(ex: PendingPayOffException) {
                        terminal.danger(ex.message)
                        return
                    }
                } else {
                    val updated = try {
                        account.toDomain().withdraw(amount.toInt()).toDouble()
                    }
                    catch(ex: BalanceEqualZeroException) {
                        terminal.danger(ex.message)
                        return 
                    }
                    catch(ex: NotEnoughBalanceException) {
                        terminal.danger(ex.message)
                        return 
                    }
                    updated
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

        terminal.success(
                "Your transaction was successful! Transaction ID: ${transactionResponse.id}"
        )

        val yourNewBalanceIs = "Your new balance is: ${newBalance}"
        terminal.println(
                Panel(
                        content = Text(yourNewBalanceIs, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Transaction Successful"),
                )
        )
    }

    fun transactionTable(transactions: List<TransactionResponse>?) {
        if (transactions.isNullOrEmpty()) {
            terminal.danger("No transactions found.")
            return
        }

        val showTransactionTable = table {
            borderType = SQUARE_DOUBLE_SECTION_SEPARATOR
            borderStyle = rgb("#4b25b9")
            align = RIGHT
            tableBorders = NONE
            header {
                style = yellow + bold
                row("Transaction Id", "Current Balance", "Amount", "Transaction Type", "Date") {
                    cellBorders = BOTTOM
                }
            }
            body {
                style = green
                column(0) {
                    align = LEFT
                    cellBorders = ALL
                    style = brightBlue
                }
                column(4) {
                    cellBorders = LEFT_BOTTOM
                    style = brightBlue
                }
                column(5) { style = brightBlue }
                rowStyles(TextStyle(), dim.style)
                cellBorders = TOP_BOTTOM
                for (transaction in transactions) {
                    row {
                        style = if (transaction.type == TransactionType.DEPOSIT.name) green else red
                        cells(
                                transaction.id,
                                transaction.balanceBefore.toString(),
                                transaction.amount.toString(),
                                if (TransactionType.DEPOSIT.name == transaction.type) {
                                    "${transaction.type} 🟢"
                                } else {
                                    "${transaction.type} 🔴"
                                },
                                Instant.ofEpochMilli(transaction.timestamp).toString()
                        )
                    }
                }
            }
            footer {
                style(italic = true)
                row {
                    cells(
                            "Your current balance is",
                            "${
                                (if ( transactions.last().type == TransactionType.DEPOSIT.name) {
                                    transactions.last().balanceBefore + transactions.last().amount
                                } else {
                                    transactions.last().balanceBefore - transactions.last().amount
                                }).toString()
                            }"
                    )
                }
            }
            captionBottom(dim("Movements history"))
        }

        terminal.println(showTransactionTable)
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
