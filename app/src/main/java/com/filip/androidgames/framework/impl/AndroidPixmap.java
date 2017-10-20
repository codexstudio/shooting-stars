package com.filip.androidgames.framework.impl;

import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.util.Log;
import com.filip.androidgames.framework.Graphics.PixmapFormat;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

// @todo: deprecate originalBitmap!!!

public class AndroidPixmap implements Pixmap {
    private Bitmap originalBitmap;
    Bitmap bitmap;
    PixmapFormat format;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.originalBitmap = bitmap;
        this.bitmap = bitmap;
        this.format = format;
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

    @Override
    public void setRotation(Vector2 rotation){
        Matrix matrix = new Matrix();

        matrix.postRotate((float) Math.toDegrees(Math.atan2(rotation.getX(), rotation.getY())));

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth(), originalBitmap.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        bitmap = rotatedBitmap;
    }
}


