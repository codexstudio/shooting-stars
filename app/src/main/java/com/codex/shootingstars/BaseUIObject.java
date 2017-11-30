package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;

public abstract class BaseUIObject extends DrawableObject {

    //Members

    //Default Constructor
    public BaseUIObject() {
        super();
    }

    //Constructor
    public BaseUIObject(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }
    //Setter & Getters

    //Methods
    protected void update()
    {
        super.update();
    }
}
