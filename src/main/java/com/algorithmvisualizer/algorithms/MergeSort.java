package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.interfaces.SortingAlgorithm;
import com.algorithmvisualizer.model.AlgorithmStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort implements SortingAlgorithm {

    private List<AlgorithmStep> steps = new ArrayList<>();
    private int[] workingArray;

    @Override
    public List<AlgorithmStep> generateSteps(int[] array) {
        workingArray = array.clone();
        mergeSort(workingArray, 0, workingArray.length - 1);
        
        // Final mark
        int[] allSorted = new int[workingArray.length];
        for (int i = 0; i < workingArray.length; i++) allSorted[i] = i;
        steps.add(new AlgorithmStep(
            Arrays.copyOf(workingArray, workingArray.length),
            new int[]{},
            new int[]{},
            allSorted,
            AlgorithmStep.StepType.SORTED_MARK,
            "All sorted"
        ));
        
        return steps;
    }

    private void mergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private void merge(int[] arr, int left, int mid, int right) {
        int[] leftArr = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArr = Arrays.copyOfRange(arr, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            steps.add(new AlgorithmStep(
                Arrays.copyOf(arr, arr.length),
                new int[]{left + i, mid + 1 + j},
                new int[]{},
                new int[]{},
                AlgorithmStep.StepType.COMPARISON,
                "Comparing merging items"
            ));
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            steps.add(new AlgorithmStep(
                Arrays.copyOf(arr, arr.length),
                new int[]{},
                new int[]{},
                new int[]{},
                AlgorithmStep.StepType.OVERWRITE,
                "Writing back to array at " + k
            ));
            k++;
        }
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
            steps.add(new AlgorithmStep(
                Arrays.copyOf(arr, arr.length),
                new int[]{},
                new int[]{},
                new int[]{},
                AlgorithmStep.StepType.OVERWRITE,
                "Writing remaining left"
            ));
        }
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
            steps.add(new AlgorithmStep(
                Arrays.copyOf(arr, arr.length),
                new int[]{},
                new int[]{},
                new int[]{},
                AlgorithmStep.StepType.OVERWRITE,
                "Writing remaining right"
            ));
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    @Override
    public String isStable() {
        return "Yes";
    }
}
