package simdata;

import java.util.Random;

public abstract class GetRandomValue {

    public static int GetRandomValue() {
        Random rand = new Random();
        return rand.nextInt(100);
    }

    public static int GetRandomValue(Integer bounder) {
        Random rand = new Random();
        return rand.nextInt(bounder);
    }

    public static int GetRandomValue(Integer min, int max) {
        if (min > max) {
            Integer difference = min - max;
            Random rand = new Random();
            return rand.nextInt(difference + 1) + max;
        } else if (min == max) {
            return min;
        } else {
            Integer difference = max - min;
            Random rand = new Random();
            return rand.nextInt(difference + 1) + min;
        }
    }
}
