package com.filip.androidgames.framework;

import java.util.List;

public interface Input {
    class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;
        
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type == TOUCH_DOWN)
                builder.append("touch down, ");
            else if (type == TOUCH_DRAGGED)
                builder.append("touch dragged, ");
            else
                builder.append("touch up, ");
            builder.append(pointer);
            builder.append(",");
            builder.append(x);
            builder.append(",");
            builder.append(y);
            return builder.toString();
        }
    }

    class SwipeEvent {
        public static final int SWIPE_LEFT = 0;
        public static final int SWIPE_UP = 1;
        public static final int SWIPE_RIGHT = 2;
        public static final int SWIPE_DOWN = 3;

        public int direction;
        public double angle;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            switch (direction) {
                case SWIPE_LEFT:
                    builder.append("swipe left, ");
                    break;
            }
            return builder.toString();
        }
    }

    boolean isTouchDown(int pointer);
    int getTouchX(int pointer);
    int getTouchY(int pointer);
    float getAccelX();
    float getAccelY();
    float getAccelZ();
    List<TouchEvent> getTouchEvents();
    List<SwipeEvent> getSwipeEvents();
}



