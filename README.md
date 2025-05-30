# 🚗 MotorPH Employee Management System (Java GUI App)

A simple **Java-based GUI application** for MotorPH that manages employee records with basic login authentication and full CRUD operations. Built using **Swing** and file-based CSV storage.

---

## ✨ Features

### ✅ Core Features
- **Login Authentication**
  - Login using valid credentials stored in `users.csv`
  - Denies access on incorrect credentials
- **View Employee**
  - Display all employee records from `employees.csv` in a table
- **Add Employee**
  - Form to add a new employee
  - Appends data to `employees.csv`
- **Edit Employee**
  - Update selected employee's details
  - Overwrites the original line in the file
- **Delete Employee**
  - Remove selected employee from the CSV file

---

### 🔐 Additional Enhancements
- ✅ **Show Password** toggle
- ✅ **"Forgot Password?"** link with popup guidance
- ✅ **Login attempt limit** (3 attempts)
- ✅ **Remember Me** checkbox

---

## 📁 File Structure

```plaintext
├── src/
│   ├── Main.java
│   ├── LoginFrame.java
│   ├── PayrollGUI.java
│   ├── AddEmployeeForm.java
│   ├── EditEmployeeForm.java
│   ├── ViewEmployeeForm.java
├── employees.csv
├── users.csv
└── README.md
