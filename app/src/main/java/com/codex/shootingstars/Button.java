package com.codex.shootingstars;


import com.filip.androidgames.framework.Input;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public class Button extends BaseUIObject {
    //Default Constructor
    protected Button()
        {
            super();
        }

    //Constructor
    protected Button(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    protected boolean onTouchRect(Input.TouchEvent event) {
        if(event.type == Input.TouchEvent.TOUCH_UP){

            if (getBoundingRect().contains(event.x, event.y)){
            return true;}
        }
        return false;
    }

    protected boolean onTouchCircle(Input.TouchEvent event) {
        if(event.type == Input.TouchEvent.TOUCH_UP){

            if (Vector2.distance(new Vector2(event.x, event.y), transform.getLocation()) < getBoundingRadius()){
                return true;}
        }
        return false;
    }
}
