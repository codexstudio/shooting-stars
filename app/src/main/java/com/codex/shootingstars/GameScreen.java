package com.codex.shootingstars;

import android.graphics.Color;
import android.graphics.Typeface;
import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Graphics.Point;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.impl.AndroidFont;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;
import java.util.Random;

public class GameScreen extends Screen implements GameEventListener {
    private static Pixmap background;
    private static Pixmap joystickPixmap;
//    private static Pixmap ship;

    private Point bkgPos;
    private Point joystickPos;
    private float scrollSpeed = 0.25f;

    int width;
    int height;

    boolean isPaused = false;

    //private int oldScore;
    private float score = 0;

    private Random random = new Random();

    private boolean bIsTouching;
    private VirtualJoystick joystick;

    PlayerContainer playerContainer;

    FriendlyShip testShipOne;
    FriendlyShip testShipTwo;
    EnemyShip testEnemyShip;
    Asteroid testAsteroid;

    Button playPauseBtn;
    Button pauseResumeBtn;
    Button endPausebtn;
    Button death;
    Button restart;
    Button options;

    StaticUI playScore;
    StaticUI gameOver;
    StaticUI paused;

    private Font font;

    CanvasContainer<BaseCharacter> gameContainer;
    CanvasContainer<BaseUIObject> uiContainer;

    public GameScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        joystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        joystick = new VirtualJoystick();
        width = g.getWidth();
        height = g.getHeight();
        bkgPos = new Point();
        joystickPos = new Point();
        font = new AndroidFont(96, Typeface.DEFAULT, Color.WHITE);

        gameContainer = new CanvasContainer<BaseCharacter>();
        uiContainer = new CanvasContainer<BaseUIObject>();

        testShipOne = new FriendlyShip(g, FriendlyShip.ControllerState.PLAYER_CONTROLLED, 250.0f, 550.0f, 0.5f, 0.5f);
        testShipTwo = new FriendlyShip(g, FriendlyShip.ControllerState.PLAYER_CONTROLLED, 450.0f, 550.0f, 0.5f, 0.5f);

        playerContainer = new PlayerContainer(this);
        playerContainer.addShip(testShipOne);
        playerContainer.addShip(testShipTwo);

        testEnemyShip = new EnemyShip(g,250.0f, 800.0f, 0.5f, 0.5f);
        testEnemyShip.transform.setRotation(new Vector2(-1, 7));
        testAsteroid = new Asteroid(g,500.0f, 100.0f, 0.5f, 0.5f);

        gameContainer.add(testEnemyShip);
        gameContainer.add(testAsteroid);

