package com.bankaccount.console.account.domain.utils

import kotlin.random.Random
import com.bankaccount.console.shared.account.domain.model.AccountType

object BankAccountNumberGenerator {

    private const val BANK_CODE = "001"

    fun generate(accountType: AccountType): String {
        val baseNumber = generateRandomDigits(length = 8)

        val rawAccountNumber = BANK_CODE + accountType.code + baseNumber

        val checkDigit = calculateLuhnCheckDigit(rawAccountNumber)

        return "$BANK_CODE-${accountType.code}-$baseNumber-$checkDigit"
    }

    private fun generateRandomDigits(length: Int): String {
        return (1..length)
            .map { Random.nextInt(0, 10) }
            .joinToString("")
    }

    private fun calculateLuhnCheckDigit(number: String): Int {
        val sum = number
            .reversed()
            .mapIndexed { index, char ->
                var digit = char.digitToInt()

                if (index % 2 == 0) {
                    digit *= 2
                    if (digit > 9) {
                        digit -= 9
                    }
                }

                digit
            }
            .sum()

        return (10 - (sum % 10)) % 10
    }

    fun isValid(accountNumber: String): Boolean {
        val cleanedNumber = accountNumber.replace("-", "")

        if (!cleanedNumber.all { it.isDigit() }) return false
        if (cleanedNumber.length != 14) return false

        val numberWithoutCheckDigit = cleanedNumber.dropLast(1)
        val providedCheckDigit = cleanedNumber.last().digitToInt()
        val expectedCheckDigit = calculateLuhnCheckDigit(numberWithoutCheckDigit)

        return providedCheckDigit == expectedCheckDigit
    }
}