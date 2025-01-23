package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Data class representing an expense
data class Expense(val amount: Double, val category: String, val date: String) {
    fun displayInfo(): String {
        return "Сумма: $amount, Категория: $category, Дата: $date"
    }
}

// Class managing a list of expenses
class ExpenseManager {
    private val expenses = mutableListOf<Expense>()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun getAllExpenses(): List<Expense> {
        return expenses
    }

    fun getTotalByCategory(): Map<String, Double> {
        return expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
}


class MainActivity : AppCompatActivity() {

    private val expenseManager = ExpenseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountInput: EditText = findViewById(R.id.etAmount)
        val categoryInput: EditText = findViewById(R.id.etCategory)

        val addButton: Button = findViewById(R.id.btnAddExpense)
        val showButton: Button = findViewById(R.id.BtnshowButton)
        val outputView: TextView = findViewById(R.id.tvExpenses)

        addButton.setOnClickListener {
            val amount = amountInput.text.toString().toDoubleOrNull()
            val category = categoryInput.text.toString()
            val currentDate = getCurrentDate()

            if (amount != null && category.isNotBlank() && currentDate.isNotBlank()) {
                val expense = Expense(amount, category, currentDate)
                expenseManager.addExpense(expense)
                outputView.text = "История: ${expense.displayInfo()}"
                amountInput.text.clear()
                categoryInput.text.clear()

            } else {
                outputView.text = "Заполни все поля!."
            }
        }

        showButton.setOnClickListener {
            val allExpenses = expenseManager.getAllExpenses()
            val totalByCategory = expenseManager.getTotalByCategory()

            val expensesText = allExpenses.joinToString("\n") { it.displayInfo() }
            val totalText = totalByCategory.entries.joinToString("\n") { "${it.key}: ${it.value}" }

            outputView.text = "Все траты:\n$expensesText\n\nПо категориям:\n$totalText"
        }
    }
}

// Метод для получения текущей даты в формате "dd/MM/yyyy"
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }
