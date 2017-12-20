package com.codex.shootingstars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.MathUtil;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.impl.AndroidPixmap;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceBackground {
    private final static int OBJECT_GENERATION_RADIUS = 2500;
    private final static int NUMBER_OF_STARS = 5000;

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
            starList.add(MathUtil.randomPointInCircle(radius));
        }

//        for (int i = 0; i < 4; i++) {
//            Vector2 loc = randomPointInCircle(radius);
//        }
    }



    Pixmap getBackground(final PlayerView playerView) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        starPaint.setColor(Color.WHITE);

        Log.d("current view", Float.toString(playerView.getLocation().getX()) + "\t" + Float.toString(playerView.getLocation().getY()));

        for (Vector2 star : starList) {
            if (playerView.isWithinView(star)) {
                final Vector2 location = playerView.getScreenLocation(star);
                canvas.drawPoint(location.getX(), location.getY(), starPaint);
            }
            else if (Vector2.distance(star, playerView.getLocation()) > OBJECT_GENERATION_RADIUS) {
                star.setX(playerView.getLocation().getX() + (playerView.getLocation().getX() - star.getX()));
                star.setY(playerView.getLocation().getY() + (playerView.getLocation().getY() - star.getY()));
            }
        }

        return new AndroidPixmap(bitmap, Graphics.PixmapFormat.RGB565);
//        for (BackgroundObject obj : significantObjects) {
//            if (playerView.isWithinView(obj))
//                canvas.drawBitmap(obj.getBitmap(),
//            }
//        }
    }
}
