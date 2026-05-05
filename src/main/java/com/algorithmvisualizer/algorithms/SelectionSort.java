package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionSort implements SortingAlgorithm {

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        List<AlgorithmStep> steps = new ArrayList<>();
        Set<Integer> sortedSet = new HashSet<>();
        int n = array.length;
        
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{j, minIdx},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing current with min: " + j + " vs " + minIdx
                ));
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(array, i, minIdx);
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{},
                    new int[]{i, minIdx},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.SWAP,
                    "Swapping " + i + " and " + minIdx
                ));
            }
            sortedSet.add(i);
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(sortedSet),
                AlgorithmStep.StepType.SORTED_MARK,
                "Position " + i + " is sorted"
            ));
        }
        sortedSet.add(n - 1);
        steps.add(new AlgorithmStep(
            Arrays.copyOf(array, n),
            new int[]{},
            new int[]{},
            sortedSetToArray(sortedSet),
            AlgorithmStep.StepType.SORTED_MARK,
            "Position " + (n - 1) + " is sorted"
        ));
        
        return steps;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int[] sortedSetToArray(Set<Integer> sortedSet) {
        return sortedSet.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String getName() {
        return "Selection Sort";
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
        return "No";
    }
}
