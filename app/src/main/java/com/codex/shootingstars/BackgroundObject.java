package com.codex.shootingstars;

import android.graphics.Bitmap;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;

public class BackgroundObject {
    private Bitmap bitmap;
    private Vector2 location;

    BackgroundObject(Bitmap bitmap, Vector2 location) {
        bitmap.setHasAlpha(true);
        this.bitmap = bitmap;
        this.location = location;
    }

    Bitmap getBitmap() { return bitmap; }
    void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    Vector2 getLocation() { return location; }
    void setLocation(Vector2 location) { this.location = location; }

    int getWidth() { return bitmap.getWidth(); }
    int getHeight() { return bitmap.getHeight(); }
}
