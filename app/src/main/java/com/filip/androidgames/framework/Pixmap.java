package com.filip.androidgames.framework;

import com.filip.androidgames.framework.Graphics.PixmapFormat;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;

public interface Pixmap 
{
    public int getWidth();
    public int getHeight();
    public PixmapFormat getFormat();
    public void dispose();
    public void setRotation(Vector2 rotation);
    public void postTranslate(Vector2 translation);
    public void postScale(Vector2 scale);
    public void setTransform(Transform2D transform);
}
