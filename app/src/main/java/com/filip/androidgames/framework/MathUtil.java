package com.filip.androidgames.framework;

import com.filip.androidgames.framework.types.Vector2;

import java.util.Random;

public class MathUtil {

    private static Random random = new Random();

    public static Vector2 randomPointInCircle(float radius) {
        double t = 2 * Math.PI * random.nextDouble();
        double r = Math.sqrt(random.nextDouble()) * radius;
        return new Vector2((float) (Math.cos(t) * r), (float) (Math.sin(t) * r));
    }

    public static Vector2 randomVectorWithMagnitude(float magnitude) {
        double t = 2 * Math.PI * random.nextDouble();
        return new Vector2((float) (Math.cos(t) * magnitude), (float) (Math.sin(t) * magnitude));
    }

}
