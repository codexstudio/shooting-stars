package com.filip.androidgames.framework;

import android.graphics.Typeface;
import com.filip.androidgames.framework.types.Vector2;

public interface Graphics
{
    public static enum PixmapFormat
    {
        ARGB8888, ARGB4444, RGB565
    }

    public static class Animation {
        public Pixmap pixmap;
        public int x;
        public int y;
        public int rows;
        public int columns;
        public int srcWidth;
        public int srcHeight;
        public boolean bIsLooping = false;
        public int maxLoops = 0;
        public int counter = 0;
        public int loopCounter = 0;

        public Animation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight) {
            this.pixmap = pixmap;
            this.x = x;
            this.y = y;
            this.rows = rows;
            this.columns = columns;
            this.srcWidth = srcWidth;
            this.srcHeight = srcHeight;
        }

        public Animation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, boolean bIsLooping) {
            this(pixmap, x, y, rows, columns, srcWidth, srcHeight);
            this.maxLoops = 0;
            this.bIsLooping = bIsLooping;
        }

        public Animation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, int maxLoops) {
            this(pixmap, x, y, rows, columns, srcWidth, srcHeight);
            this.maxLoops = maxLoops;
            this.bIsLooping = true;
        }
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(int p) {
            this.x = this.y = p;
        }

        public Point() {
            x = y = 0;
        }
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);
    public Font newFont(String fileName, float size);
    public void clear(int color);
    public void drawPixel(int x, int y, int color);
    public void drawLine(int x, int y, int x2, int y2, int color);
    public void drawRect(int x, int y, int width, int height, int color);
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, int dstWidth, int dstHeight);
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    public void drawPixmap(Pixmap pixmap, int x, int y);
    public void drawText(String str, int x, int y, Font font, int color);
    public void drawAnimations(float deltaTime);
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight);
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, boolean bIsLooping);
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, int maxLoops);
    public int getWidth();
    public int getHeight();
}

