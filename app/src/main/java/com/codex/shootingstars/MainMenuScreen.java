package com.codex.shootingstars;

import android.graphics.Color;
import android.graphics.Typeface;
import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.impl.AndroidFont;

import java.util.List;

public class MainMenuScreen extends Screen {
    private static Pixmap background;
    private Pixmap explosion;
    private Font font;

    public MainMenuScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        explosion = g.newPixmap("explosion.png", Graphics.PixmapFormat.ARGB8888);
        g.addAnimation(explosion, 300, 300, 6, 8, 256, 256, true);
        font = new AndroidFont(48, Typeface.DEFAULT);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
//                if (inBounds(event, playXPos, playYPos, playButton.getWidth(), playButton.getHeight())) {
//                    game.setScreen(new GameScreen(game));
//                    return;
//                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        g.drawText("Play game", 200, 200, font, Color.WHITE);
        g.update(deltaTime);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
