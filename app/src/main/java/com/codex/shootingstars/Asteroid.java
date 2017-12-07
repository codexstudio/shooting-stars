package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;

public class Asteroid extends BaseCharacter {

    //Members

    //Default Constructor
    public Asteroid(Graphics g){
        setActorSpriteSheet(g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888));
    }

    //Constructor
    public Asteroid(Pixmap pixmap, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    protected void update(float deltaTime) {
        super.update(deltaTime);
    }
    //Methods

}
