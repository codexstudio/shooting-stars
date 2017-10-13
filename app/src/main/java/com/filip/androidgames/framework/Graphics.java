package com.filip.androidgames.framework;

import android.graphics.Typeface;

public interface Graphics
{
    public static enum PixmapFormat
    {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);
    public Font newFont(String fileName, float size);
    public void clear(int color);
    public void drawPixel(int x, int y, int color);
    public void drawLine(int x, int y, int x2, int y2, int color);
    public void drawRect(int x, int y, int width, int height, int color);
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    public void drawPixmap(Pixmap pixmap, int x, int y);
    public void drawAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight);
    public void drawText(String str, int x, int y, Font font);
    public int getWidth();
    public int getHeight();
}

