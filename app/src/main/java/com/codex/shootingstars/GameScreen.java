package com.codex.shootingstars;

import android.view.MotionEvent;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;

import java.util.List;
import java.util.Random;

public class GameScreen extends Screen{
    private static final float UPDATE_BLOB_TIME = 1.0f;

    private static Pixmap background;
    private static Pixmap blob;
    private static Pixmap numbers;

    private int blobXPos;
    private int blobYPos;

    private int backgroundXPos = 0;
    private int backgroundYPos = 0;

    int width;
    int height;

    //private int oldScore;
    private String score = "0";

    private float timePassed;

    private Random random = new Random();


    public GameScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        blob = g.newPixmap("blob.png", Graphics.PixmapFormat.ARGB4444);
        numbers = g.newPixmap("numbers.png", Graphics.PixmapFormat.ARGB4444);
        width = g.getWidth();
        height = g.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == MotionEvent.ACTION_MOVE) {
                if (inBounds(event, 0, 0, width, height/2)) {
                    if (backgroundYPos < -height) {
                        backgroundYPos = 0;
                    }
                    else
                    {
                        backgroundYPos -= 5;
                    }
                    //score = "" + oldScore;
                }
                if(inBounds(event, 0, height/2, width, height))
                {
                    if (backgroundYPos > height) {
                        backgroundYPos = 0;
                    }
                    else
                    {
                        backgroundYPos += 5;
                    }
                    //score = "" + oldScore;
                }
                if(inBounds(event, 0, 0, width/2, height))
                {
                    if (backgroundXPos < -width) {
                        backgroundXPos = 0;
                    }
                    else
                    {
                        backgroundXPos -= 5;
                    }
                    //score = "" + oldScore;
                }
                if(inBounds(event, width/2, 0, width, height))
                {
                    if (backgroundXPos > width) {
                        backgroundXPos = 0;
                    }
                    else
                    {
                        backgroundXPos += 5;
                    }
                    //score = "" + oldScore;
                }
            }
        }

        timePassed += deltaTime;
        if (timePassed > UPDATE_BLOB_TIME) {
            blobXPos = random.nextInt(game.getGraphics().getWidth() - blob.getWidth());
            blobYPos = random.nextInt(game.getGraphics().getHeight() - blob.getHeight());
            timePassed = 0;
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        //original
        g.drawPixmap(background, backgroundXPos, backgroundYPos);

        //up/down screens
        if (backgroundYPos > 0) {
            g.drawPixmap(background, backgroundXPos, -background.getHeight() + backgroundYPos);
        }
        if (backgroundYPos < 0) {
            g.drawPixmap(background, backgroundXPos, background.getHeight() + backgroundYPos);
        }

        //left/right screens
        if (backgroundXPos > 0) {
            g.drawPixmap(background, -background.getWidth() + backgroundXPos, backgroundYPos);
        }
        if (backgroundXPos < 0) {
            g.drawPixmap(background, background.getWidth() + backgroundXPos, backgroundYPos);
        }

        //fourth corner screen
        if ((backgroundYPos > 0) && (backgroundXPos > 0))
        {
            g.drawPixmap(background, -background.getWidth() + backgroundXPos, -background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos > 0) && (backgroundXPos < 0))
        {
            g.drawPixmap(background, background.getWidth() + backgroundXPos, -background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos < 0) && (backgroundXPos > 0))
        {
            g.drawPixmap(background, -background.getWidth() + backgroundXPos, background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos < 0) && (backgroundXPos < 0))
        {
            g.drawPixmap(background, background.getWidth() + backgroundXPos, background.getHeight() + backgroundYPos);
        }

        g.drawPixmap(blob, blobXPos, blobYPos);

        //drawText(g, score, g.getWidth() / 2 - score.length() * 20 / 2, g.getHeight() - 42);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

//    public void drawText(Graphics g, String line, int x, int y) {
//        int len = line.length();
//        for (int i = 0; i < len; i++) {
//            char character = line.charAt(i);
//
//            if (character == ' ') {
//                x += 20;
//                continue;
//            }
//
//            int srcX;
//            int srcWidth;
//            if (character == '.') {
//                srcX = 200;
//                srcWidth = 10;
//            } else {
//                srcX = (character - '0') * 20;
//                srcWidth = 20;
//            }
//
//            g.drawPixmap(numbers, x, y, srcX, 0, srcWidth, 32);
//            x += srcWidth;
//        }
//    }
}

