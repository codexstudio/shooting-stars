package com.codex.shootingstars;

import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;

public class MainMenuScreen extends Screen {
    private static Pixmap background;
    String leaderboardsLines[] = new String[5];

    private Button playGameBtn;
    private Button optionsBtn;
    private Button leaderboardsBtn;
    private Button charactersBtn;
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

    boolean bKeepPlaying;

    private Pixmap numbersPixmap;
    public static Music mainTheme;

    private CanvasContainer<BaseUIObject> mainMenuContainer;
    private CanvasContainer<BaseUIObject> optionsContainer;
    private CanvasContainer<BaseUIObject> leaderboardsContainer;
    private CanvasContainer<BaseUIObject> charactersContainer;

    public MainMenuScreen(Game game) {
        super(game);

        bKeepPlaying = false;
        Settings.loadFiles(game.getFileIO());
        if (Settings.soundEnabled) {
            if (mainTheme == null) {
                mainTheme = game.getAudio().newMusic("Bag Raiders - Shooting Stars.mp3");
                mainTheme.setLooping(true);
                mainTheme.play();
            }
        }
        for (int i = 0; i < 5; i++)
        {
            leaderboardsLines[i] = "" + (i + 1) + ". " + Settings.highscores[i]; // "1. 40"
        }

        Graphics g = game.getGraphics();

        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        numbersPixmap = g.newPixmap("numbers.png", Graphics.PixmapFormat.ARGB8888);

        mainMenuContainer = new CanvasContainer<BaseUIObject>();
        charactersContainer = new CanvasContainer<BaseUIObject>();
        leaderboardsContainer = new CanvasContainer<BaseUIObject>();
        optionsContainer = new CanvasContainer<BaseUIObject>();


        playGameBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Play_Game.png", Graphics.PixmapFormat.ARGB8888));
        optionsBtn = new Button(g.getWidth() / 2, g.getHeight() * 2.5f / 5, 1.0f, 1.0f, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));
        leaderboardsBtn = new Button(g.getWidth() / 2, g.getHeight() * 3 / 5, 1.0f, 1.0f, g.newPixmap("Leaderboards.png", Graphics.PixmapFormat.ARGB8888));
        charactersBtn = new Button(g.getWidth() / 2, g.getHeight() * 3.5f / 5, 1.0f, 1.0f, g.newPixmap("Characters.png", Graphics.PixmapFormat.ARGB8888));
        backBtn = new Button(g.getWidth() - 200, g.getHeight() - 100, 1.0f, 1.0f, g.newPixmap("Back.png", Graphics.PixmapFormat.ARGB8888));
        soundOnBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Sound_On.png", Graphics.PixmapFormat.ARGB8888));
        soundOffBtn = new Button(g.getWidth() / 2, g.getHeight() * 2 / 5, 1.0f, 1.0f, g.newPixmap("Sound_Off.png", Graphics.PixmapFormat.ARGB8888));

