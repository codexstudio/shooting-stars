package com.codex.shootingstars;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.MathUtil;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.impl.AndroidPixmap;
import com.filip.androidgames.framework.types.Vector2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class SpaceBackground {
    private final static int STAR_GENERATION_RADIUS = 1525;
    private final static int OBJECT_GENERATION_RADIUS = 3000;
    private final static int NUMBER_OF_STARS = 2500;
    private final static int NUMBER_OF_OBJECTS = 8;

    private List<BackgroundObject> significantObjects;
    private List<Vector2> starList;
    private Paint starPaint;

    SpaceBackground(Context context, PlayerView playerView) {
        starList = new ArrayList<>();
        significantObjects = new ArrayList<>();
        starPaint = new Paint();

        starPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        generateObjects(context, playerView.getLocation());
    }

    private void generateObjects(Context context, Vector2 location) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap;
        try {
            inputStream = assetManager.open("nebula1.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            starList.add(Vector2.sum(MathUtil.randomPointInCircle(STAR_GENERATION_RADIUS), location));
        }
        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            Vector2 loc = Vector2.sum(MathUtil.randomPointInCircle(OBJECT_GENERATION_RADIUS), location);
            significantObjects.add(new BackgroundObject(bitmap, loc));
        }
    }

    Pixmap getBackground(final PlayerView playerView) {
        Bitmap bitmap = Bitmap.createBitmap(playerView.width, playerView.height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        starPaint.setColor(Color.WHITE);

        for (Vector2 star : starList) {
            if (playerView.isWithinView(star)) {
                final Vector2 location = playerView.getScreenLocation(star);
                canvas.drawPoint(location.getX(), location.getY(), starPaint);
            }
            else if (Vector2.distance(star, playerView.getLocation()) > STAR_GENERATION_RADIUS) {
                star.setX(playerView.getLocation().getX() + (playerView.getLocation().getX() - star.getX()));
                star.setY(playerView.getLocation().getY() + (playerView.getLocation().getY() - star.getY()));
            }
        }

        for (BackgroundObject obj : significantObjects) {
            if (playerView.isWithinView(obj)) {
                final Vector2 location = playerView.getScreenLocation(obj.getLocation());
                canvas.drawBitmap(obj.getBitmap(), location.getX() - obj.getWidth() / 2, location.getY() - obj.getHeight() / 2, starPaint);
            }
            else if (Vector2.distance(obj.getLocation(), playerView.getLocation()) > OBJECT_GENERATION_RADIUS) {
                obj.setLocation(new Vector2(
                        playerView.getLocation().getX() + (playerView.getLocation().getX() - obj.getLocation().getX()),
                        playerView.getLocation().getY() + (playerView.getLocation().getY() - obj.getLocation().getY())
                ));
            }
        }
        return new AndroidPixmap(bitmap, Graphics.PixmapFormat.RGB565);
    }
}
