package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;

public class EnemyShip extends BaseCharacter {

    //Members

    //Default Constructor
    public EnemyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888));
    }

    //Constructor
    public EnemyShip(Pixmap pixmap, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    //Methods

}
