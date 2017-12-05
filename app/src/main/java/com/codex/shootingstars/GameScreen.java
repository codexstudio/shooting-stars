package com.codex.shootingstars;

import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Freezable;
import android.util.Log;
import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Pool.PoolObjectFactory;
import com.filip.androidgames.framework.Graphics.Point;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.impl.AndroidFont;
import com.filip.androidgames.framework.impl.AndroidGame;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen extends Screen implements GameEventListener {
    //Sprite Resources
    private static Pixmap background;
    private static Pixmap joystickPixmap;
    private Pixmap friendlyShipPixmap;
    private Pixmap enemyShipPixmap;
    private Pixmap asteroidPixmap;

    private Point bkgPos;
    private Point joystickPos;

    private int width;
    private int height;

    private boolean isPaused = false;

    private int score = 0;

    private boolean bIsTouching;
    private VirtualJoystick joystick;

    private Button playPauseBtn;
    private Button pauseResumeBtn;
    private Button endPauseBtn;
    private Button death;
    private Button restart;
    private Button options;

    private StaticUI playScore;
    private StaticUI gameOver;
    private StaticUI paused;

    private Font font;

    private PlayerContainer playerContainer;

    private CanvasContainer<BaseCharacter> gameContainer;
    private CanvasContainer<BaseUIObject> uiContainer;

    private Pool<FriendlyShip> friendlyPool;
    private Pool<EnemyShip> enemyPool;
    private Pool<Asteroid> asteroidPool;


    GameScreen(final Game game) {
        super(game);
        Graphics g = game.getGraphics();

        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        joystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        friendlyShipPixmap = g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888);
        enemyShipPixmap = g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888);
        asteroidPixmap = g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888);

        joystick = new VirtualJoystick();
        width = g.getWidth();
        height = g.getHeight();
        bkgPos = new Point();
        joystickPos = new Point();
        font = new AndroidFont(96, Typeface.DEFAULT, Color.WHITE);

        gameContainer = new CanvasContainer<>();
        uiContainer = new CanvasContainer<>();
        playerContainer = new PlayerContainer(this);

        PoolObjectFactory<FriendlyShip> friendlyPoolFactory;
        PoolObjectFactory<EnemyShip> enemyPoolFactory;
        PoolObjectFactory<Asteroid> asteroidPoolFactory;

        friendlyPoolFactory = new PoolObjectFactory<FriendlyShip>() {
            @Override
            public FriendlyShip createObject() {
                FriendlyShip temp = new FriendlyShip(friendlyShipPixmap, FriendlyShip.ControllerState.AI_CONTROLLED, -500.0f, -500.0f, 0.25f, 0.25f);
                gameContainer.add(temp);
                return temp;
            }
        };

        enemyPoolFactory = new PoolObjectFactory<EnemyShip>() {
            @Override
            public EnemyShip createObject() {
                EnemyShip temp = new EnemyShip(enemyShipPixmap, -500.0f, -500.0f, 0.5f, 0.5f);
                gameContainer.add(temp);
                return temp;
            }
        };

        asteroidPoolFactory = new PoolObjectFactory<Asteroid>() {
            @Override
            public Asteroid createObject() {
                Asteroid temp = new Asteroid(asteroidPixmap, -500.0f, -500.0f, 0.5f, 0.5f);
                gameContainer.add(temp);
                return temp;
            }
        };

        friendlyPool = new Pool<>(friendlyPoolFactory, 100);
        enemyPool = new Pool<>(enemyPoolFactory, 100);
        asteroidPool = new Pool<>(asteroidPoolFactory, 100);

        FriendlyShip starterShip = friendlyPool.newObject();
        starterShip.transform.setLocation(new Vector2(width / 2, height / 2));
        starterShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(starterShip, false);

        FriendlyShip starterShip2 = friendlyPool.newObject();
        starterShip2.transform.setLocation(new Vector2(width / 4 * 3, height / 2));
        starterShip2.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(starterShip2, false);

        Asteroid testAsteroid = asteroidPool.newObject();
        testAsteroid.transform.setLocation(new Vector2(width / 2, 100.0f));
        //Asteroid testAsteroid2 = asteroidPool.newObject();
        //testAsteroid2.transform.setLocation(new Vector2(width / 4 *3, 250.0f));
        FriendlyShip testAIFrShip = friendlyPool.newObject();
        testAIFrShip.transform.setRotation(new Vector2(0.0f, -1.0f));
        testAIFrShip.transform.setLocation(new Vector2(width / 4 *3, 250.0f));

        //UI containers
        playPauseBtn = new Button(width - 64, 64, 0.28f, 0.28f, g.newPixmap("Pause_Button.png", Graphics.PixmapFormat.ARGB8888));
        pauseResumeBtn = new Button(width / 2, height / 2, 1.0f, 1.0f, g.newPixmap("Resume.png", Graphics.PixmapFormat.ARGB8888));
        endPauseBtn = new Button(width / 2, height * 5 / 6, 1.0f, 1.0f, g.newPixmap("End.png", Graphics.PixmapFormat.ARGB8888));
        death = new Button(width - 64, 192, 0.28f, 0.28f, g.newPixmap("death.png", Graphics.PixmapFormat.ARGB8888));
        restart = new Button(width / 2, height / 2, 1, 1, g.newPixmap("restart.png", Graphics.PixmapFormat.ARGB8888));
        options = new Button(g.getWidth() / 2, g.getHeight() * 3.5f / 5, 1, 1, g.newPixmap("Options.png", Graphics.PixmapFormat.ARGB8888));

        playScore = new StaticUI(102, 36, 1.0f, 1.0f, g.newPixmap("Score.png", Graphics.PixmapFormat.ARGB8888));
        gameOver = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("game_over.png", Graphics.PixmapFormat.ARGB8888));
        paused = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("paused.png", Graphics.PixmapFormat.ARGB8888));


