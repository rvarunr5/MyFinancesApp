package com.example.myfinances

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TransactionListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.transactionListView)

        displayTransactions()
    }

    private fun displayTransactions() {
        val transactions = dbHelper.getAllTransactions()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transactions)
        listView.adapter = adapter
    }
}