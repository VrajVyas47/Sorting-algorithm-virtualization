package com.algorithmvisualizer.interfaces;

import com.algorithmvisualizer.model.AlgorithmStep;
import java.util.List;

public interface SortingAlgorithm {
    List<AlgorithmStep> generateSteps(int[] array);
    String getName();
    String getTimeComplexity();
    String getSpaceComplexity();
    String isStable();
}
