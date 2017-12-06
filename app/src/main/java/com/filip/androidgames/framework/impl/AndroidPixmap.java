package com.filip.androidgames.framework.impl;

import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.util.Log;
import com.filip.androidgames.framework.Graphics.PixmapFormat;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;

public class AndroidPixmap implements Pixmap {
    private Matrix matrix;
    Bitmap bitmap;
    PixmapFormat format;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
        this.matrix = new Matrix();
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }

    @Deprecated
    @Override
    public void setRotation(Vector2 rotation) {
        final float degrees = (float) Math.toDegrees(Math.atan2(rotation.getX(), rotation.getY()));

        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }

    @Override
    public void postTranslate(Vector2 translation) {
        matrix.postTranslate(translation.getX(), translation.getY());
    }

    @Override
    public void postScale(Vector2 scale) {
        matrix.postScale(scale.getX(), scale.getY());
    }

    @Override
    public void setPixmapTransform(Transform2D transform) {
        final Vector2 rotation = transform.getRotation();
        final Vector2 scale = transform.getScale();
        final Vector2 location = transform.getLocation();

        matrix.setScale(scale.getX(), scale.getY(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        final float degrees = (float) Math.toDegrees(Math.atan2(rotation.getX(), rotation.getY()));
        matrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        matrix.postTranslate(location.getX() - bitmap.getWidth() / 2, location.getY() - bitmap.getHeight() / 2);
    }

    public Matrix getMatrix() { return matrix; }
}
