package com.codex.shootingstars;

import com.filip.androidgames.framework.types.*;

public abstract class GameObject {
    //Members
    protected Transform2D transform;
    protected Vector2 worldLocation;

    //Constructor
    protected GameObject() {
        transform = new Transform2D();
        worldLocation = new Vector2();
    }

    //Methods
    protected abstract void update(float deltaTime);

    public void setTransform (Vector2 location, Vector2 rotation, Vector2 scale) {
        transform.setLocation(location);
        transform.setRotation(rotation);
        transform.setScale(scale);
    }

    public void setTransform(Transform2D transform2D) {
        transform = transform2D;
    }

    public void setWorldLocation(Vector2 location) {
        worldLocation = location;
    }

    public Vector2 getWorldLocation() { return worldLocation; };
}
