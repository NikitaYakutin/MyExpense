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

data class Expense(
    val amount: Double,
    val category: String,
    val date: String
) {
    fun getExpenseInfo(): String {
        return "Сумма: $amount, Категория: $category, Дата: $date"
    }
}

class ExpenseManager {
    private val expenses = mutableListOf<Expense>()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun getAllExpenses(): String {
        return if (expenses.isEmpty()) {
            "Никаких зарегистрированных расходов."
        } else {
            expenses.joinToString(separator = "\n") { it.getExpenseInfo() }
        }
    }

    fun getTotalByCategory(): Map<String, Double> {
        return expenses.groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
    }
}

class MainActivity : AppCompatActivity() {

    private val expenseManager = ExpenseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etCategory = findViewById<EditText>(R.id.etCategory)

        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        val tvExpenses = findViewById<TextView>(R.id.tvExpenses)
// Получение текущей даты в формате dd/MM/yyyy
        val currentDate = getCurrentDate()
        btnAddExpense.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            val category = etCategory.text.toString()



            // Если сумма или категория не введены, показываем ошибку
            if (amount != null && category.isNotEmpty()) {
                // Дата будет автоматически заполняться текущей датой
                val expense = Expense(amount, category, currentDate)
                expenseManager.addExpense(expense)

                // Обновляем TextView с расходами
                tvExpenses.text = expenseManager.getAllExpenses()
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля правильно.", Toast.LENGTH_SHORT).show()
            }

    }
}
    // Метод для получения текущей даты в формате "dd/MM/yyyy"
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }
}