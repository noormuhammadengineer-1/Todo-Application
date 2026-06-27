# 📋 Android Todo App (MVVM + Retrofit)

A professional Android application demonstrating modern development practices, clean architecture, and REST API integration. This project performs full **CRUD operations** using the JSONPlaceholder mock API.

---

## 🚀 Features

- **Read:** Fetches a list of Todos from a remote server.
- **Create:** Add new tasks with custom titles and user IDs.
- **Update:** Modify existing tasks using the `PUT` method.
- **Delete:** Remove tasks with confirmation dialogs.
- **Advanced Error Handling:** Implemented a generic `Resource` wrapper class to handle:
    - Loading states (Progress Bars).
    - Network failures (No internet).
    - Server-side errors (400, 404, 500).
- **Modern UI:** Uses Material Design components like `FloatingActionButton`, `CardView`, and `RecyclerView`.

---

## 🛠 Tech Stack & Libraries

- **Language:** Java
- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & [GSON Converter](https://github.com/google/gson)
- **Reactive UI:** LiveData & ViewModel
- **UI Components:** RecyclerView, ConstraintLayout, Material Design

---

## 🏗 Architecture Pattern (MVVM)

This project follows the **Model-View-ViewModel (MVVM)** architecture to ensure separation of concerns and maintainability:

1.  **Model:** Data classes and POJOs representing the API response.
2.  **View (MainActivity):** Responsible for the UI and observing LiveData from the ViewModel.
3.  **ViewModel:** Acts as a bridge between the Repository and the View. It survives configuration changes (like screen rotation).
4.  **Repository:** Manages data logic and coordinates between the ApiService and ViewModel.

---

## 📂 Project Structure
