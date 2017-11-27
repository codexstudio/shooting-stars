package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;

public class FriendlyShip extends BaseCharacter{

    public static enum ControllerStates {
        AI_CONTROLLED,
        PLAYER_CONTROLLED
    }

    //Members
    private ControllerStates controllerState;

    //Default Constructor
    public FriendlyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        controllerState = ControllerStates.AI_CONTROLLED;
        update();
    }

    //Constructor
    public FriendlyShip(Graphics g, ControllerStates state, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        controllerState = state;
        update();
    }

    //Setter & Getters
    public ControllerStates getState() {return controllerState; }

    //Methods
    public void changeControllerState (ControllerStates state) {
        controllerState = state;
    }

}