//        uiContainer.add(options, playPauseBtn, pauseResumeBtn, endPauseBtn, playScore, death, restart, gameOver, paused);
        uiContainer.add(options);
        uiContainer.add(playPauseBtn);
        uiContainer.add(pauseResumeBtn);
        uiContainer.add(endPauseBtn);
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

                if (playPauseBtn.isVisible()) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), playPauseBtn.transform.getLocation()) < playPauseBtn.getBoundingRadius()) {
                        pause();
                        pauseResumeBtn.setVisibility(true);
                        paused.setVisibility(true);
                    }
                }
            }

            if (event.type == TouchEvent.TOUCH_UP) {
                if (pauseResumeBtn.isVisible()) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), pauseResumeBtn.transform.getLocation()) < pauseResumeBtn.getBoundingRadius()) {
                        pause();
                    }
                }
                if (endPauseBtn.isVisible()) {
                    if (endPauseBtn.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
                if (death.isVisible()) {
                    if (Vector2.Distance(new Vector2(event.x, event.y), death.transform.getLocation()) < death.getBoundingRadius()) {
                        pause();
                        restart.setVisibility(true);
                        gameOver.setVisibility(true);
                        pauseResumeBtn.setVisibility(false);
                    }
                }
                if (restart.isVisible()) {
                    if (restart.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new GameScreen(game));
                    }
                }
            }
        }

        if (!isPaused) {
            if (bIsTouching) {
                final float scrollSpeed = 0.25f;
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

            //Test Movement
            for (BaseCharacter obj : gameContainer.containerList) {
                if (obj.getClass() == Asteroid.class) {
                    obj.transform.setLocation(new Vector2(obj.transform.getLocation().getX(), obj.transform.getLocation().getY() + 0.7f));
                }
                else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip)obj).getState() == FriendlyShip.ControllerState.AI_CONTROLLED) {
                    obj.transform.setLocation(new Vector2(obj.transform.getLocation().getX(), obj.transform.getLocation().getY() + 0.7f));
                }
            }

            checkOutOfBounds();
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
            endPauseBtn.setVisibility(false);
            options.setVisibility(false);
            gameOver.setVisibility(false);
            pauseResumeBtn.setVisibility(false);
            paused.setVisibility(false);

            g.drawText(String.valueOf(score), 212, 72, font, Color.WHITE);
        } else {
            playPauseBtn.setVisibility(false);
            playScore.setVisibility(false);
            death.setVisibility(false);

            endPauseBtn.setVisibility(true);
            options.setVisibility(true);
        }
        gameContainer.drawContainer(g);
        uiContainer.drawContainer(g);
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
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

    private void checkCollisions() {
        List<FriendlyShip> frList = playerContainer.friendlyShipList;
        List<BaseCharacter> baseChList = gameContainer.containerList;

        for (Iterator<FriendlyShip> frIterator = frList.iterator(); frIterator.hasNext();) {
            FriendlyShip frSp = frIterator.next();
            for (Iterator<BaseCharacter> bsChIterator = baseChList.iterator(); bsChIterator.hasNext();) {
                BaseCharacter obj = bsChIterator.next();
                if (frSp.isCollidingWith(obj)) {
                    if (obj.getClass() == Asteroid.class) {
                        frIterator.remove();
                        bsChIterator.remove();
                        frSp.setToPoolTransform();
                        friendlyPool.free(frSp);
                        if (frList.isEmpty()) {
                            gameOver();
                        }
                    }
                    else if (obj.getClass() == EnemyShip.class) {
                        frIterator.remove();
                        bsChIterator.remove();
                        frSp.setToPoolTransform();
                        friendlyPool.free(frSp);
                        if (frList.isEmpty()) {
                            gameOver();
                        }
                    } else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip) obj).getState() == FriendlyShip.ControllerState.AI_CONTROLLED) {
                        ((FriendlyShip) obj).changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
                        playerContainer.addShip((FriendlyShip) obj, true);
                    }
                }
            }
        }

        playerContainer.friendlyShipList = frList;
        gameContainer.containerList = baseChList;
    }

    private void checkOutOfBounds() {
        List<BaseCharacter> baseChList = gameContainer.containerList;

        for (Iterator<BaseCharacter> bsChIterator = baseChList.iterator(); bsChIterator.hasNext();) {
            BaseCharacter obj = bsChIterator.next();
            final float OUT_OF_BOUNDS_EXTENSION = 5.0f;
            if (obj.transform.getLocation().getX() < -OUT_OF_BOUNDS_EXTENSION ||
                    obj.transform.getLocation().getX() > width + OUT_OF_BOUNDS_EXTENSION ||
                    obj.transform.getLocation().getY() < -OUT_OF_BOUNDS_EXTENSION ||
                    obj.transform.getLocation().getY() > height + OUT_OF_BOUNDS_EXTENSION)
            {
                bsChIterator.remove();
                obj.setToPoolTransform();
                if (obj.getClass() == FriendlyShip.class) {
                    friendlyPool.free((FriendlyShip) obj);
                } else if (obj.getClass() == EnemyShip.class) {
                    enemyPool.free((EnemyShip) obj);
                } else if (obj.getClass() == Asteroid.class) {
                    asteroidPool.free((Asteroid) obj);
                }
            }
        }

        gameContainer.containerList = baseChList;
    }

    private void gameOver() {
        pause();
        restart.setVisibility(true);
        gameOver.setVisibility(true);
        pauseResumeBtn.setVisibility(false);
    }

}