package com.example.myfinances

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var accountTypeRadioGroup: RadioGroup
    private lateinit var accountNumberEditText: EditText
    private lateinit var initialBalanceEditText: EditText
    private lateinit var currentBalanceEditText: EditText
    private lateinit var interestRateEditText: EditText
    private lateinit var paymentAmountEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var viewTransactionsButton: Button
    private lateinit var cdLoanFieldsLayout: LinearLayout
    private lateinit var loanFieldsLayout: LinearLayout

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        accountTypeRadioGroup = findViewById(R.id.accountTypeRadioGroup)
        accountNumberEditText = findViewById(R.id.accountNumberEditText)
        initialBalanceEditText = findViewById(R.id.initialBalanceEditText)
        currentBalanceEditText = findViewById(R.id.currentBalanceEditText)
        interestRateEditText = findViewById(R.id.interestRateEditText)
        paymentAmountEditText = findViewById(R.id.paymentAmountEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        viewTransactionsButton = findViewById(R.id.viewTransactionsButton)
        cdLoanFieldsLayout = findViewById(R.id.cdLoanFieldsLayout)
        loanFieldsLayout = findViewById(R.id.loanFieldsLayout)
    }

    private fun setupListeners() {
        accountTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.cdRadioButton -> {
                    cdLoanFieldsLayout.visibility = View.VISIBLE
                    loanFieldsLayout.visibility = View.GONE
                }
                R.id.loanRadioButton -> {
                    cdLoanFieldsLayout.visibility = View.VISIBLE
                    loanFieldsLayout.visibility = View.VISIBLE
                }
                R.id.checkingRadioButton -> {
                    cdLoanFieldsLayout.visibility = View.GONE
                    loanFieldsLayout.visibility = View.GONE
                }
            }
        }

        saveButton.setOnClickListener {
            saveFinancialObject()
        }

        cancelButton.setOnClickListener {
            clearFields()
        }

        viewTransactionsButton.setOnClickListener {
            val intent = Intent(this, TransactionListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveFinancialObject() {
        val accountNumber = accountNumberEditText.text.toString()
        val currentBalance = currentBalanceEditText.text.toString().toDoubleOrNull() ?: 0.0

        val financialObject = when (accountTypeRadioGroup.checkedRadioButtonId) {
            R.id.cdRadioButton -> {
                val initialBalance = initialBalanceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val interestRate = interestRateEditText.text.toString().toDoubleOrNull() ?: 0.0
                CD(accountNumber, initialBalance, currentBalance, interestRate)
            }
            R.id.loanRadioButton -> {
                val initialBalance = initialBalanceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val interestRate = interestRateEditText.text.toString().toDoubleOrNull() ?: 0.0
                val paymentAmount = paymentAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
                Loan(accountNumber, initialBalance, currentBalance, paymentAmount, interestRate)
            }
            R.id.checkingRadioButton -> {
                CheckingAccount(accountNumber, currentBalance)
            }
            else -> null
        }

        if (financialObject != null) {
            dbHelper.addFinancialObject(financialObject)
            Toast.makeText(this, "Financial object saved successfully", Toast.LENGTH_SHORT).show()
            clearFields()
        } else {
            Toast.makeText(this, "Please select an account type", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        accountTypeRadioGroup.clearCheck()
        accountNumberEditText.text.clear()
        initialBalanceEditText.text.clear()
        currentBalanceEditText.text.clear()
        interestRateEditText.text.clear()
        paymentAmountEditText.text.clear()

        cdLoanFieldsLayout.visibility = View.GONE
        loanFieldsLayout.visibility = View.GONE
    }
}