package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public abstract class DrawableObject extends GameObject {

    //Members
    private Pixmap actorSpriteSheet;
    private float boundingRadius;

    //Default Contructor
    protected DrawableObject() {
        this.transform.setLocation(new Vector2(0.0f,0.0f));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(0.0f,0.0f));
        boundingRadius = 0.0f;
    }

    //Constructor
    protected DrawableObject(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        this.transform.setLocation(new Vector2(xLocation,yLocation));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(xScale,yScale));
        actorSpriteSheet = pixmap;
        setBoundingRadius();
    }

    //Setter & Getters
    protected Pixmap getActorSpriteSheet() { return actorSpriteSheet; }
    protected void setActorSpriteSheet(Pixmap sprite) {actorSpriteSheet = sprite;}

    protected float getBoundingRadius() { return boundingRadius; }
    private void setBoundingRadius() { boundingRadius = (actorSpriteSheet.getHeight() > actorSpriteSheet.getWidth()) ? ((float) actorSpriteSheet.getHeight()) : ((float) actorSpriteSheet.getWidth()); }
    protected void setBoundingRadius(float value) { boundingRadius = value; }

    //Methods
    protected void update() {
        super.update();
        //Update sprite transform
        actorSpriteSheet.setPixmapTransform(transform);
    }

    protected void draw(Graphics g){
        update();
        g.drawPixmap(actorSpriteSheet);
    }

}
