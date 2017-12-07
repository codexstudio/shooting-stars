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

    private Button playGameMenubtn;
    private Button optionsMenubtn;
    private Button lbMenubtn;
    private Button charMenubtn;
    private Button backBtn;
    //    Button leftCharbtn;
    //    Button rightCharbtn;

    private Button shipOne;
    private Button shipTwo;
    private Button shipThree;
    private Button shipFour;
    private Button shipFive;
    private Button shipSix;

    private StaticUI character;
    private StaticUI leaderboards;
    private StaticUI options;
    private StaticUI title;

    private CanvasContainer<BaseUIObject> uiContainer;

    public MainMenuScreen(Game game) {
        super(game);

        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        //explosion = g.newPixmap("explosion.png", Graphics.PixmapFormat.ARGB8888);
        //g.addAnimation(explosion, 300, 300, 6, 8, 256, 256, true);
        font = new AndroidFont(48, Typeface.DEFAULT, Color.WHITE);


        uiContainer = new CanvasContainer<BaseUIObject>();


        playGameMenubtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Play_Game.png", Graphics.PixmapFormat.ARGB8888));
        optionsMenubtn = new Button(g.getWidth() / 2, g.getHeight() * 2.5f / 5, 1.0f, 1.0f, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));
        lbMenubtn = new Button(g.getWidth() / 2, g.getHeight() * 3 / 5, 1.0f, 1.0f, g.newPixmap("Leaderboards.png", Graphics.PixmapFormat.ARGB8888));
        charMenubtn = new Button(g.getWidth() / 2, g.getHeight() * 3.5f / 5, 1.0f, 1.0f, g.newPixmap("Characters.png", Graphics.PixmapFormat.ARGB8888));
        backBtn = new Button(g.getWidth() - 200, g.getHeight() - 100, 1.0f, 1.0f, g.newPixmap("Back.png", Graphics.PixmapFormat.ARGB8888));
//        leftCharbtn = new Button(g.getWidth()/2, g.getHeight()*2/5, 1.0f, 1.0f, g.newPixmap("LeftArrow.png",Graphics.PixmapFormat.ARGB8888));
//        rightCharbtn = new Button(g.getWidth()/2, g.getHeight()*2/5, 1.0f, 1.0f, g.newPixmap("RightArrow.png",Graphics.PixmapFormat.ARGB8888));

        shipOne = new Button(g.getWidth() / 4, g.getHeight() * 1 / 5, 2 / 5f, 2 / 5f, g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        shipTwo = new Button(g.getWidth() / 4, g.getHeight() * 2 / 5, 2 / 5f, 2 / 5f, g.newPixmap("PlayerShip2.png", Graphics.PixmapFormat.ARGB8888));
        shipThree = new Button(g.getWidth() / 4, g.getHeight() * 3 / 5, 1 / 3f, 1 / 3f, g.newPixmap("PlayerShip3.png", Graphics.PixmapFormat.ARGB8888));
        shipFour = new Button(g.getWidth() * 3 / 4, g.getHeight() * 1 / 5, 1 / 6f, 1 / 6f, g.newPixmap("PlayerShip4.png", Graphics.PixmapFormat.ARGB8888));
        shipFive = new Button(g.getWidth() * 3 / 4, g.getHeight() * 2 / 5, 1 / 3f, 1 / 3f, g.newPixmap("PlayerShip5.png", Graphics.PixmapFormat.ARGB8888));
        shipSix = new Button(g.getWidth() * 3 / 4, g.getHeight() * 3 / 5, 1 / 12f, 1 / 12f, g.newPixmap("PlayerShip6.png", Graphics.PixmapFormat.ARGB8888));

        leaderboards = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Leaderboards.png", Graphics.PixmapFormat.ARGB8888));
        character = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Characters.png", Graphics.PixmapFormat.ARGB8888));
        options = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));
        title = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("title.png", Graphics.PixmapFormat.ARGB8888));


        uiContainer.add(playGameMenubtn);
        uiContainer.add(optionsMenubtn);
        uiContainer.add(lbMenubtn);
        uiContainer.add(charMenubtn);
        uiContainer.add(backBtn);
//       uiContainer.add(leftCharbtn);
//       uiContainer.add(rightCharbtn);

        uiContainer.add(character);
        uiContainer.add(options);
        uiContainer.add(leaderboards);
        uiContainer.add(title);

        uiContainer.add(shipOne);
        uiContainer.add(shipTwo);
        uiContainer.add(shipThree);
        uiContainer.add(shipFour);
        uiContainer.add(shipFive);
        uiContainer.add(shipSix);

        backBtn.setVisibility(false);
//        leftCharbtn.setVisibility(false);
//        rightCharbtn.setVisibility(false);

        character.setVisibility(false);
        options.setVisibility(false);
        leaderboards.setVisibility(false);

        shipOne.setVisibility(false);
        shipTwo.setVisibility(false);
        shipThree.setVisibility(false);
        shipFour.setVisibility(false);
        shipFive.setVisibility(false);
        shipSix.setVisibility(false);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (playGameMenubtn.isVisible()) {
                    if (playGameMenubtn.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new GameScreen(game));
                    }
                }
                if (optionsMenubtn.isVisible()) {
                    if (optionsMenubtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                        options.setVisibility(true);
                        title.setVisibility(false);
                    }
                }
                if (lbMenubtn.isVisible()) {
                    if (lbMenubtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                        leaderboards.setVisibility(true);
                        title.setVisibility(false);
                    }
                }
                if (charMenubtn.isVisible()) {
                    if (charMenubtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                        character.setVisibility(true);
                        title.setVisibility(false);

                        shipOne.setVisibility(true);
                        shipTwo.setVisibility(true);
                        shipThree.setVisibility(true);
                        shipFour.setVisibility(true);
                        shipFive.setVisibility(true);
                        shipSix.setVisibility(true);
                    }
                }
                if (backBtn.isVisible()) {
                    if (backBtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameMenubtn.setVisibility(true);
                        lbMenubtn.setVisibility(true);
                        optionsMenubtn.setVisibility(true);
                        charMenubtn.setVisibility(true);
                        backBtn.setVisibility(false);
                        title.setVisibility(true);

                        character.setVisibility(false);
                        options.setVisibility(false);
                        leaderboards.setVisibility(false);

                        shipOne.setVisibility(false);
                        shipTwo.setVisibility(false);
                        shipThree.setVisibility(false);
                        shipFour.setVisibility(false);
                        shipFive.setVisibility(false);
                        shipSix.setVisibility(false);
                    }
                }
            }
        }
        uiContainer.update(deltaTime);
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        // g.drawText("Play game", 200, 200, font, Color.WHITE);
        g.drawAnimations(deltaTime);

        uiContainer.draw(g);
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
