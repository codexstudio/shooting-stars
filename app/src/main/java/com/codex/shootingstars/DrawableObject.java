package com.codex.shootingstars;

import android.graphics.Rect;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public abstract class DrawableObject extends GameObject {

    //Members
    private Pixmap actorSpriteSheet;
    private float boundingRadius;
    private Rect boundingRect;
    private boolean isVisible;

    //Default Constructor
    protected DrawableObject() {
        this.transform.setLocation(new Vector2(0.0f,0.0f));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(0.0f,0.0f));
        boundingRadius = 0.0f;
        boundingRect = new Rect(0,0,0,0);
        isVisible = true;
    }

    //Constructor
    protected DrawableObject(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap) {
        this.transform.setLocation(new Vector2(xLocation,yLocation));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(xScale,yScale));
        actorSpriteSheet = pixmap;
        setBoundingRadius();
        setBoundingRect();
        isVisible = true;
    }

    //Setter & Getters
    protected Pixmap getActorSpriteSheet() { return actorSpriteSheet; }
    protected void setActorSpriteSheet(Pixmap sprite) {actorSpriteSheet = sprite;}

    public boolean isVisible() { return isVisible; }
    public void setVisibility(boolean value) { isVisible = value; }

    protected float getBoundingRadius() { return boundingRadius; }
    //private void setBoundingRadius() { boundingRadius = (actorSpriteSheet.getHeight() > actorSpriteSheet.getWidth()) ? ((float) actorSpriteSheet.getHeight() / 2) : ((float) actorSpriteSheet.getWidth() / 2); }
    private void setBoundingRadius() { boundingRadius = (actorSpriteSheet.getHeight() * this.transform.getScale().getY() + actorSpriteSheet.getWidth() * this.transform.getScale().getX()) / 4; }
    protected void setBoundingRadius(float value) { boundingRadius = value; }

    protected Rect getBoundingRect() {return boundingRect;}
    private void setBoundingRect() { boundingRect = new Rect((int)(this.transform.getLocation().getX() - actorSpriteSheet.getWidth()/2), (int)(this.transform.getLocation().getY() - actorSpriteSheet.getHeight()/2), (int)this.transform.getLocation().getX() + actorSpriteSheet.getWidth()/2, (int)this.transform.getLocation().getY() + actorSpriteSheet.getHeight()/2);}
    protected void setBoundingRect(int x, int y) { boundingRect = new Rect(x, y, actorSpriteSheet.getWidth()+x, actorSpriteSheet.getHeight()+y); }

    //Methods
    protected void update() {
        super.update();
        //Update sprite transform
        actorSpriteSheet.setPixmapTransform(transform);
    }

    protected void draw(Graphics g) {
        update();
        g.drawPixmap(actorSpriteSheet);
    }

    protected boolean isCollidingWith(DrawableObject object) {
        //Log.i("This", "" + this.transform.getLocation().getX() + " " + this.transform.getLocation().getY());
        //Log.i("Object", "" + object.transform.getLocation().getX() + " " + object.transform.getLocation().getY());
        float distance = Math.abs(Vector2.Distance(this.transform.getLocation(), object.transform.getLocation()));
        //Log.i("Distance", "" + distance);
        //Log.i("This Rad", "" + this.getBoundingRadius());
        //Log.i("Object Rad", "" + object.getBoundingRadius());
        return distance < this.getBoundingRadius() + object.getBoundingRadius();
    }
}
