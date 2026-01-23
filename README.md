# meal-lab-project
A Java recipe application using TheMealDB API and JavaFX.

## MealLab System

A Recipe Management System developed for a university assignment.
The project consists of two modules:
1. **meallab-api**: A Java library that connects to [TheMealDB](https://www.themealdb.com) API to fetch recipes.
2. **meallab-app**: A Desktop Application (GUI) built with **JavaFX** to display recipes to the user.

## 🚀 Features
* **Search:** Find recipes based on a primary ingredient.
* **Detailed View:** Access full cooking instructions, ingredient lists, and high-quality photos.
* **Favorites:** Save preferred recipes to a local JSON file for quick offline access.
* **Testing:** Robust backend validation using JUnit tests.

## 🛠 Technologies Used
* **Java 21** (LTS)
* **Maven** (Dependency Management)
* **JavaFX** (User Interface)
* **Jackson** (JSON Parsing)
* **Java HttpClient** (API Connection)
* **JUnit 5** (Testing)

## 💻 Execution Instructions
To run the application locally, follow these steps:

1. **JDK:** Ensure you have **JDK 21** or later installed on your system.
2. **Clone:** Clone this repository or download it as a ZIP file.
3. **Import:** Import the project into Eclipse (or any other IDE) as **Existing Maven Projects**.
4. **Dependencies:** Allow Maven to automatically download all necessary libraries.
5. **Run:** Locate and execute the `Launcher.java` class within the `MealApp` module (`src/main/java/gr/unipi/meallab/app/Launcher.java`).

## 🧪 Testing
To verify the backend library's functionality, you can run the JUnit tests located in the `MealAPI` module under the `src/test/java` directory.

**University of Piraeus** | 2025-2026
