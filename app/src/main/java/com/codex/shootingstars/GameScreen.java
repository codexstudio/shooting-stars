package com.codex.shootingstars;

import android.graphics.Color;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Graphics.Point;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;

import static com.codex.shootingstars.MainMenuScreen.mainTheme;

//link for the font for blue colours
//http://www.1001fonts.com/unispace-font.html?text=Shooting%20Stars&fg=3d9994

//link for the font with red colours
//http://www.1001fonts.com/unispace-font.html?text=Game%20Over&fg=f02d2f

public class GameScreen extends Screen implements GameEventListener {

    //Sprite Resources
//    private static Pixmap background;
    private static Pixmap joystickPixmap;
    private Pixmap numbersPixmap;
//    private Point bkgPos;
    private Point joystickPos;

    private int width;
    private int height;

    private boolean isPaused = false;
    private boolean isAlive = true;

    private int score = 0;

    private boolean bIsTouching;
    private VirtualJoystick joystick;

    private Button pauseBtn;
    private Button resumeBtn;
    private Button endBtn;
    private Button death;
    private Button restartBtn;

    private StaticUI scoreUI;
    private StaticUI gameOverUI;
    private StaticUI pausedUI;

    private CanvasContainer<BaseUIObject> HUDContainer;
    private CanvasContainer<BaseUIObject> pauseContainer;
    private CanvasContainer<BaseUIObject> deathContainer;


    private GameObjectsContainer gameObjectsContainer;
    private PlayerView playerView;

    private SpaceBackground bkg;

    GameScreen(final Game game) {
        super(game);

        Graphics g = game.getGraphics();
        width = g.getWidth();
        height = g.getHeight();

        playerView = new PlayerView(width, height);
        gameObjectsContainer = new GameObjectsContainer(g, this, playerView);

        bkg = new SpaceBackground(playerView, width, height);

//        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        joystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);
        numbersPixmap = g.newPixmap("numbers.png", Graphics.PixmapFormat.ARGB4444);

        joystick = new VirtualJoystick();
//        bkgPos = new Point();
        joystickPos = new Point();

        HUDContainer = new CanvasContainer<>();
        pauseContainer = new CanvasContainer<>();
        deathContainer = new CanvasContainer<>();

        //UI containers
        pauseBtn = new Button(width - 64, 64, 0.28f, 0.28f, g.newPixmap("Pause_Button.png", Graphics.PixmapFormat.ARGB8888));
        resumeBtn = new Button(width / 2, height / 2, 1.0f, 1.0f, g.newPixmap("Resume.png", Graphics.PixmapFormat.ARGB8888));
        endBtn = new Button(width / 2, height * 5 / 6, 1.0f, 1.0f, g.newPixmap("End.png", Graphics.PixmapFormat.ARGB8888));
        death = new Button(width - 64, 192, 0.28f, 0.28f, g.newPixmap("death.png", Graphics.PixmapFormat.ARGB8888));
        restartBtn = new Button(width / 2, height / 2, 1, 1, g.newPixmap("restart.png", Graphics.PixmapFormat.ARGB8888));

        scoreUI = new StaticUI(123, 29, 1.0f, 1.0f, g.newPixmap("Score.png", Graphics.PixmapFormat.ARGB8888));
        gameOverUI = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("game_over.png", Graphics.PixmapFormat.ARGB8888));
        pausedUI = new StaticUI(g.getWidth() / 2, g.getHeight() * 1.5f / 11, 1, 1, g.newPixmap("paused.png", Graphics.PixmapFormat.ARGB8888));

