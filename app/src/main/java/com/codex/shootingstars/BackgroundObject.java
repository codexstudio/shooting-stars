package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Transform2D;

public class BackgroundObject {
    private Pixmap pixmap;
    private Transform2D transform;

    BackgroundObject(Pixmap pixmap, Transform2D transform) {
        this.pixmap = pixmap;
        this.transform = transform;
    }

    Pixmap getPixmap() { return pixmap; }
    void setPixmap(Pixmap pixmap) { this.pixmap = pixmap; }

    Transform2D getTransform() { return transform; }
    void setTransform(Transform2D transform) { this.transform = transform; }
}
