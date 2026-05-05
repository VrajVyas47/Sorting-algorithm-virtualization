package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BubbleSort implements SortingAlgorithm {

    private List<AlgorithmStep> steps = new ArrayList<>();
    private Set<Integer> sortedSet = new HashSet<>();

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{j, j + 1},
                    new int[]{},
                    sortedSetToArray(),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing index " + j + " and " + (j + 1)
                ));
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    steps.add(new AlgorithmStep(
                        Arrays.copyOf(array, n),
                        new int[]{},
                        new int[]{j, j + 1},
                        sortedSetToArray(),
                        AlgorithmStep.StepType.SWAP,
                        "Swapping index " + j + " and " + (j + 1)
                    ));
                }
            }
            sortedSet.add(n - i - 1);
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(),
                AlgorithmStep.StepType.SORTED_MARK,
                "Position " + (n - i - 1) + " is sorted"
            ));
        }
        sortedSet.add(0);
        steps.add(new AlgorithmStep(
            Arrays.copyOf(array, n),
            new int[]{},
            new int[]{},
            sortedSetToArray(),
            AlgorithmStep.StepType.SORTED_MARK,
            "Position 0 is sorted"
        ));
        return steps;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int[] sortedSetToArray() {
        return sortedSet.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public String isStable() {
        return "Yes";
    }
}
