package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;

public class FriendlyShip extends BaseCharacter{

    public enum ControllerState {
        AI_CONTROLLED,
        PLAYER_CONTROLLED
    }

    //Members
    private ControllerState controllerState;

    //Default Constructor
    public FriendlyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        controllerState = ControllerState.AI_CONTROLLED;
        update();
    }

    //Constructor
    public FriendlyShip(Pixmap pixmap, ControllerState state, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
        controllerState = state;
        update();
    }

    //Setter & Getters
    public ControllerState getState() {return controllerState; }

    //Methods
    public void changeControllerState (ControllerState state) {
        controllerState = state;
    }

    protected void update() {
        super.update();
    }

    @Override
    public void setToPoolTransform() {
        super.setToPoolTransform();
        this.controllerState = ControllerState.AI_CONTROLLED;
    }
}
