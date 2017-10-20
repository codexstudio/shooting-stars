package com.filip.androidgames.framework.impl;

import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.types.Vector2;

public class VirtualJoystick {
    private float initialX;
    private float initialY;
    private Vector2 direction;

    public boolean isActive(TouchEvent touchEvent) {
        switch (touchEvent.type) {
            case TouchEvent.TOUCH_DOWN:
                initialX = touchEvent.x;
                initialY = touchEvent.y;
            case TouchEvent.TOUCH_DRAGGED:
                direction = new Vector2(touchEvent.x - initialX, initialY - touchEvent.y);
                break;
            default:
                return false;
        }
        return true;
    }

    public Vector2 getDirection() { return direction; }
}
