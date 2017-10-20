package com.filip.androidgames.framework.types;

public class Vector2 {
    private float x;
    private float y;

    public static final Vector2 ZERO_VECTOR = new Vector2();
    public static final Vector2 UP_VECTOR = new Vector2(0.0f, 1.0f);
    public static final Vector2 RIGHT_VECTOR = new Vector2(1.0f, 0.0f);

    public Vector2() {}

    public Vector2(float v) {
        this.x = this.y = v;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public Vector2 unitVector() {
        final float sqrSum = x * x + y * y;
        final float scale = (float) (1 / Math.sqrt(sqrSum));
        return new Vector2(x * scale, y * scale);
    }

    public void add(final Vector2 rhs) {
        x += rhs.getX();
        y += rhs.getY();
    }

    public void subtract(final Vector2 rhs) {
        x -= rhs.getX();
        y -= rhs.getY();
    }

    public void scale(final float scale) {
        x *= scale;
        y *= scale;
    }

    public boolean equal(final Vector2 rhs) {
        return x == rhs.getX() && y == rhs.getY();
    }

    public static final float DotProduct(final Vector2 lhs, final Vector2 rhs) {
        return lhs.getX() * rhs.getX() + lhs.getY() * rhs.getY();
    }

    public static final float CrossProduct(final Vector2 lhs, final Vector2 rhs) {
        return lhs.getX() * rhs.getY() - lhs.getY() * rhs.getX();
    }

    public static final float Distance(final Vector2 lhs, final Vector2 rhs) {
        return (float) (Math.sqrt(rhs.getX() - lhs.getX()) + Math.sqrt(rhs.getY() - lhs.getY()));
    }
}
