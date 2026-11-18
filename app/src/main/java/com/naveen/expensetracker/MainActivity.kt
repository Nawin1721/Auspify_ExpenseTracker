
package com.naveen.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.naveen.expensetracker.data.ExpenseDatabase
import com.naveen.expensetracker.data.ExpenseRepository
import com.naveen.expensetracker.ui.ExpenseViewModel
import com.naveen.expensetracker.ui.ExpenseViewModelFactory

private val BackgroundColor = Color(0xFF12101C)
private val CardColor = Color(0xFF252333)
private val PurpleColor = Color(0xFF8B5CF6)
private val LightPurple = Color(0xFFB794F6)
private val WhiteColor = Color.White
private val GreyColor = Color.LightGray
private val PinkColor = Color(0xFFFF6B81)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ExpenseDatabase.getDatabase(applicationContext)
        val repository = ExpenseRepository(database.expenseDao())
        val factory = ExpenseViewModelFactory(repository)

        val expenseViewModel = ViewModelProvider(
            this,
            factory
        )[ExpenseViewModel::class.java]

        setContent {
            MaterialTheme {
                ExpenseTrackerScreen(expenseViewModel)
            }
        }
    }
}

@Composable
fun ExpenseTrackerScreen(viewModel: ExpenseViewModel) {

    val expenses by viewModel.expenses.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val totalExpense = expenses.sumOf { it.amount }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "Expense Tracker",
                color = WhiteColor,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Total Expense Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF35215F)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Total Expenses",
                        color = GreyColor,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "₹ %.2f".format(totalExpense),
                        color = WhiteColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Income",
                    amount = "₹ 0.00",
                    color = Color(0xFF145A43),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Expenses",
                    amount = "₹ %.2f".format(totalExpense),
                    color = Color(0xFF71334B),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Recent Transactions",
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Expense List
            if (expenses.isEmpty()) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses yet.\nTap Add Expense to get started!",
                        color = GreyColor,
                        fontSize = 15.sp
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = expenses,
                        key = { it.id }
                    ) { expense ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = CardColor
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = expense.title,
                                        color = WhiteColor,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = expense.category,
                                        color = GreyColor,
                                        fontSize = 13.sp
                                    )

                                    Text(
                                        text = "- ₹${"%.2f".format(expense.amount)}",
                                        color = PinkColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        viewModel.deleteExpense(expense.id)
                                    }
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = PinkColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Add Expense Button
            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleColor
                )
            ) {
                Text(
                    text = "＋ Add Expense",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { title, amount, category ->

                viewModel.addExpense(
                    title,
                    amount,
                    category
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (String, Double, String) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val categories = listOf(
        "Food",
        "Travel",
        "Shopping",
        "Bills",
        "Entertainment",
        "Health",
        "Other"
    )

    // Dark theme text field colors
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = WhiteColor,
        unfocusedTextColor = WhiteColor,
        disabledTextColor = GreyColor,
        focusedBorderColor = PurpleColor,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = LightPurple,
        unfocusedLabelColor = GreyColor,
        cursorColor = PurpleColor,
        focusedPlaceholderColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardColor,

        title = {
            Text(
                text = "Add New Expense",
                color = WhiteColor,
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Expense Title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        errorMessage = ""
                    },
                    label = {
                        Text("Expense Title")
                    },
                    placeholder = {
                        Text("e.g. Lunch")
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp
                    ),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                // Amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        errorMessage = ""
                    },
                    label = {
                        Text("Amount (₹)")
                    },
                    placeholder = {
                        Text("Enter amount")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    textStyle = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp
                    ),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Dropdown
                Box {
                    OutlinedButton(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = WhiteColor
                        )
                    ) {
                        Text(
                            text = "Category: $category",
                            color = WhiteColor
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        containerColor = CardColor
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item,
                                        color = WhiteColor
                                    )
                                },
                                onClick = {
                                    category = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Validation Message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = PinkColor,
                        fontSize = 13.sp
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = {

                    val enteredAmount = amount.toDoubleOrNull()

                    when {
                        title.isBlank() -> {
                            errorMessage = "Please enter expense title"
                        }

                        enteredAmount == null ||
                                !enteredAmount.isFinite() ||
                                enteredAmount <= 0 -> {
                            errorMessage = "Enter a valid amount"
                        }

                        else -> {
                            onSave(
                                title.trim(),
                                enteredAmount,
                                category
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleColor
                )
            ) {
                Text(
                    text = "Save Expense",
                    color = WhiteColor
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = LightPurple
                )
            }
        }
    )
}

@Composable
fun SummaryCard(
    title: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                color = WhiteColor.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = amount,
                color = WhiteColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
