package com.codex.shootingstars;

import com.filip.androidgames.framework.types.Vector2;

class PlayerView {

    private final float OFF_SCREEN_EXTENSION = 5.0f;

    private Vector2 location;
    final int width;
    final int height;

    PlayerView(int width, int height) {
        location = new Vector2(width / 2, height / 2);
        this.width = width;
        this.height = height;
    }

    Vector2 getLocation() { return location; }
    void setLocation(Vector2 value) { location = value; }

    float distanceFromObject(GameObject obj) {
        return Vector2.distance(obj.getWorldLocation(), location);
    }

    boolean isWithinView(DrawableObject obj) {
        final float xPosRightEdge = obj.getWorldLocation().getX() + obj.getBoundingRadius();
        final float xPosLeftEdge = obj.getWorldLocation().getX() - obj.getBoundingRadius();
        final float yPosTopEdge = obj.getWorldLocation().getY() - obj.getBoundingRadius();
        final float yPosBottomEdge = obj.getWorldLocation().getY() + obj.getBoundingRadius();

        return xPosRightEdge > location.getX() - width / 2 - OFF_SCREEN_EXTENSION &&
                xPosLeftEdge < location.getX() + width / 2 + OFF_SCREEN_EXTENSION &&
                yPosTopEdge < location.getY() + height / 2 + OFF_SCREEN_EXTENSION &&
                yPosBottomEdge > location.getY() - height / 2 - OFF_SCREEN_EXTENSION;
    }

    boolean isWithinView(BackgroundObject obj) {
        final float objXPos = obj.getLocation().getX();
        final float objYPos = obj.getLocation().getY();

        final boolean rightEdgeInView = objXPos + obj.getWidth() / 2 > location.getX() - width / 2 - OFF_SCREEN_EXTENSION;
        final boolean leftEdgeInView = objXPos - obj.getWidth() / 2 < location.getX() + width / 2 + OFF_SCREEN_EXTENSION;
        final boolean topEdgeInView = objYPos - obj.getHeight() / 2 < location.getY() + height / 2 + OFF_SCREEN_EXTENSION;
        final boolean bottomEdgeInView = objYPos + obj.getHeight() / 2 > location.getY() - height / 2 - OFF_SCREEN_EXTENSION;

        return rightEdgeInView && leftEdgeInView && topEdgeInView && bottomEdgeInView;
    }

    boolean isWithinView(Vector2 point) {
        return point.getX() > location.getX() - width / 2 &&
                point.getX() < location.getX() + width / 2 &&
                point.getY() < location.getY() + height / 2 &&
                point.getY() > location.getY() - height / 2;
    }

    Vector2 getScreenLocation(Vector2 obj) {
        return new Vector2(
                obj.getX() - location.getX() + width / 2,
                obj.getY() - location.getY() + height / 2);
    }
}
