package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombSort implements SortingAlgorithm {

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int n = array.length;
        double shrink = 1.3;
        int gap = n;
        boolean sorted = false;
        Set<Integer> sortedSet = new HashSet<>();

        while (!sorted) {
            gap = (int) (gap / shrink);
            if (gap <= 1) {
                gap = 1;
                sorted = true;
            }

            for (int i = 0; i < n - gap; i++) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{i, i + gap},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing " + i + " and " + (i + gap)
                ));
                if (array[i] > array[i + gap]) {
                    swap(array, i, i + gap);
                    steps.add(new AlgorithmStep(
                        Arrays.copyOf(array, n),
                        new int[]{},
                        new int[]{i, i + gap},
                        sortedSetToArray(sortedSet),
                        AlgorithmStep.StepType.SWAP,
                        "Swapping " + i + " and " + (i + gap)
                    ));
                    sorted = false;
                }
            }
        }

        for (int i = 0; i < n; i++) sortedSet.add(i);
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
        return "Comb Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
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
