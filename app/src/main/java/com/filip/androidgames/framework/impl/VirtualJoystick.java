package com.filip.androidgames.framework.impl;

import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.types.Vector2;

public class VirtualJoystick {
    private static final float MAX_JOYSTICK_DISTANCE = 100.0f;

    private float initialX;
    private float initialY;
    private Vector2 direction;

    public VirtualJoystick() {
        direction = new Vector2();
    }

    public boolean isActiveAndSetDirection(TouchEvent touchEvent) {
        switch (touchEvent.type) {
            case TouchEvent.TOUCH_DOWN:
                initialX = touchEvent.x;
                initialY = touchEvent.y;
            case TouchEvent.TOUCH_DRAGGED:
                direction.setX(touchEvent.x - initialX);
                direction.setY(initialY - touchEvent.y);
                break;
            default:
                direction.setX(0);
                direction.setY(0);
                return false;
        }
        return true;
    }

    // returns direction clamped to MAX_JOYSTICK_DISTANCE
    public Vector2 getDirection() { return direction.getClampedToSize(MAX_JOYSTICK_DISTANCE); }
    public Vector2 getRawDirection() { return direction; }
}
