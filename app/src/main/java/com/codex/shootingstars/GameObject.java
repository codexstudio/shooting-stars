package com.codex.shootingstars;

public abstract class GameObject {

    protected class Vector3 {
        protected int x;
        protected int y;
        protected int z;
    }

    //Members
    protected Vector3 transform;

    //Constructor
    protected GameObject() { transform.x = 0; transform.y = 0; transform.z = 0; }



}
