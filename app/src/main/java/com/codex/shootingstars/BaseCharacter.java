package com.codex.shootingstars;

import android.graphics.Rect;
import android.util.Log;
import com.filip.androidgames.framework.Pixmap;

public abstract class BaseCharacter extends GameObject{

    //Members
    private Pixmap actorSpriteSheet;
    private Rect boundingRect;

    //Constructor
    protected BaseCharacter() {
        setBoundingRect();
    }

    //Setter & Getters
    public Pixmap getActorSpriteSheet() { return actorSpriteSheet; }
    protected void setActorSpriteSheet(Pixmap sprite) {actorSpriteSheet = sprite;}

    public Rect getBoundingRect() { return boundingRect; }
    private void setBoundingRect(){
        if (actorSpriteSheet != null) {
            boundingRect.set(0, 0, actorSpriteSheet.getHeight(), actorSpriteSheet.getWidth());
        }
        else { Log.i("WARNING!", "ActorSpriteSheet Missing!"); }
    }

    //Methods
        //AI Behaviour Here

}
