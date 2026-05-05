package com.algorithmvisualizer.visualizers;

import com.algorithmvisualizer.interfaces.Visualizer;
import com.algorithmvisualizer.utils.AppConstants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BarVisualizer implements Visualizer {

    @Override
    public void draw(int[] array, int[] compareIdx, int[] swapIdx, int[] sortedIdx, GraphicsContext gc) {
        double canvasW = gc.getCanvas().getWidth();
        double canvasH = gc.getCanvas().getHeight();

        gc.setFill(Color.web(AppConstants.COLOR_BG_CANVAS));
        gc.fillRect(0, 0, canvasW, canvasH);

        if (array == null || array.length == 0) return;

        int n = array.length;
        int maxVal = Arrays.stream(array).max().getAsInt();
        double totalBarWidth = (canvasW - AppConstants.BAR_PADDING * (n + 1)) / n;

        Set<Integer> compareSet = toSet(compareIdx);
        Set<Integer> swapSet = toSet(swapIdx);
        Set<Integer> sortedSet = toSet(sortedIdx);

        for (int i = 0; i < n; i++) {
            double barHeight = (array[i] / (double) maxVal) * (canvasH - 20);
            double x = AppConstants.BAR_PADDING + i * (totalBarWidth + AppConstants.BAR_PADDING);
            double y = canvasH - barHeight;

            if (swapSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_SWAP));
            } else if (compareSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_COMPARE));
            } else if (sortedSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_SORTED));
            } else {
                gc.setFill(Color.web(AppConstants.COLOR_DEFAULT));
            }

            gc.fillRect(x, y, totalBarWidth, barHeight);

            if (totalBarWidth > 15) {
                gc.setFill(Color.web(AppConstants.COLOR_TEXT_PRIMARY));
                gc.setFont(Font.font(9));
                gc.fillText(String.valueOf(array[i]), x + 2, canvasH - 2);
            }
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
        return "Bar View";
    }
}
