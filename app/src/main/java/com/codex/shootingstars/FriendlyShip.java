package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public class FriendlyShip extends BaseCharacter{

    public enum ControllerState {
        AI_CONTROLLED,
        PLAYER_CONTROLLED
    }

    //Members
    private ControllerState controllerState;
    Vector2 offset;
    Vector2 waypoint;

    //Default Constructor
    public FriendlyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap(Settings.PlayerShip, Graphics.PixmapFormat.ARGB8888));
        controllerState = ControllerState.AI_CONTROLLED;
    }

    //Constructor
    public FriendlyShip(Pixmap pixmap, ControllerState state, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
        controllerState = state;
    }

    //Setter & Getters
    public ControllerState getState() {return controllerState; }

    //Methods
    public void changeControllerState (ControllerState state) {
        controllerState = state;
    }

    protected void update(float deltaTime) {
        super.update(deltaTime);
        if (this.controllerState == ControllerState.AI_CONTROLLED) {
            if (waypoint == null) {
                waypoint = findNextWanderWaypoint();
            }
            else if (Vector2.distance(waypoint, getWorldLocation()) <= AI_SPEED) {
                waypoint = findNextWanderWaypoint();
            }
            else {
                moveTowardsWaypoint(waypoint);
            }
        }
    }

}
