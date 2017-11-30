package com.codex.shootingstars;

import android.graphics.Rect;
import android.util.Log;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public abstract class BaseCharacter extends DrawableObject {

// @todo: AI behaviour to be defined in this class

    //Members

    //Default Constructor
    protected BaseCharacter() {
        super();
    }

    //Constructor
    protected BaseCharacter(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    //Setter & Getters

    //Methods

    protected void update() {
        super.update();
    }

}
