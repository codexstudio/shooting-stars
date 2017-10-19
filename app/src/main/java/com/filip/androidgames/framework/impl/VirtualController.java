package com.filip.androidgames.framework.impl;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class VirtualController implements OnTouchListener {
   public double initialX;
   public double initialY;
   public Double angle;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialX = event.getX();
            initialY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            angle = Math.toDegrees(Math.atan2(event.getY() - initialY, event.getX() - initialX));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return true;
    }
}
