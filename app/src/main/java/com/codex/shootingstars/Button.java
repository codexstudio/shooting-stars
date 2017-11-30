package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;

public class Button extends BaseUIObject {
    //Default Constructor
    protected Button()
        {
            super();
        }

    //Constructor
    protected Button(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap, ScreenType type) {
        super(xLocation, yLocation, xScale, yScale, pixmap, type);
    }

    
}
