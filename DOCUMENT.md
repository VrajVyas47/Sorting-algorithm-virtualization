# 📖 Multi-View Sorting Algorithm Visualizer – Exhaustive Developer Documentation

Welcome to the absolute, deep-dive reference manual for the **JavaFX Sorting Algorithm Visualizer**. This document leaves no stone unturned. If you need to understand the exact mathematical formulas used to render pixels, the precise threading model for the animation timeline, or how every variable is manipulated during a runtime state change, this is your source of truth.

---

## 📑 Comprehensive Table of Contents
1. [System Architecture & JavaFX Lifecycle](#1-system-architecture--javafx-lifecycle)
2. [Deep-Dive File Structure & Class Responsibilities](#2-deep-dive-file-structure--class-responsibilities)
3. [The User Interface Layer (FXML & CSS Physics)](#3-the-user-interface-layer-fxml--css-physics)
4. [State Management & The Controller Engine](#4-state-management--the-controller-engine)
5. [The Animation & Threading Model (The Timeline)](#5-the-animation--threading-model-the-timeline)
6. [Data Models: The `AlgorithmStep` Record](#6-data-models-the-algorithmstep-record)
7. [Visualizer Mathematics & Pixel Rendering](#7-visualizer-mathematics--pixel-rendering)
8. [Algorithm Implementations & Step Generation](#8-algorithm-implementations--step-generation)
9. [Utility Tools & Constants](#9-utility-tools--constants)
10. [Event Flow Trace (What happens when...)](#10-event-flow-trace-what-happens-when)

---

## 1. System Architecture & JavaFX Lifecycle

The application is built on **pure Java 17** and **JavaFX 17**, managed by **Maven**. There are absolutely no third-party libraries (no Spring, no Guava, no Apache Commons). It relies strictly on `javafx.controls`, `javafx.fxml`, and `javafx.graphics`.

### The Build System (`pom.xml`)
The `pom.xml` configures the `maven-compiler-plugin` to target Java 17. The `javafx-maven-plugin` is configured to map the execution target to `com.algorithmvisualizer.MainApp`. 

### The Lifecycle Trigger (`MainApp.java`)
1. **JVM Starts:** User runs `mvn javafx:run`.
2. **Launch:** `MainApp.main()` calls `Application.launch(args)`.
3. **Init:** JavaFX provisions the Application Thread (JAT).
4. **Start:** `start(Stage primaryStage)` is invoked on the JAT.
   - Creates an `FXMLLoader` and points it to `/com/algorithmvisualizer/fxml/main_view.fxml`.
   - The FXML is parsed. While parsing, JavaFX reflects upon `MainController.java`.
   - The `@FXML` annotated fields in the Controller are populated with the instantiated UI objects.
   - The `MainController.initialize()` method is called by JavaFX.
   - The `Scene` is created with strict dimensions `1100x700` (`AppConstants.WINDOW_WIDTH`, `WINDOW_HEIGHT`).
   - The stylesheet `/com/algorithmvisualizer/css/style.css` is injected into the Scene graph.
   - `primaryStage.show()` is called, rendering the first frame.

---

## 2. Deep-Dive File Structure & Class Responsibilities

```text
AlgorithmVisualizer/
├── pom.xml                                   (Dependency & Build config)
└── src/
    └── main/
        ├── java/com/algorithmvisualizer/
        │   ├── MainApp.java                  (JavaFX Application Entry)
        │   ├── controller/
        │   │   └── MainController.java       (UI interaction, event routing, Timeline management)
        │   ├── interfaces/
        │   │   ├── Visualizer.java           (Contract for drawing frames to Canvas)
        │   │   └── SortingAlgorithm.java     (Contract for generating AlgorithmSteps)
        │   ├── model/
        │   │   └── AlgorithmStep.java        (Immutable Record mapping the array state at a specific micro-tick)
        │   ├── algorithms/                   (Concrete Strategy Implementations)
        │   │   ├── BubbleSort.java           (O(N^2) Adjacent Swap)
        │   │   ├── SelectionSort.java        (O(N^2) Min-Finding Swap)
        │   │   ├── InsertionSort.java        (O(N^2) Shifting)
        │   │   ├── CocktailSort.java         (Bidirectional Bubble)
        │   │   ├── CombSort.java             (Gap-shrinking Bubble)
        │   │   └── MergeSort.java            (O(N log N) Divide/Conquer overwrite)
        │   ├── visualizers/                  (Concrete Canvas Painters)
        │   │   ├── BarVisualizer.java        (Calculates vertical rects)
        │   │   ├── DotVisualizer.java        (Calculates point coordinates)
        │   │   └── HorizontalBarVisualizer.java (Calculates horizontal rects)
        │   └── utils/
        │       ├── AppConstants.java         (Global config, dimensions, hex colors)
        │       ├── ArrayGenerator.java       (Random int generators, CSV parsers)
        │       └── DelayHandler.java         (Converts tick times to JavaFX Duration)
        └── resources/com/algorithmvisualizer/
            ├── fxml/
            │   └── main_view.fxml            (XML layout tree)
            └── css/
                └── style.css                 (Styling rules applied to FXML classes)
```

---

## 3. The User Interface Layer (FXML & CSS Physics)

The UI is strictly separated into layout (`.fxml`) and styling (`.css`).

### FXML Structure Mechanics (`main_view.fxml`)
The root node is a `BorderPane`. This pane divides the window into 5 distinct regions (Top, Bottom, Left, Right, Center). We utilize Top, Center, and Right.

*   **`<top>`**: An `HBox` aligned to `CENTER` holding the Window Title text.
*   **`<center>`**: A `VBox` containing:
    *   **`Canvas` (ID: visualCanvas)**: Hardcoded to `760x480`. This is a raw pixel buffer. It has no layout semantics. Java controls it via `GraphicsContext`.
    *   **Info Bar (`HBox`)**: Houses text labels (`statusLabel`, `stepCountLabel`, etc.) with `spacing="30"` for padding.
    *   **`ProgressBar`**: Stretches to `760px` to match the Canvas width exactly.
*   **`<right>`**: A `VBox` (Width: 310) serving as the Control Panel.
    *   Contains `ComboBox`es, `Slider`, `TextField`, and `Button`s.
    *   `Insets` applied to add exactly 20px padding top/bottom and 15px left/right.
    *   `Separator` elements are used to explicitly map out visual sections.

### CSS Styling Mapping (`style.css`)
JavaFX CSS differs slightly from Web CSS by prefixing properties with `-fx-`.

*   **Colors**: The `.root-pane` utilizes `#1A1A2E` (Navy Base). Control panels use `#16213E`.
*   **Buttons**: The standard button `.btn-primary` uses `#E94560`. It specifies `-fx-cursor: hand;` to change the mouse pointer, resolving a common JavaFX quirk where buttons don't have pointer cursors by default.
*   **Pseudo-classes**: Hover states are handled directly in CSS (e.g., `.btn-primary:hover {-fx-background-color: #FF6B6B;}`). Disabled states have `-fx-opacity: 0.4;`.
*   **Overrides**: Classes like `.speed-slider` use `-fx-accent: #E94560;` to recolor the thumb and progress track of JavaFX's default slider.

---

## 4. State Management & The Controller Engine

The `MainController.java` is a giant state machine. Here are the exact memory components it tracks:

### Volatile State Variables
*   `int[] currentArray`: The authoritative array data. If animation is paused or stopped, this holds the current arrangement of integers.
*   `List<AlgorithmStep> stepList`: A pre-computed manifest of every single frame of the animation. This list can range from 10 items to 10,000+ items depending on array size and algorithm.
*   `int currentStepIndex`: The playhead pointer. It points to the specific index of `stepList` currently being rendered.
*   `Visualizer activeVisualizer`: A polymorphic reference storing whether we are currently parsing `BarVisualizer`, `DotVisualizer`, etc.
*   `Timeline animationTimeline`: The JavaFX chronometer object responsible for dispatching tasks.
*   `boolean isPaused`: Simple flag preventing timeline restart anomalies.
*   `int comparisonCount`, `int swapCount`: Accumulators that reset on "Generate Array" and increment during animation playback.

### Initialization Sequence (`initialize` method)
Upon software start, the controller:
1. Hardcodes the dropdown items.
2. Selects index 0 for both dropdowns automatically.
3. Sets up an active listener on the `speedSlider`: `speedSlider.valueProperty().addListener(...)`. If dragged, it dynamically replaces the `Timeline` keyframes without losing the `currentStepIndex`.
4. Initializes `activeVisualizer = new BarVisualizer()`.
5. Calls `generateRandomArray()` to immediately seed the screen so it isn't blank on launch.

---

## 5. The Animation & Threading Model (The Timeline)

Directly modifying a JavaFX UI component or Canvas inside a standard Java `while` or `for` loop will completely freeze the application window until the loop finishes. 

To bypass this without threading nightmares, we use **Deterministic Pre-Computation**.

### Phase 1: The Pre-Computation Freeze
When you press `Start`, we do exactly what would normally freeze the UI: we run the sorting algorithm completely. 
However, because it is running on a memory-cloned array tracking integers, the CPU resolves an array of 50 items in about 2 milliseconds. During these 2 ms, the algorithm (e.g. BubbleSort) generates a giant list of `AlgorithmStep` objects. Once returned, the Controller holds the entire "script" of the movie.

### Phase 2: The JavaFX Timeline
We initialize a `Timeline`.
```java
animationTimeline = new Timeline(
    new KeyFrame(Duration.millis(speedSlider.getValue()), event -> handleAnimationTick())
);
animationTimeline.setCycleCount(Timeline.INDEFINITE);
animationTimeline.play();
```
*   **`KeyFrame`**: Defines an action to happen every *X* milliseconds. 
*   **`event -> handleAnimationTick()`**: The lambda expression executed by the internal JavaFX UI Thread. Since the JAT handles it, we safely read from `stepList` and draw to the canvas without `Platform.runLater()` anomalies or `ConcurrentModificationException`s.

### The Tick Logic
Every time `handleAnimationTick()` fires:
1. It validates: `if (currentStepIndex >= stepList.size())`, animation is over. Stop timeline, trigger post-sort UI (enable buttons, paint everything green).
2. It fetches `Step = stepList.get(currentStepIndex)`.
3. It pushes the data directly to the visualizer: `activeVisualizer.draw(step.array(), step.compareIndices()...)`.
4. Increments the statistics UI based on what `StepType` the step had.
5. Advances the playhead: `currentStepIndex++`.

---

## 6. Data Models: The `AlgorithmStep` Record

The `AlgorithmStep.java` is a Java `record` (introduced in Java 14+). Records are strictly immutable carriers.

We store:
*   `int[] array`: A **cloned snapshot** of the array at the exact micro-second. (Crucial: It must be a clone via `Arrays.copyOf()`. If we passed the reference, every step in the list would point to the fully-sorted final array instantly).
*   `int[] compareIndices`: Used to paint specific bars **Yellow**.
*   `int[] swapIndices`: Used to paint specific bars **Red**.
*   `int[] sortedIndices`: Used to paint specific bars **Green** permanently.
*   `StepType type`: An Enum (`COMPARISON`, `SWAP`, `OVERWRITE`, `NO_OP`, `SORTED_MARK`). Used by the controller to know which statistical integer to increment.
*   `String description`: Text to push to `statusLabel` ("Swapping index 3 and 4").

---

## 7. Visualizer Mathematics & Pixel Rendering

Visualizers take the data and translate it to coordinates on a `760 x 480` Canvas via `GraphicsContext (gc)`. Let's examine `BarVisualizer.draw()`.

### The Drawing Engine Reset
```java
gc.setFill(Color.web(AppConstants.COLOR_BG_CANVAS));
gc.fillRect(0, 0, canvasW, canvasH);
```
Every single frame completely wipes the Canvas black/navy. We overdraw 100% of the screen.

### The Bar Geometry Formula
We need `N` bars to fit perfectly within `760px`, with a `2px` padding between them.
```java
double totalBarWidth = (canvasW - BAR_PADDING * (n + 1)) / n;
```
*   `BAR_PADDING * (n + 1)` calculates the total pixel space consumed by gaps (there is a gap before the first bar, between each, and after the last).
*   `(canvasW - gaps)` gives us usable pixel space.
*   `/ n` divides usable space equally among the rectangles.

### The Height Formula
```java
int maxVal = Arrays.stream(array).max().getAsInt();
double barHeight = (array[i] / (double) maxVal) * (canvasH - 20);
```
*   Find the highest number in the array. That number represents `100% height`.
*   Take the current number `array[i] / maxVal`. (e.g., if array[i] is 50, and max is 100, we get `0.5`).
*   Multiply by `(canvasH - 20)`. The `-20` acts as a top-margin so the bars don't clip the top of the canvas. Height = `0.5 * 460 = 230px`.

### The Placement Formula
```java
double x = AppConstants.BAR_PADDING + i * (totalBarWidth + AppConstants.BAR_PADDING);
double y = canvasH - barHeight; // JavaFX Y-axis originates at top-left.
gc.fillRect(x, y, totalBarWidth, barHeight);
```

### The Color Resolution
It uses `Set.contains(i)` to evaluate the priority:
1. Is it swapping? Red.
2. Else if compared? Yellow.
3. Else if sorted? Green.
4. Else, Default Blue.

---

## 8. Algorithm Implementations & Step Generation

Every Algorithm overrides `public List<AlgorithmStep> generateSteps(int[] array)`.

### Bubble Sort Pattern
*   Outer Loop `i`: Controls sorting passes.
*   Inner Loop `j`: Evaluates `j` and `j+1`.
*   Records `COMPARISON` step for `[j, j+1]`.
*   If `arr[j] > arr[j+1]`, it swaps the memory, then immediately records a `SWAP` step for `[j, j+1]`.
*   At the end of inner loop, `n-i-1` is finalized. Recorded as `SORTED_MARK`.

### Merge Sort Nuance
Merge sort requires array splitting, meaning we aren't just doing direct 1-to-1 index swaps. 
*   We use an `OVERWRITE` StepType. 
*   When rebuilding the parent array from the left and right temp arrays, we log the index `k` where we are forcefully injecting a value. The visualizer paints index `k` red/purple, and overwrites the bar's height instantly.

---

## 9. Utility Tools & Constants

*   **`AppConstants.java`**: By consolidating `WINDOW_WIDTH`, `MAX_VALUE` (400), and hex colors (e.g., `#E94560`) into public static final strings, we prevent "Magic Numbers". Changing the array maximum from 400 to 1000 requires changing precisely one line of code in the entire repository.
*   **`ArrayGenerator.java`**: 
    *   `generateRandom()` uses `java.util.Random`.
    *   `parseCustomArray()` uses `String.split(",")`, applies `Integer.parseInt()`, and checks boundaries. Wraps in a `try-catch` to reject strings like `"apple"`.
*   **`DelayHandler.java`**: Maps English labels (Fast, Slow) to millisecond doubles (80, 1200) for edge-case uses.

---

## 10. Event Flow Trace (What happens when...)

**Scenario: You type "5,1,9", click "Set", and click "Start".**

1.  **Click "Set"**:
    *   `MainController.onCustomArraySubmit()` fires.
    *   Reads `"5,1,9"`. Gives it to `ArrayGenerator.parseCustomArray`.
    *   Fails silently/warns because minimum size is 5 (`MIN_ARRAY_SIZE`).
2.  **You correct it to "5,1,9,4,2". Click "Set"**:
    *   Validation passes. `currentArray` is overwritten in memory to `[5,1,9,4,2]`.
    *   `updateVisualizer()` is called, immediately re-drawing 5 wide bars on the canvas.
3.  **Click "Start"**:
    *   `onStartButtonClicked()` fires.
    *   Dropdown says "Bubble Sort". Instantiates `BubbleSort()`.
    *   Passes `[5,1,9,4,2]` to `bubbleSort.generateSteps()`.
    *   Bubble run inside memory: 
        *   Compares 5,1 -> Swaps to 1,5 -> Logs step.
        *   Compares 5,9 -> No swap -> Logs step.
        *   (Returns list of ~25 Step objects).
    *   Buttons disabled via `button.setDisable(true)`.
    *   Timeline initiated.
4.  **Ticks Begin**:
    *   Tick 0: Canvas draws frame 0. `Comparisons: 1` Text updates.
    *   ...
    *   Tick 25: `if (index >= list.size())` triggers.
    *   `markAllSorted()` invoked -> Overwrites `sortedIndices` to `[0,1,2,3,4]`. BarVisualizer draws all 5 bars `#00E676` (Green).
    *   Timeline halts. Buttons unlocked. Sort is finished.

---
*Developed as an engineering guide for deep-dive application tracing. Every line of Java and JavaFX architecture executes precisely as outlined above.*
