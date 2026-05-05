package com.algorithmvisualizer.utils;

import javafx.util.Duration;

public class DelayHandler {

    public static Duration toDuration(double milliseconds) {
        return Duration.millis(milliseconds);
    }

    public static double labelToMs(String label) {
        switch (label) {
            case "Slow": return 1200;
            case "Medium": return 300;
            case "Fast": return 80;
            case "Turbo": return 20;
            default: return AppConstants.DEFAULT_SPEED_MS;
        }
    }
}
