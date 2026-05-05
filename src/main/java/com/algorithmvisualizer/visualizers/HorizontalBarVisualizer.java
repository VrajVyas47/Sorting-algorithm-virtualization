package com.algorithmvisualizer.visualizers;

import com.algorithmvisualizer.interfaces.Visualizer;
import com.algorithmvisualizer.utils.AppConstants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HorizontalBarVisualizer implements Visualizer {

    @Override
    public void draw(int[] array, int[] compareIdx, int[] swapIdx, int[] sortedIdx, GraphicsContext gc) {
        double canvasW = gc.getCanvas().getWidth();
        double canvasH = gc.getCanvas().getHeight();

        gc.setFill(Color.web(AppConstants.COLOR_BG_CANVAS));
        gc.fillRect(0, 0, canvasW, canvasH);

        if (array == null || array.length == 0) return;

        int n = array.length;
        int maxVal = Arrays.stream(array).max().getAsInt();

        double rowHeight = (canvasH - AppConstants.BAR_PADDING * (n + 1)) / (double) n;

        Set<Integer> compareSet = toSet(compareIdx);
        Set<Integer> swapSet = toSet(swapIdx);
        Set<Integer> sortedSet = toSet(sortedIdx);

        for (int i = 0; i < n; i++) {
            double barWidth = (array[i] / (double)maxVal) * (canvasW - 60);
            double y = AppConstants.BAR_PADDING + i * (rowHeight + AppConstants.BAR_PADDING);

            if (swapSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_SWAP));
            } else if (compareSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_COMPARE));
            } else if (sortedSet.contains(i)) {
                gc.setFill(Color.web(AppConstants.COLOR_SORTED));
            } else {
                gc.setFill(Color.web(AppConstants.COLOR_DEFAULT));
            }

            gc.fillRect(40, y, barWidth, rowHeight - 1);

            gc.setFill(Color.web(AppConstants.COLOR_TEXT_SECONDARY));
            gc.setFont(Font.font(10));
            gc.fillText(String.valueOf(i), 5, y + rowHeight / 2 + 4);

            if (barWidth > 25) {
                gc.setFill(Color.web(AppConstants.COLOR_TEXT_PRIMARY));
                gc.fillText(String.valueOf(array[i]), 44 + barWidth - 20, y + rowHeight / 2 + 4);
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
        return "Horizontal Bar View";
    }
}
