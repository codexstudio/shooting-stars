package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

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
        rotateClockwise(0.1f);
    }

    //Methods

}
