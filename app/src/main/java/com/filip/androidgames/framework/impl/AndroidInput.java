package com.filip.androidgames.framework.impl;

import java.util.List;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.filip.androidgames.framework.Input;

public class AndroidInput implements Input, View.OnTouchListener {
    AccelerometerHandler accelHandler;
    TouchHandler touchHandler;
    GestureDetector gestureHandler;
    SwipeHandler swipeHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        touchHandler = new TouchHandler(scaleX, scaleY);
        swipeHandler = new SwipeHandler();
        gestureHandler = new GestureDetector(context, swipeHandler);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return touchHandler.onTouch(v, event) && gestureHandler.onTouchEvent(event);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    @Override
    public List<SwipeEvent> getSwipeEvents() { return swipeHandler.getSwipeEvents(); }
}


