package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class Asteroid extends BaseCharacter {

    //Constructor
    public Asteroid(Graphics g){
        setActorSpriteSheet(g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888));
    }

}
