package com.example.myfinances

abstract class FinancialObject(
    open val accountNumber: String,
    open var currentBalance: Double
)

data class CD(
    override val accountNumber: String,
    val initialBalance: Double,
    override var currentBalance: Double,
    val interestRate: Double
) : FinancialObject(accountNumber, currentBalance)

data class Loan(
    override val accountNumber: String,
    val initialBalance: Double,
    override var currentBalance: Double,
    val paymentAmount: Double,
    val interestRate: Double
) : FinancialObject(accountNumber, currentBalance)

data class CheckingAccount(
    override val accountNumber: String,
    override var currentBalance: Double
) : FinancialObject(accountNumber, currentBalance)