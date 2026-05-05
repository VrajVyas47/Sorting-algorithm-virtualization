package com.algorithmvisualizer.interfaces;

import javafx.scene.canvas.GraphicsContext;

public interface Visualizer {
    void draw(int[] array, int[] compareIdx, int[] swapIdx, int[] sortedIdx, GraphicsContext gc);
    String getDisplayName();
}
