# Practical 3 - Object methods, nested class and clone

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To make Account and Customer objects behave correctly: readable printing, comparison by account number, a nested Address class and clone().

toString() gives readable text. equals() and hashCode() compare accounts by accountNumber only, which collections such as HashMap rely on. Address is a static nested class inside Customer, and Customer implements Cloneable so clone() returns an independent copy (the Address is copied too).

## What this practical covers

- `toString()`, `equals()` and `hashCode()` in `Account`
- Public static nested class `Customer.Address` (line, city, pincode)
- `Customer.clone()` with `Cloneable`
- `instanceof` check in `main`

## Project structure

```text
practical-3/
├── .vscode/
│   └── settings.json
├── src/
│   ├── Account.java
│   ├── BankInfo.java
│   ├── Customer.java
│   ├── MenuOption.java
│   └── MiniBank.java
├── .gitignore
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-3`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-3
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-3
mkdir bin
javac -d bin src\*.java
java -cp bin MiniBank
```

## Sample output

```text
Account[accountNumber=AC0001, ownerName=Kiran Desai, balance=3000]
Account[accountNumber=AC0002, ownerName=Kiran Desai, balance=3000]
a.equals(sameAccount) = true
a.equals(other)       = false
same hashCode         = true
o instanceof Account  = true
o instanceof Customer = false
original : Customer[CUST101, Neha Mehta, neha@example.com, 9988776655] | 12 Alkapuri, Vadodara - 390007
clone    : Customer[CUST101, Neha Mehta, neha@example.com, 9988776655] | 12 Alkapuri, Vadodara - 390007
different objects: true, address copied too: true
```

## Concepts used

Overriding Object methods, equals/hashCode contract, nested classes, Cloneable, instanceof.

## Navigation

[← Practical 2](../practical-2) · [All practicals](..) · [Practical 4 →](../practical-4)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML
