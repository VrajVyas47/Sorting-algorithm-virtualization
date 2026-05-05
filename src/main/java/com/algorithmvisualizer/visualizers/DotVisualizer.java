package com.algorithmvisualizer.visualizers;

import com.algorithmvisualizer.interfaces.Visualizer;
import com.algorithmvisualizer.utils.AppConstants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DotVisualizer implements Visualizer {

    @Override
    public void draw(int[] array, int[] compareIdx, int[] swapIdx, int[] sortedIdx, GraphicsContext gc) {
        double canvasW = gc.getCanvas().getWidth();
        double canvasH = gc.getCanvas().getHeight();

        gc.setFill(Color.web(AppConstants.COLOR_BG_CANVAS));
        gc.fillRect(0, 0, canvasW, canvasH);

        if (array == null || array.length == 0) return;

        int n = array.length;
        int maxVal = Arrays.stream(array).max().getAsInt();
        double xStep = canvasW / (double)(n + 1);
        double yRange = canvasH - 30;

        Set<Integer> compareSet = toSet(compareIdx);
        Set<Integer> swapSet = toSet(swapIdx);
        Set<Integer> sortedSet = toSet(sortedIdx);

        for (int i = 0; i < n; i++) {
            double x = (i + 1) * xStep;
            double y = canvasH - 10 - (array[i] / (double)maxVal) * yRange;

            Color c;
            if (swapSet.contains(i)) {
                c = Color.web(AppConstants.COLOR_SWAP);
            } else if (compareSet.contains(i)) {
                c = Color.web(AppConstants.COLOR_COMPARE);
            } else if (sortedSet.contains(i)) {
                c = Color.web(AppConstants.COLOR_SORTED);
            } else {
                c = Color.web(AppConstants.COLOR_DEFAULT);
            }

            gc.setStroke(Color.web("#2A4060"));
            gc.setLineWidth(1);
            gc.strokeLine(x, canvasH - 10, x, y + 5);

            gc.setFill(c);
            gc.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    private Set<Integer> toSet(int[] array) {
        Set<Integer> set = new HashSet<>();
        if (array != null) {
            for (int j : array) set.add(j);
        }
        return set;
    }

    @Override
    public String getDisplayName() {
        return "Dot View";
    }
}
