package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class EnemyShip extends BaseCharacter {

    //Constructor
    public EnemyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("", Graphics.PixmapFormat.ARGB8888));
    }
}
