package com.codex.shootingstars;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

public class EnemyShip extends BaseCharacter {

    //Members
    Vector2 waypoint;

    //Default Constructor
    public EnemyShip(Graphics g){
        setActorSpriteSheet(g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888));
    }

    //Constructor
    public EnemyShip(Pixmap pixmap, float xLocation, float yLocation, float xScale, float yScale) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
    }

    //Methods

    protected void update(float deltaTime) {
        super.update(deltaTime);
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
