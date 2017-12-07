package com.codex.shootingstars;

import android.graphics.Bitmap;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Transform2D;

public class BackgroundObject {
    private Bitmap bitmap;
    private Transform2D transform;

    BackgroundObject(Bitmap bitmap, Transform2D transform) {
        this.bitmap = bitmap;
        this.transform = transform;
    }

    Bitmap getBitmap() { return bitmap; }
    void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    Transform2D getTransform() { return transform; }
    void setTransform(Transform2D transform) { this.transform = transform; }

    int getWidth() { return bitmap.getWidth(); }
    int getHeight() { return bitmap.getHeight(); }
}
