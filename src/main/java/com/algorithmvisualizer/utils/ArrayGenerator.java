package com.algorithmvisualizer.utils;

import java.util.Random;

public class ArrayGenerator {

    public static int[] generateRandom(int size, int min, int max) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    public static int[] parseCustomArray(String input) {
        if (input == null || input.isBlank()) return null;
        String[] parts = input.trim().split(",");
        int[] result = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Integer.parseInt(parts[i].trim());
                if (result[i] < 1 || result[i] > AppConstants.MAX_VALUE) return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        if (result.length < AppConstants.MIN_ARRAY_SIZE || result.length > AppConstants.MAX_ARRAY_SIZE) return null;
        return result;
    }

    public static int[] generateNearlySorted(int size) {
        int[] arr = generateSorted(size);
        Random r = new Random();
        int swaps = size / 10;
        for (int i = 0; i < swaps; i++) {
            int a = r.nextInt(size);
            int b = r.nextInt(size);
            int tmp = arr[a];
            arr[a] = arr[b];
            arr[b] = tmp;
        }
        return arr;
    }

    public static int[] generateReverseSorted(int size) {
        int[] arr = generateSorted(size);
        for (int i = 0; i < size / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[size - 1 - i];
            arr[size - 1 - i] = temp;
        }
        return arr;
    }

    private static int[] generateSorted(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (i + 1) * (AppConstants.MAX_VALUE / size);
        }
        return arr;
    }
}
