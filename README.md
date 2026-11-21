
# 💰 Expense Tracker

A modern Android Expense Tracker application built using **Kotlin, Jetpack Compose, and Room Database**. This app helps users manage daily expenses, track spending, and organize transactions with a clean and user-friendly interface.

## 📱 Features

- 🎨 Modern dark purple UI
- ➕ Add new expenses
- 🗂️ Categorize expenses
- 💰 Automatically calculate total expenses
- 📋 View recent transactions
- 🗑️ Delete expenses
- 💾 Store expenses locally using Room Database
- 🔄 Expenses remain saved after restarting the app
- ✅ Input validation for expense title and amount

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Programming Language |
| Jetpack Compose | UI Development |
| Material 3 | UI Components |
| Room Database | Local Data Storage |
| ViewModel | UI State Management |
| Kotlin Coroutines | Background Operations |
| Android Studio | Development Environment |

## 🏗️ Architecture

The application follows a simple MVVM architecture.

```text
com.naveen.expensetracker
│
├── data
│   ├── Expense.kt
│   ├── ExpenseDao.kt
│   ├── ExpenseDatabase.kt
│   └── ExpenseRepository.kt
│
├── ui
│   ├── ExpenseViewModel.kt
│   ├── ExpenseViewModelFactory.kt
│   └── theme
│
└── MainActivity.kt
```

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK
- Kotlin support
- Android device or emulator

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Nawin1721/ExpenseTracker.git
   ```

2. Open the project in Android Studio.

3. Wait for Gradle synchronization to complete.

4. Connect an Android device or start an emulator.

5. Click the **Run ▶** button.

## 📸 Screenshots

<img width="1376" height="768" alt="Lumo generated 2026-11-18 20 53" src="https://github.com/user-attachments/assets/8465f061-9417-4fb6-a8bd-5ac2f3d768c1" />


## 💡 How It Works

1. Open the Expense Tracker application.
2. Tap the **Add Expense** button.
3. Enter the expense title.
4. Enter the amount.
5. Select an expense category.
6. Tap **Save Expense**.
7. View the transaction in the Recent Transactions section.
8. The total expense updates automatically.
9. Delete a transaction whenever required.

## 🎯 Project Objective

The main objective of this project is to develop a simple and effective Android application for managing daily expenses while learning modern Android development concepts such as Jetpack Compose, Room Database, MVVM architecture, and Kotlin Coroutines.

## 👨‍💻 Developer

**Naveen Kadali 💜**

- GitHub: [@Nawin1721](https://github.com/Nawin1721)
- Project: [Expense Tracker](https://github.com/Nawin1721/Auspify_ExpenseTracker)

## 📄 License

This project is open-source and available for learning and educational purposes.
