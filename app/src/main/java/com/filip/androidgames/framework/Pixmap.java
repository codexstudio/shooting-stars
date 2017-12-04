package com.filip.androidgames.framework;

import com.filip.androidgames.framework.Graphics.PixmapFormat;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;

public interface Pixmap 
{
    int getWidth();
    int getHeight();
    PixmapFormat getFormat();
    void dispose();
    void postTranslate(Vector2 translation);
    void postScale(Vector2 scale);
    void setPixmapTransform(Transform2D transform);

    @Deprecated
    void setRotation(Vector2 rotation);
}
