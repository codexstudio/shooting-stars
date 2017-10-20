package com.codex.shootingstars;

import android.util.Log;
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
    private static Pixmap background;
    private static Pixmap virtualJoystickPixmap;
    private static Pixmap ship;

    private int backgroundXPos = 0;
    private int backgroundYPos = 0;
    private int virtualJoystickXPos = 0;
    private int virtualJoystickYPos = 0;
    private float scrollSpeed = 5.0f;

    int width;
    int height;

    //private int oldScore;
    private String score = "0";

    private Random random = new Random();

    private boolean bIsTouching;
    private VirtualJoystick virtualJoystick;


    public GameScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        ship = g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB4444);
        virtualJoystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        virtualJoystick = new VirtualJoystick();
        width = g.getWidth();
        height = g.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (virtualJoystick.isActiveAndSetDirection(event)) {
                bIsTouching = true;
            }
            else {
                bIsTouching = false;
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
        }

        if (bIsTouching) {
            double test = virtualJoystick.getDirection().getX();
            double test2 = virtualJoystick.getDirection().getY();
            Log.d("X Scroll",Double.toString(test));
            Log.d("Y Scroll",Double.toString(test2));
            if (virtualJoystick.getDirection().getX() < 0) {
                backgroundXPos -= scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.RIGHT_VECTOR);
            }
            if (virtualJoystick.getDirection().getX() > 0) {
                backgroundXPos -= scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.RIGHT_VECTOR);
            }
            if (virtualJoystick.getDirection().getY() > 0) {
                backgroundYPos += scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.UP_VECTOR);
            }
            if (virtualJoystick.getDirection().getY() < 0) {
                backgroundYPos += scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.UP_VECTOR);
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

