package com.codex.shootingstars;

import com.filip.androidgames.framework.types.*;

public abstract class GameObject {

    //Members
    protected Transform transform;

    //Constructor
    protected GameObject() {
        transform = new Transform();
    }
}
