package com.codex.shootingstars;

import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;
import java.util.Random;

public class GameScreen extends Screen {
    private static final float UPDATE_BLOB_TIME = 1.0f;

    private static Pixmap background;
    private static Pixmap blob;
    private static Pixmap virtualJoystickPixmap;
    private static Pixmap ship;

    private int blobXPos;
    private int blobYPos;

    private int backgroundXPos = 0;
    private int backgroundYPos = 0;
    private int virtualJoystickXPos = 0;
    private int virtualJoystickYPos = 0;

    int width;
    int height;

    //private int oldScore;
    private String score = "0";

    private float timePassed;

    private Random random = new Random();

    private boolean bIsTouching;
    private VirtualJoystick virtualJoystick;
    private Vector2 joystickDirection;


    public GameScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        blob = g.newPixmap("blob.png", Graphics.PixmapFormat.ARGB4444);
        ship = g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB4444);
        virtualJoystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        virtualJoystick = new VirtualJoystick();
        joystickDirection = new Vector2();
        width = g.getWidth();
        height = g.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (virtualJoystick.isActive(event)) {
                joystickDirection = virtualJoystick.getDirection();
            }

            if (event.type == TouchEvent.TOUCH_DOWN) {
                virtualJoystickXPos = event.x;
                virtualJoystickYPos = event.y;

                bIsTouching = true;

                if (inBounds(event, 0, 0, width, height / 2)) {
                    if (backgroundYPos < -height) {
                        backgroundYPos = 0;
                    }
//                    else
//                    {
//                        backgroundYPos += 5;
//                    }
                    //score = "" + oldScore;
                }
                if (inBounds(event, 0, height / 2, width, height)) {
                    if (backgroundYPos > height) {
                        backgroundYPos = 0;
                    }
//                    else
//                    {
//                        backgroundYPos -= 5;
//                    }
                    //score = "" + oldScore;
                }
                if (inBounds(event, 0, 0, width / 2, height)) {
                    if (backgroundXPos < -width) {
                        backgroundXPos = 0;
                    }
//                    else
//                    {
//                        backgroundXPos += 5;
//                    }
                    //score = "" + oldScore;
                }
                if (inBounds(event, width / 2, 0, width, height)) {
                    if (backgroundXPos > width) {
                        backgroundXPos = 0;
                    }
//                    else
//                    {
//                        backgroundXPos -= 5;
//                    }
                    //score = "" + oldScore;
                }
            }
            if (event.type == TouchEvent.TOUCH_UP) {
                bIsTouching = false;
            }
        }

        timePassed += deltaTime;
        if (timePassed > UPDATE_BLOB_TIME) {
            blobXPos = random.nextInt(game.getGraphics().getWidth() - blob.getWidth());
            blobYPos = random.nextInt(game.getGraphics().getHeight() - blob.getHeight());
            timePassed = 0;
        }
        if (bIsTouching) {
            if (virtualJoystickXPos < width / 2) {
                backgroundXPos += 5;
            }
            if (virtualJoystickXPos > width / 2) {
                backgroundXPos -= 5;
            }
            if (virtualJoystickYPos > height / 2) {
                backgroundYPos -= 5;
            }
            if (virtualJoystickYPos < height / 2) {
                backgroundYPos += 5;
            }
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
        if ((backgroundYPos > 0) && (backgroundXPos > 0)) {
            g.drawPixmap(background, -background.getWidth() + backgroundXPos, -background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos > 0) && (backgroundXPos < 0)) {
            g.drawPixmap(background, background.getWidth() + backgroundXPos, -background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos < 0) && (backgroundXPos > 0)) {
            g.drawPixmap(background, -background.getWidth() + backgroundXPos, background.getHeight() + backgroundYPos);
        }
        if ((backgroundYPos < 0) && (backgroundXPos < 0)) {
            g.drawPixmap(background, background.getWidth() + backgroundXPos, background.getHeight() + backgroundYPos);
        }

        if (bIsTouching) {
            g.drawPixmap(virtualJoystickPixmap, virtualJoystickXPos - 128, virtualJoystickYPos - 128, 0, 0, virtualJoystickPixmap.getWidth(), virtualJoystickPixmap.getHeight(), 256, 256);
        }
        g.drawPixmap(blob, blobXPos, blobYPos);
        g.drawPixmap(ship, width / 2 - ship.getWidth() / 6, height / 2 - ship.getHeight() / 6, 0, 0, ship.getWidth(), ship.getHeight(), ship.getWidth() / 3, ship.getHeight() / 3);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}

