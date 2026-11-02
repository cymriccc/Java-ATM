# Java ATM Simulation

[![Java Version](https://img.shields.io/badge/Java-8%2B-007396?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-brightgreen.svg?style=flat-square)](https://github.com/cymriccc/Java-ATM/graphs/commit-activity)

A console-based Automated Teller Machine (ATM) simulator engineered in Java to demonstrate core Object-Oriented Programming (OOP) concepts, encapuslation, state management, and defensive transaction handling.

> **Academic Note:** Built as a Finals Activity requirement for Fundamentals of Programming at National University – MOA.

---

## 🛠️ Key Features

- **Account Authentication:** Secure session initiation requiring valid account PIN verification.
- **Balance Inquiry & Account Auditing:** Real-time balance retrieval across active accounts.
- **Deposit & Withdrawal Engine:** Transaction processing with validation checks to prevent negative deposits or overdraft errors.
- **Inter-Account Fund Transfers:** Seamless value transfer between multiple registered account instances.
- **Dynamic PIN Management:** Real-time credential modification logic.

---

## 🏗️ Architecture & OOP Concepts Applied

- **Encapsulation:** Private state variables (`balance`, `pin`, `accountNumber`) isolated within data models and exposed safely via getter/setter methods.
- **Modular Class Structure:** Clean separation between state model representations (`Account.java`) and system execution/interactive driver logic (`ATM.java`).
- **Defensive Input Validation:** Handles invalid menu choices, out-of-bounds transfers, and invalid numerical inputs to prevent runtime exceptions.

---

## 📂 Repository Structure

```text
Java-ATM/
├── Account.java      # Data model encapsulating account state, credentials, and math operations
├── ATM.java          # CLI application entry point, state controller, and menu handler
├── README.md         # Repository documentation
└── .gitignore        # Java compilation exclusions (*.class)
```

---

## 🚀 Setup and Execution

### Prerequisites

* **Java Development Kit (JDK) 8 or higher** installed.

### Compilation & Launch

1. **Clone the repository:**
   ```bash
   git clone https://github.com/cymriccc/Java-ATM.git
   cd Java-ATM
   ```

2. **Compile Java source files:**
   ```powershell
   javac ATM.java Account.java
   ```

3. **Run the simulation:**
   ```powershell
   java ATM
   ```

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for details.

---

**Author**: [@cymriccc](https://github.com/cymriccc)
