package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Transform2D;
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
    //private void setBoundingRadius() { boundingRadius = (actorSpriteSheet.getHeight() > actorSpriteSheet.getWidth()) ? ((float) actorSpriteSheet.getHeight() / 2) : ((float) actorSpriteSheet.getWidth() / 2); }
    private void setBoundingRadius() { boundingRadius = (actorSpriteSheet.getHeight() * this.transform.getScale().getY() + actorSpriteSheet.getWidth() * this.transform.getScale().getX()) / 4; }
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

    protected boolean isCollidingWith(DrawableObject object) {
        //Log.i("This", "" + this.transform.getLocation().getX() + " " + this.transform.getLocation().getY());
        //Log.i("Object", "" + object.transform.getLocation().getX() + " " + object.transform.getLocation().getY());
        float distance = distance(this.transform.getLocation(), object.transform.getLocation());
        //Log.i("Distance", "" + distance);
        //Log.i("This Rad", "" + this.getBoundingRadius());
        //Log.i("Object Rad", "" + object.getBoundingRadius());
        if (distance < this.getBoundingRadius() + object.getBoundingRadius()) {
            return true;
        }
        return false;
    }

    private float distance(Vector2 v1, Vector2 v2) {
        return Math.abs( (float) Math.sqrt( (Math.pow( (v2.getX() - v1.getX()) , 2)) + (Math.pow( (v2.getY() - v1.getY()) , 2)) ) );
    }

}
