package com.codex.shootingstars;

import com.filip.androidgames.framework.types.Vector2;

public class PlayerView {

    private final float OFF_SCREEN_EXTENSION = 5.0f;

    private Vector2 location;
    private int width;
    private int height;

    PlayerView(int width, int height) {
        location = new Vector2(0, 0);
        this.width = width;
        this.height = height;
    }

    float distanceFromObject(GameObject obj) {
        return Vector2.Distance(obj.transform.getLocation(), location);
    }

    boolean isWithinView(DrawableObject obj) {
        final float xPosRightEdge = obj.transform.getLocation().getX() + obj.getBoundingRadius();
        final float xPosLeftEdge = obj.transform.getLocation().getX() - obj.getBoundingRadius();
        final float yPosTopEdge = obj.transform.getLocation().getY() - obj.getBoundingRadius();
        final float yPosBottomEdge = obj.transform.getLocation().getY() + obj.getBoundingRadius();

        if (xPosRightEdge > location.getX() - width / 2 - OFF_SCREEN_EXTENSION &&
            xPosLeftEdge < location.getX() + width / 2 + OFF_SCREEN_EXTENSION &&
            yPosTopEdge < location.getY() + height / 2 + OFF_SCREEN_EXTENSION &&
            yPosBottomEdge > location.getY() - height / 2 - OFF_SCREEN_EXTENSION) {
            return true;
        }
        return false;
    }

}