        //UI containers
        playPauseBtn = new Button(width - 64, 64, 0.28f, 0.28f, g.newPixmap("Pause_Button.png", Graphics.PixmapFormat.ARGB8888));
        pauseResumeBtn = new Button(width / 2, height / 2, 1.0f, 1.0f, g.newPixmap("Resume.png", Graphics.PixmapFormat.ARGB8888));
        endPausebtn = new Button(width / 2, height * 5 / 6, 1.0f, 1.0f, g.newPixmap("End.png", Graphics.PixmapFormat.ARGB8888));
        death = new Button(width - 64, 192, 0.28f, 0.28f, g.newPixmap("death.png", Graphics.PixmapFormat.ARGB8888));
        restart = new Button(width / 2, height / 2, 1, 1, g.newPixmap("restart.png", Graphics.PixmapFormat.ARGB8888));
        options = new Button(g.getWidth() / 2, g.getHeight() * 3.5f/5, 1, 1, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));

        playScore = new StaticUI(102, 36, 1.0f, 1.0f, g.newPixmap("Score.png", Graphics.PixmapFormat.ARGB8888));
        gameOver = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("game_over.png", Graphics.PixmapFormat.ARGB8888));
        paused = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("paused.png", Graphics.PixmapFormat.ARGB8888));


        uiContainer.add(options);
        uiContainer.add(playPauseBtn);
        uiContainer.add(pauseResumeBtn);
        uiContainer.add(endPausebtn);
        uiContainer.add(playScore);
        uiContainer.add(death);
        uiContainer.add(restart);
        uiContainer.add(gameOver);
        uiContainer.add(paused);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for (TouchEvent event : touchEvents) {
            bIsTouching = joystick.isActiveAndSetDirection(event);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                joystickPos.x = event.x;
                joystickPos.y = event.y;

                if (playPauseBtn.getVisibility() == true) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), playPauseBtn.transform.getLocation()) < playPauseBtn.getBoundingRadius()) {
                        pause();
                        pauseResumeBtn.setVisibility(true);
                        paused.setVisibility(true);
                    }
                }
            }
            if (event.type == TouchEvent.TOUCH_UP) {
                if (pauseResumeBtn.getVisibility() == true) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), pauseResumeBtn.transform.getLocation()) < pauseResumeBtn.getBoundingRadius()) {
                        pause();
                    }
                }
                if (endPausebtn.getVisibility() == true) {
                    if (endPausebtn.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
                if (death.getVisibility() == true) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), death.transform.getLocation()) < death.getBoundingRadius()) {
                        pause();
                        restart.setVisibility(true);
                        gameOver.setVisibility(true);
                        pauseResumeBtn.setVisibility(false);
                    }
                }
                if (restart.getVisibility() == true) {
                    if (restart.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new GameScreen(game));
                    }
                }
            }
        }

        if (!isPaused) {
            if (bIsTouching) {
                bkgPos.x -= scrollSpeed * Vector2.Projection(joystick.getDirection(), Vector2.RIGHT_VECTOR);
                bkgPos.y += scrollSpeed * Vector2.Projection(joystick.getDirection(), Vector2.UP_VECTOR);
                if (bkgPos.x > width || bkgPos.x < -width) {
                    bkgPos.x = 0;
                }
                if (bkgPos.y < -height || bkgPos.y > height) {
                    bkgPos.y = 0;
                }
                playerContainer.rotateShips(joystick.getDirection());
                score += 1;
            }
            checkCollisions();
        }
    }


    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(Color.WHITE);
        //original

        g.drawPixmap(background, bkgPos.x, bkgPos.y);

        if (bkgPos.x != 0 && bkgPos.y != 0) {
            final int bkgYPos = ((bkgPos.y > 0) ? -1 : 1) * background.getHeight();
            final int bkgXPos = ((bkgPos.x > 0) ? -1 : 1) * background.getWidth();

            //up/down screens
            g.drawPixmap(background, bkgPos.x, bkgYPos + bkgPos.y);

            //left/right screens
            g.drawPixmap(background, bkgXPos + bkgPos.x, bkgPos.y);

            //fourth corner screen
            g.drawPixmap(background, bkgXPos + bkgPos.x, bkgYPos + bkgPos.y);
        }
        if (!isPaused) {
            if (bIsTouching) {
                g.drawPixmap(joystickPixmap, new Point(joystickPos.x - 128, joystickPos.y - 128), new Point(256));
            }
            playPauseBtn.setVisibility(true);
            playScore.setVisibility(true);
            death.setVisibility(true);

            restart.setVisibility(false);
            endPausebtn.setVisibility(false);
            options.setVisibility(false);
            gameOver.setVisibility(false);
            pauseResumeBtn.setVisibility(false);
            paused.setVisibility(false);

            g.drawText(String.valueOf(score), 212, 72, font, Color.WHITE);
        } else {
            playPauseBtn.setVisibility(false);
            playScore.setVisibility(false);
            death.setVisibility(false);

            endPausebtn.setVisibility(true);
            options.setVisibility(true);
    }
        gameContainer.drawContainer(g);
        uiContainer.drawContainer(g);
    }

    @Override
    public void pause() {
        if (isPaused) {
            isPaused = false;
        } else {
            isPaused = true;
        }
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void onPlayerAdded(FriendlyShip fs) {
        gameContainer.add(fs);
    }

    @Override
    public void onPlayerRemoved(FriendlyShip fs) {
        gameContainer.remove(fs);
    }

    public void checkCollisions() {
        for (FriendlyShip frSp : playerContainer.friendlyShipList) {
            for (BaseCharacter obj : gameContainer.containerList) {
                if (frSp.isCollidingWith(obj)) {
                    if (obj.getClass() == Asteroid.class) {
                        playerContainer.removeShip(frSp);
                        //frSp.returnToPool();
                        if (playerContainer.getShipListSize() == 0) {
                            loseGame();
                        }
                    } else if (obj.getClass() == EnemyShip.class) {
                        playerContainer.removeShip(frSp);
                        //frSp.returnToPool();
                        if (playerContainer.getShipListSize() == 0) {
                            loseGame();
                        }
                    } else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip) obj).getState() == FriendlyShip.ControllerState.AI_CONTROLLED) {
                        ((FriendlyShip) obj).changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
                        playerContainer.addShip((FriendlyShip) obj);
                    }
                }
            }
        }
    }

    public void loseGame() {
        pause();
        restart.setVisibility(true);
        gameOver.setVisibility(true);
        pauseResumeBtn.setVisibility(false);
    }

}