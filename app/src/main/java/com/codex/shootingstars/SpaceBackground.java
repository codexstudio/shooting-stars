package com.codex.shootingstars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.impl.AndroidPixmap;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;
import java.util.Random;

public class SpaceBackground {
    private Pixmap pixmap;
    private Random random = new Random();
    private List<DrawableObject> significantObjects;
    private List<Vector2> starList;
    private int width;
    private int height;

    SpaceBackground(Graphics g, int width, int height) {
        pixmap = generateBackground(g);
        this.width = width;
        this.height = height;
    }

    private Pixmap generateBackground(Graphics g) {
        for (int i = 0; i < 100; i++) {
            starList.add(randomPointInCircle(1));
        }

        for (int i = 0; i < 4; i++) {
            Vector2 loc = randomPointInCircle(1);
            significantObjects.add(new Asteroid(g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888), loc.getX(), loc.getY(), 1, 1));
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        return new AndroidPixmap(bitmap, Graphics.PixmapFormat.RGB565);
    }

    private Vector2 randomPointInCircle(float radius) {
        double t = 2 * Math.PI * random.nextDouble();
        double r = Math.sqrt(random.nextDouble()) * radius;
        return new Vector2((float) (Math.cos(t) * r), (float) (Math.sin(t) * r));
    }
}
