# ✉️ JavaFX Mailer

A sleek, modern desktop email client built with **Java 21**, **JavaFX 21**, and **Jakarta Mail**. 

This application allows you to connect directly to any SMTP server to compose and send emails through a custom-styled, responsive user interface. It features a beautiful grey/white/blue theme with baby-blue accents.

## 🚀 Features

* **Direct SMTP Integration**: Connects securely to mail servers (Gmail, Outlook, custom domains) using SSL or STARTTLS.
* **Modern JavaFX UI**: A clean, card-based layout built entirely with JavaFX and styled using external CSS. No inline styles!
* **Asynchronous Sending**: Utilizes Java background threads (`Thread` & `Platform.runLater`) so the UI never freezes while waiting for the network.
* **Input Validation**: Prevents sending emails if required fields are missing and handles port-number formatting gracefully.
* **HTML Support**: Automatically converts plain text from the text area into HTML format so line breaks (`\n`) render correctly in the recipient's inbox.

## 🛠️ Tech Stack

* **Java 21** (LTS)
* **JavaFX 21** (UI Framework)
* **Jakarta Mail 2.0.1** (SMTP engine, previously known as JavaMail)
* **Maven** (Dependency Management & Build Tool)
* **CSS3** (Custom styling for JavaFX)

---

## 📦 How to Run This Project

### Prerequisites
You need **Java 21 (JDK)** and **Maven** installed on your system. 
*(Note: You do not need to install JavaFX separately; Maven will handle it automatically).*

### 1. Clone the Repository
```bash
git clone https://github.com/ScottSehlako/javafx-mailer.git
cd javafx-mailer
