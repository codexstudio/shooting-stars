package com.filip.androidgames.framework.impl;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.filip.androidgames.framework.Input.SwipeEvent;
import com.filip.androidgames.framework.Pool;
import com.filip.androidgames.framework.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class SwipeHandler extends GestureDetector.SimpleOnGestureListener {
    Pool<SwipeEvent> swipeEventPool;
    List<SwipeEvent> swipeEvents = new ArrayList<>();
    List<SwipeEvent> swipeEventsBuffer = new ArrayList<>();

    public SwipeHandler() {
        Pool.PoolObjectFactory<SwipeEvent> factory = new PoolObjectFactory<SwipeEvent>() {
            @Override
            public SwipeEvent createObject() {
                return new SwipeEvent();
            }
        };
        swipeEventPool = new Pool<>(factory, 100);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (this) {
            SwipeEvent swipeEvent;

            Double angle = Math.toDegrees(Math.atan2(e1.getY() - e2.getY(), e2.getX() - e1.getX()));
            swipeEvent = swipeEventPool.newObject();
            if (angle > 45 && angle <= 135) {
                swipeEvent.direction = SwipeEvent.SWIPE_UP;
            } else if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                swipeEvent.direction = SwipeEvent.SWIPE_LEFT;
            } else if (angle < -45 && angle >= -135) {
                swipeEvent.direction = SwipeEvent.SWIPE_DOWN;
            } else if (angle > -45 && angle <= 45) {
                swipeEvent.direction = SwipeEvent.SWIPE_RIGHT;
            }
            swipeEvent.angle = angle;
            swipeEventsBuffer.add(swipeEvent);
        }
        return true;
    }

    public List<SwipeEvent> getSwipeEvents() {
        synchronized (this) {
            int len = swipeEvents.size();
            for (int i = 0; i < len; i++)
                swipeEventPool.free(swipeEvents.get(i));
            swipeEvents.clear();
            swipeEvents.addAll(swipeEventsBuffer);
            swipeEventsBuffer.clear();
            return swipeEvents;
        }
    }
}
