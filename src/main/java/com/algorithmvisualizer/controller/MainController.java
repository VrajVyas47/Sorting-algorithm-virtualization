package com.algorithmvisualizer.controller;

import com.algorithmvisualizer.algorithms.*;
import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.interfaces.Visualizer;
import com.algorithmvisualizer.model.AlgorithmStep;
import com.algorithmvisualizer.utils.AppConstants;
import com.algorithmvisualizer.utils.ArrayGenerator;
import com.algorithmvisualizer.visualizers.BarVisualizer;
import com.algorithmvisualizer.visualizers.DotVisualizer;
import com.algorithmvisualizer.visualizers.HorizontalBarVisualizer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private ComboBox<String> visualizerComboBox;
    @FXML private Slider speedSlider;
    @FXML private TextField customArrayField;
    @FXML private Button generateButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;
    @FXML private Canvas visualCanvas;
    @FXML private Label statusLabel;
    @FXML private Label stepCountLabel;
    @FXML private Label comparisonsLabel;
    @FXML private Label swapsLabel;
    @FXML private Label algorithmInfoLabel;
    @FXML private ProgressBar sortProgress;

    private int[] currentArray;
    private List<AlgorithmStep> stepList;
    private int currentStepIndex;
    private Visualizer activeVisualizer;
    private Timeline animationTimeline;
    private boolean isPaused;
    private int comparisonCount;
    private int swapCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algorithmComboBox.getItems().addAll(
            "Bubble Sort", "Selection Sort", "Insertion Sort",
            "Cocktail Shaker Sort", "Comb Sort", "Merge Sort"
        );
        visualizerComboBox.getItems().addAll(
            "Bar View", "Dot View", "Horizontal Bar View"
        );

        algorithmComboBox.getSelectionModel().select(0);
        visualizerComboBox.getSelectionModel().select(0);

        speedSlider.setMin(AppConstants.MIN_SPEED_MS);
        speedSlider.setMax(AppConstants.MAX_SPEED_MS);
        speedSlider.setValue(AppConstants.DEFAULT_SPEED_MS);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (animationTimeline != null && animationTimeline.getStatus() == Animation.Status.RUNNING) {
                animationTimeline.stop();
                animationTimeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.millis(newVal.doubleValue()), event -> handleAnimationTick())
                );
                animationTimeline.play();
            }
        });

        startButton.setDisable(true);
        activeVisualizer = new BarVisualizer();
        generateRandomArray();
        updateAlgorithmInfo(algorithmComboBox.getValue());
    }

    private void handleAnimationTick() {
        if (stepList == null || currentStepIndex >= stepList.size()) {
            if (animationTimeline != null) animationTimeline.stop();
            markAllSorted();
            statusLabel.setText("Sorting Complete! ✓");
            reEnableControls();
            return;
        }
        AlgorithmStep step = stepList.get(currentStepIndex);
        applyStep(step);
        
        statusLabel.setText(step.description() != null ? step.description() : "Sorting...");
        stepCountLabel.setText("Step: " + (currentStepIndex + 1) + " / " + stepList.size());
        sortProgress.setProgress((double)(currentStepIndex + 1) / stepList.size());
        
        currentStepIndex++;
    }

    private void generateRandomArray() {
        currentArray = ArrayGenerator.generateRandom(AppConstants.DEFAULT_ARRAY_SIZE, AppConstants.MIN_VALUE, AppConstants.MAX_VALUE);
        resetState();
        updateVisualizer(currentArray, new int[]{}, new int[]{});
        startButton.setDisable(false);
        statusLabel.setText("Array ready. Press Start.");
    }

    private void resetState() {
        if (animationTimeline != null) animationTimeline.stop();
        stepList = null;
        currentStepIndex = 0;
        comparisonCount = 0;
        swapCount = 0;
        comparisonsLabel.setText("Comparisons: 0");
        swapsLabel.setText("Swaps: 0");
        stepCountLabel.setText("Step: 0 / 0");
        sortProgress.setProgress(0);
    }

    @FXML
    private void onGenerateButtonClicked(ActionEvent event) {
        if (animationTimeline != null) animationTimeline.stop();
        generateRandomArray();
    }

    @FXML
    private void onCustomArraySubmit(ActionEvent event) {
        String text = customArrayField.getText();
        int[] parsed = ArrayGenerator.parseCustomArray(text);
        if (parsed == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid comma-separated list of numbers (Min 5 elements) between 1 and 400.");
        } else {
            if (animationTimeline != null) animationTimeline.stop();
            currentArray = parsed;
            resetState();
            updateVisualizer(currentArray, new int[]{}, new int[]{});
            startButton.setDisable(false);
            statusLabel.setText("Custom array loaded. Press Start.");
        }
    }

    @FXML
    private void onStartButtonClicked(ActionEvent event) {
        if (isPaused) {
            isPaused = false;
            animationTimeline.play();
            startButton.setText("▶ Start");
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            return;
        }

        SortingAlgorithm algorithm = createAlgorithm(algorithmComboBox.getValue());
        if (algorithm == null) return;

        stepList = algorithm.generateSteps(currentArray.clone());
        currentStepIndex = 0;
        
        generateButton.setDisable(true);
        startButton.setDisable(true);
        algorithmComboBox.setDisable(true);
        visualizerComboBox.setDisable(true);
        customArrayField.setDisable(true);
        pauseButton.setDisable(false);

        playAnimation();
    }

    @FXML
    private void onPauseButtonClicked(ActionEvent event) {
        if (animationTimeline != null && animationTimeline.getStatus() == Animation.Status.RUNNING) {
            animationTimeline.pause();
            isPaused = true;
            startButton.setText("Resume");
            startButton.setDisable(false);
            pauseButton.setDisable(true);
            statusLabel.setText("Paused");
        }
    }

    @FXML
    private void onResetButtonClicked(ActionEvent event) {
        if (animationTimeline != null) animationTimeline.stop();
        isPaused = false;
        reEnableControls();
        startButton.setText("▶ Start");
        generateRandomArray();
    }

    private void reEnableControls() {
        generateButton.setDisable(false);
        startButton.setDisable(false);
        algorithmComboBox.setDisable(false);
        visualizerComboBox.setDisable(false);
        customArrayField.setDisable(false);
        pauseButton.setDisable(true);
    }

    @FXML
    private void onAlgorithmChanged(ActionEvent event) {
        updateAlgorithmInfo(algorithmComboBox.getValue());
        if (animationTimeline != null) animationTimeline.stop();
        reEnableControls();
        updateVisualizer(currentArray, new int[]{}, new int[]{});
    }

    @FXML
    private void onVisualizerChanged(ActionEvent event) {
        String val = visualizerComboBox.getValue();
        if ("Dot View".equals(val)) {
            activeVisualizer = new DotVisualizer();
        } else if ("Horizontal Bar View".equals(val)) {
            activeVisualizer = new HorizontalBarVisualizer();
        } else {
            activeVisualizer = new BarVisualizer();
        }
        
        if (stepList != null && currentStepIndex > 0 && currentStepIndex <= stepList.size()) {
            AlgorithmStep step = stepList.get(Math.min(currentStepIndex, stepList.size() - 1));
            applyStep(step);
        } else {
            updateVisualizer(currentArray, new int[]{}, new int[]{});
        }
    }

    private void playAnimation() {
        animationTimeline = new Timeline(
            new KeyFrame(Duration.millis(speedSlider.getValue()), event -> handleAnimationTick())
        );
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    private void applyStep(AlgorithmStep step) {
        GraphicsContext gc = visualCanvas.getGraphicsContext2D();
        activeVisualizer.draw(step.array(), step.compareIndices(), step.swapIndices(), step.sortedIndices(), gc);

        if (step.type() == AlgorithmStep.StepType.COMPARISON) {
            comparisonCount++;
            comparisonsLabel.setText("Comparisons: " + comparisonCount);
        } else if (step.type() == AlgorithmStep.StepType.SWAP) {
            swapCount++;
            swapsLabel.setText("Swaps: " + swapCount);
        }
    }

    private void updateVisualizer(int[] array, int[] compareIdx, int[] swapIdx) {
        GraphicsContext gc = visualCanvas.getGraphicsContext2D();
        activeVisualizer.draw(array, compareIdx, swapIdx, new int[]{}, gc);
    }

    private void markAllSorted() {
        int[] sorted = new int[currentArray.length];
        for (int i = 0; i < currentArray.length; i++) sorted[i] = i;
        GraphicsContext gc = visualCanvas.getGraphicsContext2D();
        int[] current = stepList != null && !stepList.isEmpty() ? stepList.get(stepList.size()-1).array() : currentArray;
        activeVisualizer.draw(current, new int[]{}, new int[]{}, sorted, gc);
    }

    private SortingAlgorithm createAlgorithm(String name) {
        switch (name) {
            case "Bubble Sort": return new BubbleSort();
            case "Selection Sort": return new SelectionSort();
            case "Insertion Sort": return new InsertionSort();
            case "Cocktail Shaker Sort": return new CocktailSort();
            case "Comb Sort": return new CombSort();
            case "Merge Sort": return new MergeSort();
            default: throw new IllegalArgumentException("Unknown algorithm: " + name);
        }
    }

    private void updateAlgorithmInfo(String algorithmName) {
        if (algorithmName == null) return;
        String info = getComplexityInfo(algorithmName);
        if (algorithmInfoLabel != null) algorithmInfoLabel.setText(info);
    }

    private String getComplexityInfo(String algorithmName) {
        switch (algorithmName) {
            case "Bubble Sort": return "Bubble Sort\nTime: O(n²) avg\nSpace: O(1)\nStable: Yes";
            case "Selection Sort": return "Selection Sort\nTime: O(n²)\nSpace: O(1)\nStable: No";
            case "Insertion Sort": return "Insertion Sort\nTime: O(n²) avg\nSpace: O(1)\nStable: Yes";
            case "Cocktail Shaker Sort": return "Cocktail Sort\nTime: O(n²) avg\nSpace: O(1)\nStable: Yes";
            case "Comb Sort": return "Comb Sort\nTime: O(n log n)\nSpace: O(1)\nStable: No";
            case "Merge Sort": return "Merge Sort\nTime: O(n log n)\nSpace: O(n)\nStable: Yes";
            default: return "—";
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #1A1A2E;");
        alert.showAndWait();
    }
}
