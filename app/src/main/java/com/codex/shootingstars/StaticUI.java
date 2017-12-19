package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;

public class StaticUI extends BaseUIObject{

    //Default Constructor
    protected StaticUI()
    {
        super();
    }

    //Constructor
    protected StaticUI(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    protected void update(float deltaTime) {
        super.update(deltaTime);
    }
}
