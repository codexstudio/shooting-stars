package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class FriendlyShip extends BaseCharacter{

    public static enum ControllerStates {
        AI_CONTROLLED,
        PLAYER_CONTROLLED
    }

    //Members
    private ControllerStates controllerState;

    //Constructor
    public FriendlyShip(Graphics g, ControllerStates state){
        setActorSpriteSheet(g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        controllerState = state;
    }

    //Setter & Getters
    public ControllerStates getState() {return controllerState; }

    //Methods
    public void changeControllerState (ControllerStates state) {
        controllerState = state;
    }

}
