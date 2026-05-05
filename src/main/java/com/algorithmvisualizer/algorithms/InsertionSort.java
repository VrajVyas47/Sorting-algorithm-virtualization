package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InsertionSort implements SortingAlgorithm {

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int n = array.length;
        Set<Integer> sortedSet = new HashSet<>();
        sortedSet.add(0);
        
        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(sortedSet),
                AlgorithmStep.StepType.NO_OP,
                "Selected key at " + i
            ));
            
            while (j >= 0 && array[j] > key) {
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{j, j + 1},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.COMPARISON,
                    "Comparing " + j + " and " + (j + 1)
                ));
                array[j + 1] = array[j];
                steps.add(new AlgorithmStep(
                    Arrays.copyOf(array, n),
                    new int[]{},
                    new int[]{},
                    sortedSetToArray(sortedSet),
                    AlgorithmStep.StepType.OVERWRITE,
                    "Overwriting to " + (j + 1)
                ));
                j--;
            }
            array[j + 1] = key;
            steps.add(new AlgorithmStep(
                Arrays.copyOf(array, n),
                new int[]{},
                new int[]{},
                sortedSetToArray(sortedSet),
                AlgorithmStep.StepType.OVERWRITE,
                "Placed key at " + (j + 1)
            ));
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
        return steps;
    }

    private int[] sortedSetToArray(Set<Integer> sortedSet) {
        return sortedSet.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String getName() {
        return "Insertion Sort";
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
