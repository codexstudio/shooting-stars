package com.codex.shootingstars;

import com.filip.androidgames.framework.types.*;

public abstract class GameObject {

    //Members
    protected Transform2D transform;

    //Constructor
    protected GameObject() {
        transform = new Transform2D();
    }

    //Methods
    protected void update() {

    }
}
