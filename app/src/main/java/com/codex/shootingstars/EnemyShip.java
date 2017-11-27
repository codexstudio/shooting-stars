package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class EnemyShip extends BaseCharacter {

    //Members

    //Default Constructor
    public EnemyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888));
        update();
    }

    //Constructor
    public EnemyShip(Graphics g, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888));
        update();
    }

    //Methods
}
