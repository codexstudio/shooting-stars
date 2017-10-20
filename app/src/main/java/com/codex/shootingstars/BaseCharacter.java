package com.codex.shootingstars;

import android.graphics.Rect;
import android.util.Log;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public abstract class BaseCharacter extends GameObject{

    //Members
    private Pixmap actorSpriteSheet;
    private Rect boundingRect;

    //Default Constructor
    protected BaseCharacter() {
        this.transform.setLocation(new Vector2(0.0f,0.0f));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(0.0f,0.0f));
        boundingRect = new Rect();
    }

    //Constructor
    protected BaseCharacter(float xLocation, float yLocation, float xScale, float yScale) {
        this.transform.setLocation(new Vector2(xLocation,yLocation));
        this.transform.setRotation(new Vector2(0.0f,0.0f));
        this.transform.setScale(new Vector2(xScale,yScale));
        boundingRect = new Rect();
    }

    //Setter & Getters
    protected Pixmap getActorSpriteSheet() { return actorSpriteSheet; }
    protected void setActorSpriteSheet(Pixmap sprite) {actorSpriteSheet = sprite;}

    protected Rect getBoundingRect() { return boundingRect; }
    private void setBoundingRect(){
        if (actorSpriteSheet != null) {
            boundingRect.set(0, 0, actorSpriteSheet.getHeight(), actorSpriteSheet.getWidth());
        }
        else { Log.wtf("WARNING!", "ActorSpriteSheet Missing!"); }
    }

    //Methods
    protected void update() {
        //Update sprite rotation
        actorSpriteSheet.setRotation(transform.getRotation());
        //Update Bounding Rect
        setBoundingRect();
    }

    protected void draw(Graphics g){
        update();
        g.drawPixmap(   actorSpriteSheet,
                        new Graphics.Point((int)transform.getLocation().getX(), (int)transform.getLocation().getY()),
                        new Graphics.Point((int)(transform.getScale().getX() * actorSpriteSheet.getWidth()), (int)(transform.getScale().getY() * actorSpriteSheet.getHeight()))
        );
    }

    //AI Behaviour Here

}
