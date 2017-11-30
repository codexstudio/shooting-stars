package com.filip.androidgames.framework.types;

public class Vector2 {
    private float x;
    private float y;

    public static final Vector2 ZERO_VECTOR = new Vector2();
    public static final Vector2 UP_VECTOR = new Vector2(0.0f, 1.0f);
    public static final Vector2 RIGHT_VECTOR = new Vector2(1.0f, 0.0f);

    public Vector2() {
        x = y = 0;
    }

    public Vector2(float v) {
        this.x = this.y = v;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 unitVector() {
        final float sqrSum = x*x + y*y;
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

    public Vector2 getScaledVector(float scale) {
        Vector2 temp = unitVector();
        return new Vector2(temp.getX() * scale, temp.getY() * scale);
    }

    public boolean equals(final Vector2 rhs) {
        return x == rhs.getX() && y == rhs.getY();
    }

    public void normalize() {
        final float sqrSum = x*x + y*y;
        scale((float) (1 / Math.sqrt(sqrSum)));
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 getClampedToSize(float MaxSize) {
        if (magnitude() > MaxSize) {
            Vector2 temp = unitVector();
            temp.scale(MaxSize);
            return temp;
        } else {
            return this;
        }
    }

    public static final float DotProduct(final Vector2 lhs, final Vector2 rhs) {
        return lhs.getX() * rhs.getX() + lhs.getY() * rhs.getY();
    }

    public static final float CrossProduct(final Vector2 lhs, final Vector2 rhs) {
        return lhs.getX() * rhs.getY() - lhs.getY() * rhs.getX();
    }

    public static final float Distance(final Vector2 lhs, final Vector2 rhs) {
        return (float) Math.abs(Math.sqrt(Math.pow(rhs.getX() - lhs.getX(), 2) + Math.pow(rhs.getY() - lhs.getY(), 2)));
    }

    public static final float Projection(final Vector2 a, final Vector2 b){
        return DotProduct(a, b.unitVector());
    }
}
