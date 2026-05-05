package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CocktailSort implements SortingAlgorithm {

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int n = array.length;
        boolean swapped = true;
        int start = 0;
        int end = n - 1;
        Set<Integer> sortedSet = new HashSet<>();

        while (swapped) {
            swapped = false;
            for (int i = start; i < end; i++) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{i, i + 1},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing " + i + " and " + (i + 1)
                ));
                if (array[i] > array[i + 1]) {
                    swap(array, i, i + 1);
                    steps.add(new AlgorithmStep(
                        Arrays.copyOf(array, n),
                        new int[]{},
                        new int[]{i, i + 1},
                        sortedSetToArray(sortedSet),
                        AlgorithmStep.StepType.SWAP,
                        "Swapping " + i + " and " + (i + 1)
                    ));
                    swapped = true;
                }
            }
            sortedSet.add(end);
            end--;
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(sortedSet),
                AlgorithmStep.StepType.SORTED_MARK,
                "Position " + (end + 1) + " is sorted"
            ));

            if (!swapped) break;

            swapped = false;
            for (int i = end; i > start; i--) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{i - 1, i},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing " + (i - 1) + " and " + i
                ));
                if (array[i] < array[i - 1]) {
                    swap(array, i - 1, i);
                    steps.add(new AlgorithmStep(
                        Arrays.copyOf(array, n),
                        new int[]{},
                        new int[]{i - 1, i},
                        sortedSetToArray(sortedSet),
                        AlgorithmStep.StepType.SWAP,
                        "Swapping " + (i - 1) + " and " + i
                    ));
                    swapped = true;
                }
            }
            sortedSet.add(start);
            start++;
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(sortedSet),
                AlgorithmStep.StepType.SORTED_MARK,
                "Position " + (start - 1) + " is sorted"
            ));
        }
        for (int i = 0; i < n; i++) {
            sortedSet.add(i);
        }
        steps.add(new AlgorithmStep(
            Arrays.copyOf(array, n),
            new int[]{},
            new int[]{},
            sortedSetToArray(sortedSet),
            AlgorithmStep.StepType.SORTED_MARK,
            "All sorted"
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
        return "Cocktail Shaker Sort";
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
