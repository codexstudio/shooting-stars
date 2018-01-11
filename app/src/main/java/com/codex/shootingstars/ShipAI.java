package com.codex.shootingstars;

import com.filip.androidgames.framework.types.Vector2;

import java.util.Random;

// AI class that makes ships wander in circles
public class ShipAI {
    private static final int WANDER_RADIUS = 300;

    private Vector2 findNextWanderWaypoint(Vector2 currWorldLocation) {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI * 2;
        double radius = Math.sqrt(random.nextDouble()) * WANDER_RADIUS ;
        float x = (float) (Math.cos(angle) * radius) + currWorldLocation.getX();
        float y = (float) (Math.sin(angle) * radius) + currWorldLocation.getY();

        return new Vector2(x, y);
    }

}
