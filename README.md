# Password Manager and Analyzer

This project was built with a focus on **core Java**, **clean project structure**, and **good programming practices**. Every component from database connectivity to file handling to input validation is deliberately organized to reflect the principles of object-oriented design: encapsulation, separation of concerns, and modular code.

---
 
## Features
 
- **Password Strength Analyzer** — checks passwords against 11 rules including length, character variety, common patterns, repeated and sequential characters
- **User Authentication** — sign up and login with credentials stored in a Microsoft Access database
- **Password Vault** — add, view, update, and delete saved passwords per user, stored in an encrypted flat file
- **Encryption** — vault passwords are encrypted before storage and decrypted on display
- **Input Validation** — all menu inputs are validated with a custom `InvalidChoiceException`
 
---

## Project Structure
 
```
📦 Password-Manager-and-Analyzer
├── 📁 database
│   ├── LoginCredentials.accdb            ← MS Access DB for user accounts
│   └── passwords.txt                     ← Encrypted vault file
└── 📁 src
    ├── 📁 logindao
    │   ├── DBConnection.java             ← Manages JDBC connection to Access DB
    │   └── UserDAO.java                  ← CRUD operations for user accounts
    ├── 📁 main
    │   └── ConsoleMain.java              ← Entry point, menu flow orchestration
    ├── 📁 model
    │   ├── User.java                     ← User entity (username + hashed password)
    │   └── VaultData.java                ← Vault entry entity (platform, username, password)
    ├── 📁 security
    │   └── CryptoHandler.java            ← Encrypt/decrypt vault passwords
    ├── 📁 services
    │   └── StrengthAnalyzer.java         ← 11-point password strength analysis logic
    ├── 📁 userinterface
    │   ├── Form.java                     ← Input forms (login, signup, vault entry)
    │   └── Menu.java                     ← All console menus
    ├── 📁 utils
    │   ├── DisplayUtils.java             ← Display helpers (titles, vault view, confirmations)
    │   ├── InputUtils.java               ← Safe integer and string input methods
    │   └── InvalidChoiceException.java   ← Custom runtime exception for menu input
    └── 📁 vaulthandler
        ├── FileOperations.java           ← Read/write/update the vault flat file
        └── VaultManager.java             ← In-memory vault operations (add, delete, update)
``` 
---

## How It Works

### Database Connectivity (Login DAO)
User accounts are stored in a **Microsoft Access database** (`LoginCredentials.accdb`) connected via the **UCanAccess JDBC driver**. `DBConnection.java` manages the connection and tests it on startup. `UserDAO.java` provides methods to add a user, search by username, and retrieve a full user object — all using `PreparedStatement` to avoid SQL injection.

### File Handling (Vault)
The password vault is stored in `passwords.txt` as a plain text file with a custom format. Each user's block starts with `[username]` and entries follow as `platform|username|encryptedPassword`. `FileOperations.java` handles reading a user's block into an `ArrayList` on login, and uses a **temp file swap pattern** (write updated data to a temp file → delete original → rename temp) to safely update the vault on sign-out.
 
### Encryption
`CryptoHandler.java` encrypts and decrypts vault passwords using a positional byte-shift cipher with Base64 encoding. Passwords are always encrypted before being written to file and decrypted only at display time.
 
### Password Strength Analyzer
`StrengthAnalyzer.java` runs 11 checks on any given password: null/empty check, minimum and maximum length, uppercase, lowercase, digits, special characters, common weak patterns (e.g. `"password"`, `"qwerty"`), excessive repeated characters, sequential characters, and a recommended length advisory. It returns a list of weaknesses — an empty list means the password is strong.

---
## How to Run
 
### Prerequisites
- IntelliJ IDEA (or any Java IDE)
- JDK 8 or above
- The project cloned from this repository
 
### Step 1 — Set Up UCanAccess JAR Files
 
UCanAccess is the JDBC driver used to connect to the Microsoft Access database.
 
1. Download UCanAccess from: **http://ucanaccess.sourceforge.net/**
2. Extract the ZIP and open the `lib` subfolder
3. In IntelliJ, go to **File → Project Structure → Libraries**
4. If any existing UCanAccess JARs are listed, **remove all 5 of them** first
5. Add the 5 JAR files from the `lib` folder of the downloaded UCanAccess ZIP
 
### Step 2 — Set the Database Path
 
1. Open `src/logindao/DBConnection.java`
2. Find this line (line 7):
   ```java
   private static final String path = "C:/Users/Muhammad Hasan/...";
   ```
3. Replace the path with the **full path** to `LoginCredentials.accdb` on your machine (the file is included in the `database/` folder of this repo)
 
### Step 3 — Invalidate Caches
 
After making the above changes, go to **File → Invalidate Caches → Invalidate and Restart** in IntelliJ. This ensures the IDE picks up the new JARs and path correctly.
 
### Step 4 — Run
 
Run `ConsoleMain.java` as the main class.
 
---
 
## Tech Stack
 
- **Language:** Java
- **Database:** Microsoft Access via UCanAccess JDBC
- **Storage:** Flat file (text-based vault)
- **IDE:** IntelliJ IDEA

## Team
- Muhammad Hasan
- [Daniyal Afzaal](https://github.com/Daniyal-Afzaal1)

