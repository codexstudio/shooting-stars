package com.filip.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import android.content.res.AssetManager;
import android.graphics.*;
import android.graphics.Paint.Style;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import com.filip.androidgames.framework.Font;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.types.Vector2;

import java.lang.Math;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer; // represents our artificial framebuffer
    Canvas canvas;        // use to draw to the artificial framebuffer
    Paint paint;        // needed for drawing
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();
    List<Animation> animationList;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.animationList = new ArrayList<>();
    }

    @Override
    public void drawAnimations(float deltaTime) {
        for (Animation a : animationList) {
            srcRect.left = a.srcWidth * ((a.counter - 1) % a.columns);
            srcRect.right = srcRect.left + a.srcWidth;
            srcRect.top = a.srcHeight * ((int) Math.ceil((double) a.counter / (double) a.columns) - 1);
            srcRect.bottom = srcRect.top + a.srcHeight;

            dstRect.left = a.x;
            dstRect.top = a.y;
            dstRect.right = a.x + a.srcWidth - 1;
            dstRect.bottom = a.y + a.srcHeight - 1;

            if (a.counter > a.columns * a.rows - 1) {
                if (a.bIsLooping) {
                    if (a.maxLoops != 0) {
                        if (a.loopCounter >= a.maxLoops) {
                            animationList.remove(a);
                        }
                    }
                    a.counter = 1;
                    a.loopCounter++;
                }
            } else {
                a.counter++;
            }

            canvas.drawBitmap(((AndroidPixmap) a.pixmap).bitmap, srcRect, dstRect, null);
        }
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public Font newFont(String fileName, float size) {
        return new AndroidFont(size, Typeface.createFromAsset(assets, fileName), Color.WHITE);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + dstWidth - 1;
        dstRect.bottom = y + dstHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, Point dst, Point dstSize) {
        drawPixmap(pixmap, dst.x, dst.y, 0, 0, pixmap.getWidth(), pixmap.getHeight(), dstSize.x, dstSize.y);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, Point dst) {
        drawPixmap(pixmap, dst.x, dst.y);
    }

    @Override
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight) {
        animationList.add(new Animation(pixmap, x, y, rows, columns, srcWidth, srcHeight));
    }

    @Override
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, boolean bIsLooping) {
        animationList.add(new Animation(pixmap, x, y, rows, columns, srcWidth, srcHeight, bIsLooping));
    }

    @Override
    public void addAnimation(Pixmap pixmap, int x, int y, int rows, int columns, int srcWidth, int srcHeight, int maxLoops) {
        animationList.add(new Animation(pixmap, x, y, rows, columns, srcWidth, srcHeight, maxLoops));
    }

    @Override
    public void drawText(String str, int x, int y, Font font, int color) {
        paint.setTextSize(font.getSize());
        paint.setTypeface(((AndroidFont) font).typeface);
        paint.setColor(color);
        canvas.drawText(str, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}