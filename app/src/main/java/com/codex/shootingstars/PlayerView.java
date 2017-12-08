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

    Vector2 getLocation() { return location; }
    void setLocation(Vector2 value) { location = value; }

    float distanceFromObject(GameObject obj) {
        return Vector2.distance(obj.transform.getLocation(), location);
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

    boolean isWithinView(BackgroundObject obj) {
        final float objXPos = obj.getTransform().getLocation().getX();
        final float objYPos = obj.getTransform().getLocation().getY();

        final boolean rightEdgeInView = objXPos + obj.getWidth() / 2 > location.getX() - width / 2 - OFF_SCREEN_EXTENSION;
        final boolean leftEdgeInView = objXPos - obj.getWidth() / 2 < location.getX() + width / 2 + OFF_SCREEN_EXTENSION;
        final boolean topEdgeInView = objYPos - obj.getHeight() / 2 < location.getY() + height / 2 + OFF_SCREEN_EXTENSION;
        final boolean bottomEdgeInView = objYPos + obj.getHeight() / 2 > location.getY() - height / 2 - OFF_SCREEN_EXTENSION;

        if (rightEdgeInView && leftEdgeInView && topEdgeInView && bottomEdgeInView) {
            return true;
        }
        return false;
    }

    boolean isWithinView(Vector2 point) {
        if (point.getX() > location.getX() - width / 2 &&
            point.getX() < location.getX() + width / 2 &&
            point.getY() < location.getY() + height / 2 &&
            point.getY() > location.getY() - height / 2)
        {
            return true;
        }
        return false;
    }

    Vector2 getScreenLocation(Vector2 obj) {
        return new Vector2(
                obj.getX() - location.getX() + width / 2,
                obj.getY() - location.getY() + height / 2);
    }
}
