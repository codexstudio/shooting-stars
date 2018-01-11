package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

import java.util.Random;

public abstract class BaseCharacter extends DrawableObject {

// @todo: AI behaviour to be defined in this class

    //Members
    private static final int WANDER_RADIUS = 10;
    protected static final float AI_SPEED = 2.0f;

    //Default Constructor
    protected BaseCharacter() {
        super();
    }

    //Constructor
    protected BaseCharacter(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    //Setter & Getters


    //Methods

    protected void update(float deltaTime) {
        super.update(deltaTime);
    }

    protected Vector2 findNextWanderWaypoint() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI * 2;
        double radius = random.nextDouble() * WANDER_RADIUS ;
        float x = (float) (Math.cos(angle) * radius) + transform.getLocation().getX();
        float y = (float) (Math.sin(angle) * radius) + transform.getLocation().getY();

        return new Vector2(x, y);
    }

    protected void moveTowardsWaypoint(Vector2 waypoint) {
        Vector2 newPos = new Vector2();
        newPos.setX(getWorldLocation().getX() + waypoint.getScaledVector(AI_SPEED).getX());
        newPos.setY(getWorldLocation().getY() + waypoint.getScaledVector(AI_SPEED).getY());
        this.transform.setRotation(new Vector2(Vector2.projection(waypoint, Vector2.RIGHT_VECTOR), -Vector2.projection(waypoint, Vector2.UP_VECTOR)));
        setWorldLocation(newPos);
    }



}