        game.unlockAchievement(R.string.achievement_first_game);
        HUDContainer.add(pauseBtn, scoreUI, death);
        pauseContainer.add(pausedUI, resumeBtn, endBtn);
        deathContainer.add(gameOverUI, restartBtn, endBtn);
    }

    @Override
    public void update(float deltaTime) {
        gameObjectsContainer.update(playerView, joystick, deltaTime);
        HUDContainer.update(deltaTime);
        pauseContainer.update(deltaTime);
        deathContainer.update(deltaTime);

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for (TouchEvent event : touchEvents) {
            bIsTouching = joystick.isActiveAndSetDirection(event);
            joystickPos.x = event.x;
            joystickPos.y = event.y;

            if (event.type == TouchEvent.TOUCH_UP) {
                if (isPaused) {
                    if (resumeBtn.onTouchCircle(event)) {
                        setPause();
                    }
                    if (endBtn.onTouchRect(event)) {
                        if (Settings.soundEnabled) {
                            if (mainTheme != null) {
                                mainTheme.stop();
                                mainTheme.dispose();
                                mainTheme = null;
                            }
                        }
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
                if (isAlive) {
                    if (death.onTouchCircle(event)) {
                        setPause();
                        gameOver();
                    }
                    if (Vector2.distance(new Vector2(event.x, event.y), pauseBtn.transform.getLocation()) < pauseBtn.getBoundingRadius()) {
                        setPause();
                    }
                }
                if (!isAlive) {
                    if (restartBtn.onTouchRect(event)) {
                        game.setScreen(new GameScreen(game));
                    }
                    if (endBtn.onTouchRect(event)) {
                        if (mainTheme != null) {
                            mainTheme.stop();
                            mainTheme.dispose();
                            mainTheme = null;
                        }
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
            }
        }

        if (!isPaused && isAlive) {
            if (bIsTouching) {
                final float scrollSpeed = 0.05f;
                Vector2 bkgPos = new Vector2();
                bkgPos.setX(playerView.getLocation().getX() + scrollSpeed * Vector2.projection(joystick.getDirection(), Vector2.RIGHT_VECTOR));
                bkgPos.setY(playerView.getLocation().getY() - scrollSpeed * Vector2.projection(joystick.getDirection(), Vector2.UP_VECTOR));

                playerView.setLocation(bkgPos);
                score += 1;
            }
        }

        if (score > 5) {
            game.unlockAchievement(R.string.achievement_five_is_alive);
        }
        if (score > 10) {
            game.unlockAchievement(R.string.achievement_ten_ten);
        }

    }


    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(Color.WHITE);

        g.drawPixmap(bkg.getBackground(playerView));

        if (isAlive) {
            if (!isPaused) {
                if (bIsTouching) {
                    g.drawPixmap(joystickPixmap, new Point(joystickPos.x - 128, joystickPos.y - 128), new Point(256));
                }
                gameObjectsContainer.draw(g);
                HUDContainer.draw(g);
                drawText(g, String.valueOf(score), 256, 0);
            } else {
                pauseContainer.draw(g);
            }
        }
        else{
            deathContainer.draw(g);
        }
    }

    public void setPause()
    {
        if(isPaused)
        {
            isPaused = false;
        }
        else
            isPaused = true;
    }
    @Override
    public void pause() {
        if(mainTheme!= null && mainTheme.isPlaying()) {
            mainTheme.pause();
        }
        setPause();
    }

    @Override
    public void resume() {
        if (mainTheme != null && Settings.soundEnabled){
        mainTheme.play();}
    }

    @Override
    public void dispose() {
    }

    @Override
    public void onPlayerAdded(FriendlyShip fs) {

    }

    @Override
    public void onPlayerRemoved(FriendlyShip fs) {
    }

//    private void checkCollisions() {
//        List<FriendlyShip> frList = playerContainer.friendlyShipList;
//        List<BaseCharacter> baseChList = gameContainer.containerList;
//
//        for (Iterator<FriendlyShip> frIterator = frList.iterator(); frIterator.hasNext();) {
//            FriendlyShip frSp = frIterator.next();
//            for (Iterator<BaseCharacter> bsChIterator = baseChList.iterator(); bsChIterator.hasNext();) {
//                BaseCharacter obj = bsChIterator.next();
//                if (frSp.isCollidingWith(obj)) {
//                    if (obj.getClass() == Asteroid.class) {
//                        frIterator.remove();
//                        bsChIterator.remove();
//                        frSp.setToPoolTransform();
//                        friendlyPool.free(frSp);
//                        if (frList.isEmpty()) {
//                            gameOver();
//                        }
//                    }
//                    else if (obj.getClass() == EnemyShip.class) {
//                        frIterator.remove();
//                        bsChIterator.remove();
//                        frSp.setToPoolTransform();
//                        friendlyPool.free(frSp);
//                        if (frList.isEmpty()) {
//                            gameOver();
//                        }
//                    } else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip) obj).getState() == FriendlyShip.ControllerState.AI_CONTROLLED) {
//                        ((FriendlyShip) obj).changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
//                        playerContainer.addShip((FriendlyShip) obj, true);
//                    }
//                }
//            }
//        }
//
//        playerContainer.friendlyShipList = frList;
//        gameContainer.containerList = baseChList;
//    }
//
//    private void checkOutOfBounds() {
//        List<BaseCharacter> baseChList = gameContainer.containerList;
//
//        for (Iterator<BaseCharacter> bsChIterator = baseChList.iterator(); bsChIterator.hasNext();) {
//            BaseCharacter obj = bsChIterator.next();
//            final float OUT_OF_BOUNDS_EXTENSION = 5.0f;
//            if (obj.transform.getLocation().getX() < -OUT_OF_BOUNDS_EXTENSION ||
//                    obj.transform.getLocation().getX() > width + OUT_OF_BOUNDS_EXTENSION ||
//                    obj.transform.getLocation().getY() < -OUT_OF_BOUNDS_EXTENSION ||
//                    obj.transform.getLocation().getY() > height + OUT_OF_BOUNDS_EXTENSION)
//            {
//                bsChIterator.remove();
//                obj.setToPoolTransform();
//                if (obj.getClass() == FriendlyShip.class) {
//                    friendlyPool.free((FriendlyShip) obj);
//                } else if (obj.getClass() == EnemyShip.class) {
//                    enemyPool.free((EnemyShip) obj);
//                } else if (obj.getClass() == Asteroid.class) {
//                    asteroidPool.free((Asteroid) obj);
//                }
//            }
//        }
//
//        gameContainer.containerList = baseChList;
//    }

    private void gameOver() {
        isAlive = false;
        Settings.addScore(score);
        Settings.saveFiles(game.getFileIO());
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