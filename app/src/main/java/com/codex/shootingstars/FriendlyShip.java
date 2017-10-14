package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class FriendlyShip extends BaseCharacter{

    public static enum ControllerStates {
        AI_CONTROLLED,
        PLAYER_CONTROLLED
    }

    //Constructor
    public FriendlyShip(Graphics g, ControllerStates state){
        setActorSpriteSheet(g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        controllerState = state;
    }

    //Members
    private ControllerStates controllerState;

    //Setter & Getters
    public ControllerStates getState() {return controllerState; }

    //Methods
    public void changeControllerState (ControllerStates state) {
        controllerState = state;
    }

}
