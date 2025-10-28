Swag-Labs (SauceDemo) — Automated Test Suite

Simple, ready-to-run TestNG + Selenium automation for the SauceDemo demo store.
This README gives step-by-step instructions to run the tests, project structure, troubleshooting tips, and recommended improvements.

🧾 Project Summary

Automates the flow:

Login to https://www.saucedemo.com

Find “Sauce Labs Backpack” and read its price

Add the item to cart

Verify cart contents: item name = Sauce Labs Backpack, price = $29.99

Close browser (always)

Written in Java using Selenium 4 and TestNG. Built with Maven.

🧰 Tech Stack

Java 17+ (or Java 11 — make sure project's JDK matches)

Maven

Selenium Java 4.21.0

TestNG 7.11.0

(Optional) WebDriverManager for driver binaries

 Project Structure (recommended)
Swag-Labs/
├─ pom.xml
├─ src/
│  ├─ main/
│  └─ test/
│     └─ java/
│        └─ Login/
│           ├─ Signin.java
│           └─ InventoryPage.java
├─ testng.xml
└─ README.md

 Prerequisites

Java JDK installed (11+ recommended). java -version

Maven installed. mvn -v

Chrome browser installed (matching chromedriver or use WebDriverManager).


