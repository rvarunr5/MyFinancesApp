package com.example.myfinances
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyFinances.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "financial_objects"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_ACCOUNT_NUMBER = "account_number"
        private const val COLUMN_INITIAL_BALANCE = "initial_balance"
        private const val COLUMN_CURRENT_BALANCE = "current_balance"
        private const val COLUMN_INTEREST_RATE = "interest_rate"
        private const val COLUMN_PAYMENT_AMOUNT = "payment_amount"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TYPE TEXT,
                $COLUMN_ACCOUNT_NUMBER TEXT,
                $COLUMN_INITIAL_BALANCE REAL,
                $COLUMN_CURRENT_BALANCE REAL,
                $COLUMN_INTEREST_RATE REAL,
                $COLUMN_PAYMENT_AMOUNT REAL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addFinancialObject(obj: FinancialObject) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_ACCOUNT_NUMBER, obj.accountNumber)
        values.put(COLUMN_CURRENT_BALANCE, obj.currentBalance)

        when (obj) {
            is CD -> {
                values.put(COLUMN_TYPE, "CD")
                values.put(COLUMN_INITIAL_BALANCE, obj.initialBalance)
                values.put(COLUMN_INTEREST_RATE, obj.interestRate)
            }
            is Loan -> {
                values.put(COLUMN_TYPE, "Loan")
                values.put(COLUMN_INITIAL_BALANCE, obj.initialBalance)
                values.put(COLUMN_INTEREST_RATE, obj.interestRate)
                values.put(COLUMN_PAYMENT_AMOUNT, obj.paymentAmount)
            }
            is CheckingAccount -> {
                values.put(COLUMN_TYPE, "Checking")
            }
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTransactions(): List<String> {
        val transactions = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                val accountNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_NUMBER))
                val currentBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_BALANCE))
                transactions.add("$type - $accountNumber: $$currentBalance")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return transactions
    }
}