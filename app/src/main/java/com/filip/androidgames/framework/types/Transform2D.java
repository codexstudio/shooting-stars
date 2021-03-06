package com.filip.androidgames.framework.types;

public class Transform2D {
    private Vector2 location;
    private Vector2 rotation;
    private Vector2 scale;

    public Transform2D() {
        location = new Vector2();
        rotation = new Vector2();
        scale = new Vector2();
    }

    public Transform2D(Vector2 location, Vector2 rotation, Vector2 scale) {
        this.location = location;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector2 getLocation() { return location; }
    public Vector2 getRotation() { return rotation; }
    public Vector2 getScale() { return scale; }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setRotation(Vector2 rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
}
