# 🚗 Car Rental Management System

A professional desktop application built with **JavaFX** and **SQLite**, designed for efficient fleet management and customer reservations. This project demonstrates clean coding practices, a modular layered architecture, and advanced UI/UX features.

## ✨ Key Features

### 1. Fleet & Reservation Management (CRUD)
* **Car Management**: Full support for Adding, Modifying, and Deleting vehicles (ID, Make, Model, Rental Price).
* **Booking System**: Streamlined reservation flow connecting customers to specific cars.
* **Smart Form Sync**: Selecting a row in the table automatically populates input fields for rapid editing. ID fields are dynamically disabled during selection to maintain referential integrity.

### 2. Advanced Filtering & Sorting (Enum-Based)
The system uses strongly-typed **Enums** (`CarFilterType`, `ReservationFilterType`, `CarSortType`) for maximum reliability:
* **Car Filters**: Filter by Make, Maximum Price, Model, or identify cars **rented by a specific customer**.
* **Reservation Filters**: Filter by Customer Name, Car ID, or find all reservations **active at a specific date**.
* **Sorting**: Instant high-to-low price sorting for the car fleet.

### 3. Undo/Redo Mechanism
Powered by the **Command Design Pattern**, the system tracks history for all data-altering actions:
* Separate history stacks for Cars and Reservations.
* Buttons dynamically enable/disable based on available history.
* Supports reversing/re-applying Add, Delete, and Modify operations.

### 4. Robust Validation & UI Feedback
* **Error Handling**: Custom dialogs for invalid inputs (e.g., non-numeric prices or incorrect date formats like `YYYY-MM-DD`).
* **Information Popups**: Detailed feedback when performing searches, such as listing all customers associated with a specific Car ID.

## 🏗️ Architecture & Design Patterns

The project follows a strict **Layered Architecture**:
1.  **Domain**: Core entities implementing `IIdentifiable`.
2.  **Repository**: Generic CRUD interface with SQLite persistence.
3.  **Service**: Business logic layer handling filtering, sorting, and data coordination.
4.  **GUI (Controller)**: The `CarsGUIController` manages the UI lifecycle and event handling.

**Design Patterns Implemented:**
* **Command Pattern**: Encapsulates actions for the Undo/Redo system.
* **Repository Pattern**: Decouples business logic from data storage.
* **Dependency Injection**: Services and Managers are injected via the controller's constructor.

## 🛠️ Tech Stack

* **Language**: Java 17+
* **Framework**: JavaFX (OpenJFX)
* **Database**: SQLite (JDBC Driver)
* **Styling**: Custom Modern Enterprise CSS (Indigo/Slate theme)

## 🚀 Getting Started

### Prerequisites
* **JDK 17** or higher.
* **JavaFX SDK** (properly configured in your IDE).
* **SQLite JDBC Driver** (.jar file) added to the project libraries.

### Setup & Running
1.  **Open the Project**: Import the source folder into your favorite IDE (IntelliJ IDEA, Eclipse, or NetBeans).
2.  **Add Libraries**:
    * Go to **Project Structure** -> **Libraries**.
    * Add the **JavaFX SDK** (lib folder).
    * Add the **sqlite-jdbc** jar file.
3.  **VM Options**: If you are using JavaFX as an external library, add the following to your Run Configuration:
    ```text
    --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
    ```
4.  **Database**: Ensure the `car_rental.db` file is located in the project root directory.
5.  **Run**: Execute the `Main.java` class.

---

## 👨‍💻 Author
**Daria Berciu*
*Computer Science Student @ Babeș-Bolyai University*
