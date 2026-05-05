package com.algorithmvisualizer.model;

public record AlgorithmStep(
    int[] array,
    int[] compareIndices,
    int[] swapIndices,
    int[] sortedIndices,
    StepType type,
    String description
) {
    public enum StepType {
        COMPARISON,
        SWAP,
        SORTED_MARK,
        OVERWRITE,
        NO_OP
    }
}
