package com.filip.androidgames.framework;

import android.graphics.Matrix;

public interface Graphics
{
    enum PixmapFormat
    {
        ARGB8888, ARGB4444, RGB565
    }

    class Animation {
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

    class Point {
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

    Pixmap newPixmap(String fileName, PixmapFormat format);
    Font newFont(String fileName, float size);
    void clear(int color);
    void drawPixel(int x, int y, int color);
    void drawLine(int x, int y, int x2, int y2, int color);
    void drawRect(int x, int y, int width, int height, int color);
    void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, int dstWidth, int dstHeight);
    void drawPixmap(Pixmap pixmap, Point dst, Point dstSize);
    void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    void drawPixmap(Pixmap pixmap, int x, int y);
    void drawPixmap(Pixmap pixmap, Point dst);
    void drawText(String str, int x, int y, Font font, int color);
    void drawAnimations(float deltaTime);
    void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight);
    void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, boolean bIsLooping);
    void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, int maxLoops);
    int getWidth();
    int getHeight();

    void drawPixmap(Pixmap pixmap);
}

