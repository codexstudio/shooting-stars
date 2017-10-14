package com.filip.androidgames.framework.impl;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class VirtualController implements OnTouchListener {
   public int initialX;
   public int initialY;
   public Double angle;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            event.getX(initialX);
            event.getY(initialY);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Double angle = Math.toDegrees(Math.atan2 (event.getX() - (initialX), event.getY() - (initialY)));
            Log.d("angle", Double.toString(angle));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }

        return true;


    }
}