        shipOne = new Button(g.getWidth() / 4, g.getHeight() * 1 / 5, 2 / 5f, 2 / 5f, g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888));
        shipTwo = new Button(g.getWidth() / 4, g.getHeight() * 2 / 5, 0.16f, 0.31f, g.newPixmap("PlayerShip2.png", Graphics.PixmapFormat.ARGB8888));
        shipThree = new Button(g.getWidth() / 4, g.getHeight() * 3 / 5, 0.18f, 0.38f, g.newPixmap("PlayerShip3.png", Graphics.PixmapFormat.ARGB8888));
        shipFour = new Button(g.getWidth() * 3 / 4, g.getHeight() * 1 / 5, 0.138f, 0.232f, g.newPixmap("PlayerShip4.png", Graphics.PixmapFormat.ARGB8888));
        shipFive = new Button(g.getWidth() * 3 / 4, g.getHeight() * 2 / 5, 0.201f, 0.302f, g.newPixmap("PlayerShip5.png", Graphics.PixmapFormat.ARGB8888));
        shipSix = new Button(g.getWidth() * 3 / 4, g.getHeight() * 3 / 5, 0.043f, 0.073f, g.newPixmap("PlayerShip6.png", Graphics.PixmapFormat.ARGB8888));

        leaderboards = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Leaderboards.png", Graphics.PixmapFormat.ARGB8888));
        character = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Characters.png", Graphics.PixmapFormat.ARGB8888));
        options = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));
        title = new StaticUI(g.getWidth() / 2, g.getHeight() * 1 / 11, 1, 1, g.newPixmap("title.png", Graphics.PixmapFormat.ARGB8888));

        mainMenuContainer.add(title, playGameBtn, optionsBtn, leaderboardsBtn, charactersBtn);
        optionsContainer.add(options, backBtn, soundOffBtn, soundOnBtn);
        leaderboardsContainer.add(leaderboards, backBtn);
        charactersContainer.add(character, shipOne, shipTwo, shipThree, shipFour, shipFive, shipSix, backBtn);

        mainMenuContainer.setVisibility(true);
        optionsContainer.setVisibility(false);
        leaderboardsContainer.setVisibility(false);
        charactersContainer.setVisibility(false);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        mainMenuContainer.update(deltaTime);
        optionsContainer.update(deltaTime);
        charactersContainer.update(deltaTime);
        leaderboardsContainer.update(deltaTime);

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (playGameBtn.isVisible()) {
                    if (playGameBtn.onTouchRect(event)) {
                        bKeepPlaying = true;
                        game.setScreen(new GameScreen(game));
                    }
                }
                if (optionsBtn.isVisible()) {
                    if (optionsBtn.onTouchRect(event)) {
                        mainMenuContainer.setVisibility(false);
                        optionsContainer.setVisibility(true);
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
                    if (leaderboardsBtn.onTouchRect(event)) {
                        leaderboardsContainer.setVisibility(true);
                        mainMenuContainer.setVisibility(false);
                    }
                }
                if (charactersBtn.isVisible()) {
                    if (charactersBtn.onTouchRect(event)) {
                        charactersContainer.setVisibility(true);
                        mainMenuContainer.setVisibility(false);
                    }
                }
                if (character.isVisible())
                {
                    if (Vector2.distance(new Vector2(event.x, event.y), shipOne.transform.getLocation()) < shipOne.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), shipTwo.transform.getLocation()) < shipTwo.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip2.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), shipThree.transform.getLocation()) < shipThree.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip3.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), shipFour.transform.getLocation()) < shipFour.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip4.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), shipFive.transform.getLocation()) < shipFive.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip5.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), shipSix.transform.getLocation()) < shipSix.getBoundingRadius()) {
                        Settings.PlayerShip = "PlayerShip6.png";
                        Settings.saveFiles(game.getFileIO());
                    }
                }
                if (backBtn.isVisible()) {
                    if (backBtn.onTouchRect(event)) {
                        mainMenuContainer.setVisibility(true);
                        charactersContainer.setVisibility(false);
                        leaderboardsContainer.setVisibility(false);
                        optionsContainer.setVisibility(false);
                    }
                }
                if (soundOnBtn.isVisible() && soundOnBtn.onTouchRect(event))
                {
                    soundOnBtn.setVisibility(false);
                    soundOffBtn.setVisibility(true);
                    Settings.soundEnabled = false;
                    Settings.saveFiles(game.getFileIO());
                    if (mainTheme != null) {
                        mainTheme.stop();
                        mainTheme.dispose();
                        mainTheme = null;
                    }
                }
                else if (soundOffBtn.isVisible() && soundOffBtn.onTouchRect(event))
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
        g.drawAnimations(deltaTime);

        if (leaderboards.isVisible())
        {
            int y = 300;
            for (int j=0; j <5; j++)
            {
                drawText(g, leaderboardsLines[j], 150,y);
                y +=100;
            }
        }
        mainMenuContainer.draw(g);
        optionsContainer.draw(g);
        charactersContainer.draw(g);
        leaderboardsContainer.draw(g);
    }

    @Override
    public void pause() {
        if(mainTheme!= null && mainTheme.isPlaying()) {
            mainTheme.pause();
        }
    }

    @Override
    public void resume() {
        if (mainTheme != null && Settings.soundEnabled){
            mainTheme.play();}
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
            if (character == '.')
            {
                srcX = 440;
            }
            g.drawPixmap(numbersPixmap, x, y, srcX, 0, 44, 58);
            x += 44;
        }
    }

}
