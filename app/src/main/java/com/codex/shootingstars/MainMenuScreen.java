package com.codex.shootingstars;

import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Input.TouchEvent;

import java.util.List;

public class MainMenuScreen extends Screen {
    private static Pixmap background;

    private Button playGameBtn;
    private Button optionsBtn;
    private Button leaderboardsBtn;
    private Button charBtn;
    private Button backBtn;
    private Button soundOnBtn;
    private Button soundOffBtn;

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

    private Pixmap numbersPixmap;
    public static Music mainTheme;

    private CanvasContainer<BaseUIObject> uiContainer;

    public MainMenuScreen(Game game) {
        super(game);

        Settings.loadFiles(game.getFileIO());
        mainTheme = game.getAudio().newMusic("Bag Raiders - Shooting Stars.mp3");
        mainTheme.setLooping(true);
        if (Settings.soundEnabled) {
            mainTheme.play();
        }

        Graphics g = game.getGraphics();

        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        numbersPixmap = g.newPixmap("numbers.png", Graphics.PixmapFormat.ARGB8888);
        //explosion = g.newPixmap("explosion.png", Graphics.PixmapFormat.ARGB8888);
        //g.addAnimation(explosion, 300, 300, 6, 8, 256, 256, true);

        uiContainer = new CanvasContainer<BaseUIObject>();


        playGameBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Play_Game.png", Graphics.PixmapFormat.ARGB8888));
        optionsBtn = new Button(g.getWidth() / 2, g.getHeight() * 2.5f / 5, 1.0f, 1.0f, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));
        leaderboardsBtn = new Button(g.getWidth() / 2, g.getHeight() * 3 / 5, 1.0f, 1.0f, g.newPixmap("Leaderboards.png", Graphics.PixmapFormat.ARGB8888));
        charBtn = new Button(g.getWidth() / 2, g.getHeight() * 3.5f / 5, 1.0f, 1.0f, g.newPixmap("Characters.png", Graphics.PixmapFormat.ARGB8888));
        backBtn = new Button(g.getWidth() - 200, g.getHeight() - 100, 1.0f, 1.0f, g.newPixmap("Back.png", Graphics.PixmapFormat.ARGB8888));
        soundOnBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Sound_On.png", Graphics.PixmapFormat.ARGB8888));
        soundOffBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Sound_Off.png", Graphics.PixmapFormat.ARGB8888));

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


        uiContainer.add(playGameBtn);
        uiContainer.add(optionsBtn);
        uiContainer.add(leaderboardsBtn);
        uiContainer.add(charBtn);
        uiContainer.add(backBtn);
        uiContainer.add(soundOnBtn);
        uiContainer.add(soundOffBtn);

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
        soundOffBtn.setVisibility(false);
        soundOnBtn.setVisibility(false);

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
                if (playGameBtn.isVisible()) {
                    if (playGameBtn.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new GameScreen(game));
                    }
                }
                if (optionsBtn.isVisible()) {
                    if (optionsBtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameBtn.setVisibility(false);
                        leaderboardsBtn.setVisibility(false);
                        charBtn.setVisibility(false);
                        optionsBtn.setVisibility(false);
                        title.setVisibility(false);

                        backBtn.setVisibility(true);
                        options.setVisibility(true);

                        if (Settings.soundEnabled)
                        {
                            soundOnBtn.setVisibility(true);
                            soundOffBtn.setVisibility(false);
                        }
                        else
                        {
                            soundOffBtn.setVisibility(true);
                            soundOnBtn.setVisibility(false);
                        }
                    }
                }
                if (leaderboardsBtn.isVisible()) {
                    if (leaderboardsBtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameBtn.setVisibility(false);
                        optionsBtn.setVisibility(false);
                        charBtn.setVisibility(false);
                        leaderboardsBtn.setVisibility(false);
                        backBtn.setVisibility(true);
                        leaderboards.setVisibility(true);
                        title.setVisibility(false);
                    }
                }
                if (charBtn.isVisible()) {
                    if (charBtn.getBoundingRect().contains(event.x, event.y)) {
                        playGameBtn.setVisibility(false);
                        leaderboardsBtn.setVisibility(false);
                        charBtn.setVisibility(false);
                        optionsBtn.setVisibility(false);
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
                        playGameBtn.setVisibility(true);
                        leaderboardsBtn.setVisibility(true);
                        optionsBtn.setVisibility(true);
                        charBtn.setVisibility(true);
                        backBtn.setVisibility(false);
                        title.setVisibility(true);

                        leaderboards.setVisibility(false);

                        character.setVisibility(false);
                        shipOne.setVisibility(false);
                        shipTwo.setVisibility(false);
                        shipThree.setVisibility(false);
                        shipFour.setVisibility(false);
                        shipFive.setVisibility(false);
                        shipSix.setVisibility(false);

                        options.setVisibility(false);
                        soundOffBtn.setVisibility(false);
                        soundOnBtn.setVisibility(false);
                    }
                }
                if (soundOnBtn.isVisible() && (soundOnBtn.getBoundingRect().contains(event.x, event.y)))
                {
                    soundOnBtn.setVisibility(false);
                    soundOffBtn.setVisibility(true);
                    Settings.soundEnabled = false;
                    Settings.saveFiles(game.getFileIO());
                    mainTheme.dispose();
                }
                else if (soundOffBtn.isVisible() && (soundOffBtn.getBoundingRect().contains(event.x, event.y)))
                {
                    soundOnBtn.setVisibility(true);
                    soundOffBtn.setVisibility(false);
                    Settings.soundEnabled = true;
                    Settings.saveFiles(game.getFileIO());
                    mainTheme = game.getAudio().newMusic("Bag Raiders - Shooting Stars.mp3");
                    mainTheme.setLooping(true);
                    mainTheme.play();
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        // g.drawText("Play game", 200, 200, font, Color.WHITE);
        g.drawAnimations(deltaTime);

        uiContainer.drawContainer(g);
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

    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++){
            int srcX = 0;
            char character = line.charAt(i);
            int s = Character.getNumericValue(character);

            srcX = s*44;
            g.drawPixmap(numbersPixmap, x, y, srcX, 0, 44, 58);
            x += 44;
        }
    }

}
