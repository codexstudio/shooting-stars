package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class Asteroid extends BaseCharacter {

    //Members

    //Default Constructor
    public Asteroid(Graphics g){
        setActorSpriteSheet(g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888));
        update();
    }

    //Constructor
    public Asteroid(Graphics g, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale);
        setActorSpriteSheet(g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888));
        update();
    }

    //Methods

}
