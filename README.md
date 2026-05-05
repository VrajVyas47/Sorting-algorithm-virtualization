# 📊 Multi-View Sorting Algorithm Visualizer

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-17-47808E?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

A feature-rich, standalone desktop application built entirely with **JavaFX** and **Java 17**. This educational tool visualizes the inner workings of various sorting algorithms in real-time. Instead of just standard vertical bars, this visualizer allows you to dynamically switch between multiple graphical representations, pause execution, inject custom arrays, and adjust the playback speed on the fly.

---

## ✨ Key Features

*   **⚡ 6 Sorting Algorithms:** Bubble, Selection, Insertion, Cocktail Shaker, Comb, and Merge Sort.
*   **🎨 3 Visualization Modes:** 
    *   **Bar View:** Classic vertical magnitude representing values.
    *   **Dot/Scatter View:** Traces the algorithmic "curve" and patterns visually.
    *   **Horizontal View:** Sideways layout for a different spatial perspective.
*   **🎮 Full Playback Control:** Play, Pause, Resume, and Reset capabilities.
*   **⏱️ Live Speed Adjustment:** Change the tick-rate (delay) instantly mid-sort using a slider.
*   **✏️ Custom Inputs:** Input your own comma-separated array (e.g., `5,1,10,3,2`) to test specific edge cases.
*   **📈 Live Statistics:** Real-time counters for Array Swaps and Element Comparisons.
*   **🧠 Architecture:** Built using a zero-blocking **Deterministic Pre-Computation** threading model ensuring a 60FPS UI without JavaFX concurrency freezes.

---

## 🚀 Getting Started

### Prerequisites

To run this project, you need exactly two things installed on your system:
1.  **JDK 17** (or newer)
2.  **Apache Maven** (3.8.x or newer)

_You do not need to install JavaFX manually. Maven will download the correct JavaFX modules for your operating system automatically!_

### Terminal / Command Line Instructions

1.  **Clone or Download** this repository.
2.  Open your terminal and navigate to the project directory (where the `pom.xml` file is located).
3.  **Compile the project:**
    ```bash
    mvn clean compile
    ```
4.  **Run the application:**
    ```bash
    mvn javafx:run
    ```

### IDE Instructions (IntelliJ IDEA)
1.  Open the folder in IntelliJ IDEA.
2.  IntelliJ will automatically detect it as a Maven project and download dependencies.
3.  Open the **Maven** side-panel on the right.
4.  Expand `AlgorithmVisualizer` -> `Plugins` -> `javafx`.
5.  Double-click `javafx:run`.

---

## 🧩 Included Algorithms & Time Complexities

| Algorithm | Average Time | Worst Time | Space | Stable |
| :--- | :--- | :--- | :--- | :--- |
| **Bubble Sort** | O(N²) | O(N²) | O(1) | Yes |
| **Selection Sort** | O(N²) | O(N²) | O(1) | No |
| **Insertion Sort** | O(N²) | O(N²) | O(1) | Yes |
| **Cocktail Shaker Sort**| O(N²) | O(N²) | O(1) | Yes |
| **Comb Sort** | O(N log N) | O(N²) | O(1) | No |
| **Merge Sort** | O(N log N) | O(N log N) | O(N) | Yes |

---

## 🎨 Color Coding Guide

During playback, elements are color-coded to indicate memory manipulation:
*   🟦 **Sky Blue:** Default, resting state.
*   🟨 **Amber Yellow:** Elements currently being **compared**.
*   🟥 **Vivid Red:** Elements actively being **swapped** or overwritten.
*   🟩 **Emerald Green:** Elements confirmed in their finalized sorted position.

---

## 📚 Further Reading & Developer Details

This project is fully documented locally! 

If you are a developer looking to understand how the JavaFX Application Thread works, how we draw to the `Canvas` using formulas, or how the codebase is architected, please read the included deep-dive rulebook:

👉 **[Read the Exhaustive Developer Documentation (DOCUMENTATION.md)](DOCUMENTATION.md)**

---

*Built with ❤️ using Java & JavaFX.*
