package com.codex.shootingstars;

import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Graphics.Point;
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

    private Point bkgPos;
    private Point joystickPos;
    private float scrollSpeed = 0.25f;

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
        virtualJoystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        virtualJoystick = new VirtualJoystick();
        width = g.getWidth();
        height = g.getHeight();
        bkgPos = new Point();
        joystickPos = new Point();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for (TouchEvent event : touchEvents) {
            bIsTouching = virtualJoystick.isActiveAndSetDirection(event);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                joystickPos.x = event.x;
                joystickPos.y = event.y;
            }
        }

        if (bIsTouching) {
            if (virtualJoystick.getDirection().getX() < 0) { bkgPos.x -= scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.RIGHT_VECTOR); }
            if (virtualJoystick.getDirection().getX() > 0) { bkgPos.x -= scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.RIGHT_VECTOR); }
            if (virtualJoystick.getDirection().getY() > 0) { bkgPos.y += scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.UP_VECTOR); }
            if (virtualJoystick.getDirection().getY() < 0) { bkgPos.y += scrollSpeed*Vector2.Projection(virtualJoystick.getDirection(), Vector2.UP_VECTOR); }
            if (bkgPos.x > width || bkgPos.x < -width) { bkgPos.x = 0; }
            if (bkgPos.y < -height || bkgPos.y > width) { bkgPos.y = 0; }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        //original
        g.drawPixmap(background, bkgPos.x, bkgPos.y);

        //up/down screens
        if (bkgPos.y > 0) { g.drawPixmap(background, bkgPos.x, -background.getHeight() + bkgPos.y); }
        if (bkgPos.y < 0) { g.drawPixmap(background, bkgPos.x, background.getHeight() + bkgPos.y); }

        //left/right screens
        if (bkgPos.x > 0) { g.drawPixmap(background, -background.getWidth() + bkgPos.x, bkgPos.y); }
        if (bkgPos.x < 0) { g.drawPixmap(background, background.getWidth() + bkgPos.x, bkgPos.y); }

        //fourth corner screen
        if ((bkgPos.y > 0) && (bkgPos.x > 0)) { g.drawPixmap(background, -background.getWidth() + bkgPos.x, -background.getHeight() + bkgPos.y); }
        if ((bkgPos.y > 0) && (bkgPos.x < 0)) { g.drawPixmap(background, background.getWidth() + bkgPos.x, -background.getHeight() + bkgPos.y); }
        if ((bkgPos.y < 0) && (bkgPos.x > 0)) { g.drawPixmap(background, -background.getWidth() + bkgPos.x, background.getHeight() + bkgPos.y); }
        if ((bkgPos.y < 0) && (bkgPos.x < 0)) { g.drawPixmap(background, background.getWidth() + bkgPos.x, background.getHeight() + bkgPos.y); }

        if (bIsTouching) {
            g.drawPixmap(virtualJoystickPixmap, new Point(joystickPos.x - 128, joystickPos.y - 128), new Point(256));
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}

