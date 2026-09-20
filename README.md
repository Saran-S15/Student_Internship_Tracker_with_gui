# 🎓 Student Internship Tracker

A full-featured, enterprise-grade **Student Internship Tracker** built in pure **Java + JDBC + MySQL**, supporting both a **Modern FlatLaf Desktop GUI** and an interactive **Terminal Console interface**.

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing%20%2B%20FlatLaf-0D9488?style=for-the-badge)

---

## 🌟 Key Features & Roles

The system is organized into **4 core role dashboards**:

1. **👑 Admin Dashboard**
   - Manage student profiles (Add, View, Edit, Search, Delete).
   - Real-time live search by Student ID, Name, or Department.
   - Automatic cascade deletion (deleting a student automatically removes their linked login account).
   - Input validation (Year: 1–4, CGPA: 0.0–10.0, uniqueness checks).

2. **🎓 Student Dashboard**
   - View Academic Profile & Linked Account.
   - Submit new internship applications with date validation (`DD-MM-YYYY`).
   - Fields: Company Name, Location, Domain, Role, Mode (`Online`/`Offline`/`Hybrid`), Start Date, End Date.
   - Track personal applications with status badges (`Pending` 🟡, `Approved` 🟢, `Rejected` 🔴).

3. **👨‍🏫 Faculty Dashboard**
   - Review pending submissions one-by-one or in batch.
   - **`[✓ Approve]`**: Instant approval without prompting for a remark.
   - **`[✕ Reject]`**: Opens a required modal dialog asking for a rejection reason.
   - **`[Skip]`**: Advance to next pending application.
   - View historical Approved and Rejected records.

4. **📊 HOD (Head of Department) Dashboard**
   - 4 KPI Stat Cards: **Total Applications**, **Approved**, **Rejected**, and **Pending Reviews**.
   - View student internship statuses with automatic duration calculation (e.g. `90 days (~3 mo)`).
   - **1-Click TXT Report Generation**: Exports `Internship_Report.txt` formatted with executive summaries and full tables.

---

## 🏛️ System Architecture

```
D:\InternshipTracker/
├── lib/
│   ├── mysql-connector-j-9.2.0.jar     # MySQL JDBC Driver
│   └── flatlaf-3.5.4.jar               # FlatLaf Modern Look and Feel
│
├── schema.sql                          # Database schema & initial seed data
├── DBConnection.java                   # JDBC Connection Provider
│
├── (Models & Authentication)
│   ├── User.java                       # Abstract base user model
│   ├── AdminUser.java, StudentUser.java, FacultyUser.java, HodUser.java
│   ├── Login.java, Authentication.java # Authentication interface & implementation
│   ├── Student.java, Internship.java   # Data models
│   └── InvalidLoginException.java, StudentException.java # Custom exceptions
│
├── (DAO Layer)
│   ├── UserDAO.java                    # User authentication & credentials
│   ├── StudentDAO.java                 # Student CRUD & cascade cleanup
│   └── InternshipDAO.java              # Internship queries & status updates
│
├── (Service Layer)
│   ├── StudentManager.java             # Admin business workflows
│   └── InternshipManager.java          # Student/Faculty/HOD workflows & report export
│
├── (Desktop GUI Layer - FlatLaf Dark Theme)
│   ├── GuiMain.java                    # Desktop GUI entry point
│   ├── TestGuiSwing.java               # Automated GUI component test suite
│   ├── run_gui.bat, run_gui.ps1        # 1-Click GUI launchers
│   └── gui/
│       ├── theme/Theme.java            # Dark palette (#0F172A), Segoe UI typography
│       ├── components/                 # CardPanel, StatCard, StyledButton, StyledTable, StatusBadgeRenderer
│       ├── dialogs/                    # StudentFormDialog, RemarkModalDialog
│       └── panels/                     # LoginPanel, AdminDashboardPanel, StudentDashboardPanel, etc.
│
└── (Console Application)
    ├── Main.java                       # Console application entry point
    ├── TestRunner.java                 # Automated 14-test integration suite
    └── run.bat                         # 1-Click Console launcher
```

---

## 🗄️ Database Setup (MySQL)

1. Ensure MySQL is running on `localhost:3306`.
2. Open MySQL CLI or MySQL Workbench and run:
   ```sql
   SOURCE D:/InternshipTracker/schema.sql;
   ```
3. Default database credentials in `DBConnection.java`:
   - **URL**: `jdbc:mysql://localhost:3306/internship_tracker`
   - **User**: `root`
   - **Password**: `Saran@2007`

---

## 🚀 How to Run

### 1. Running the Modern Desktop GUI
- **Option A (1-Click Launcher)**: Double-click `run_gui.bat` (or run `.\run_gui.ps1`).
- **Option B (PowerShell / Terminal)**:
  ```powershell
  javac -cp ".;lib/*" *.java gui/*.java gui/theme/*.java gui/components/*.java gui/dialogs/*.java gui/panels/*.java
  java -cp ".;gui;gui/theme;gui/components;gui/dialogs;gui/panels;lib/*" GuiMain
  ```

### 2. Running the Console Version
- **Option A**: Double-click `run.bat`.
- **Option B**:
  ```powershell
  javac -cp ".;lib/*" *.java
  java -cp ".;lib/*" Main
  ```

---

## 🔑 Default Test Credentials

| Role | Username | Password | Full Name / Description |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | System Administrator |
| **STUDENT** | `student1` | `student123` | Alice Smith (`STU101`) |
| **STUDENT** | `student2` | `student123` | Bob Johnson (`STU102`) |
| **FACULTY** | `faculty1` | `faculty123` | Dr. Robert Clark |
| **HOD** | `hod1` | `hod123` | Dr. Sarah Williams |

---

## 🧪 Testing & Verification

- **Automated Integration Tests (Console)**:
  ```powershell
  java -cp ".;lib/*" TestRunner
  ```
- **Automated GUI Component Tests**:
  ```powershell
  java -cp ".;gui;gui/theme;gui/components;gui/dialogs;gui/panels;lib/*" TestGuiSwing
  ```
