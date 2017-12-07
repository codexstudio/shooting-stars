package com.codex.shootingstars;

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

    protected void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void setToPoolTransform() {
        this.transform.setLocation(new Vector2(-500.0f, -500.0f));
        this.transform.setRotation(new Vector2(0.0f, 0.0f));
    }

}
