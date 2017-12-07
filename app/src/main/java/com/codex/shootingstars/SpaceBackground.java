package com.codex.shootingstars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.impl.AndroidPixmap;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;
import com.google.android.gms.games.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceBackground {
    private final static int OBJECT_GENERATION_RADIUS = 5000;
    private final static int NUMBER_OF_STARS = 5000;
    private Random random = new Random();
    private List<BackgroundObject> significantObjects;
    private List<Vector2> starList;
    private int width;
    private int height;
    private Paint starPaint;

    SpaceBackground(PlayerView playerView, int width, int height) {
        starList = new ArrayList<>();
        this.width = width;
        this.height = height;
        starPaint = new Paint();

        generateObjects(playerView.getLocation(), OBJECT_GENERATION_RADIUS);
    }

    private void generateObjects(Vector2 location, int radius) {
        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            starList.add(randomPointInCircle(radius));
        }

//        for (int i = 0; i < 4; i++) {
//            Vector2 loc = randomPointInCircle(radius);
//        }
    }

    private Vector2 randomPointInCircle(float radius) {
        double t = 2 * Math.PI * random.nextDouble();
        double r = Math.sqrt(random.nextDouble()) * radius;
        return new Vector2((float) (Math.cos(t) * r), (float) (Math.sin(t) * r));
    }

    Pixmap getBackground(final PlayerView playerView) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        starPaint.setColor(Color.WHITE);

        List<Vector2> starsToAdd = new ArrayList<>();
        for (Vector2 star : starList) {
            if (playerView.isWithinView(star)) {
                final Vector2 location = playerView.getScreenLocation(star);
                canvas.drawPoint(location.getX(), location.getY(), starPaint);
            }
            else if (Vector2.Distance(star, playerView.getLocation()) > OBJECT_GENERATION_RADIUS) {
                starsToAdd.add(new Vector2(star.getX() * - 1, star.getY() * -1));
            }
        }
        starList.removeIf(star -> (Vector2.Distance(star, playerView.getLocation()) > OBJECT_GENERATION_RADIUS));
        starList.addAll(starsToAdd);

        return new AndroidPixmap(bitmap, Graphics.PixmapFormat.RGB565);
//        for (BackgroundObject obj : significantObjects) {
//            if (playerView.isWithinView(obj))
//                canvas.drawBitmap(obj.getBitmap(),
//            }
//        }

    }
}
