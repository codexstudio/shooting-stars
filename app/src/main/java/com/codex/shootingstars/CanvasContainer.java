package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

import java.util.ArrayList;
import java.util.List;

public class CanvasContainer<T extends DrawableObject> {

    //Members
    public List<T> containerList;

    //Constructor
    public CanvasContainer() {
        containerList = new ArrayList<T>();
    }

    public void add(T obj) {
        containerList.add(obj);
    }

    public void add(T... objects) {
        for (T obj : objects) {
            add(obj);
        }
    }

    public void remove(T obj) {
        containerList.remove(obj);
    }

    public void update(float deltaTime) {
        for (T obj : containerList) {
            obj.update(deltaTime);
        }
    }

    public void draw(Graphics g) {
        for (T obj : containerList) {
            obj.draw(g);
        }
    }

    public int getSize() { return containerList.size(); }
}
