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
    private String score = "0";

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
    Button optionsIcon;

    StaticUI playScore;
    StaticUI options;

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

        gameContainer = new CanvasContainer<BaseCharacter>();
        uiContainer = new CanvasContainer<BaseUIObject>();

        testShipOne = new FriendlyShip(g, FriendlyShip.ControllerStates.PLAYER_CONTROLLED, 250.0f, 550.0f, 0.5f, 0.5f);
        testShipTwo = new FriendlyShip(g, FriendlyShip.ControllerStates.PLAYER_CONTROLLED, 450.0f, 550.0f, 0.5f, 0.5f);

        playerContainer = new PlayerContainer(this);
        playerContainer.addShip(testShipOne);
        playerContainer.addShip(testShipTwo);

        testEnemyShip = new EnemyShip(g,250.0f, 800.0f, 0.5f, 0.5f);
        testEnemyShip.transform.setRotation(new Vector2(-1, 7));
        testAsteroid = new Asteroid(g,500.0f, 100.0f, 0.5f, 0.5f);

        gameContainer.add(testEnemyShip);
        gameContainer.add(testAsteroid);

        //UI containers
        playPauseBtn = new Button(width - 64, 64, 0.28f, 0.28f, g.newPixmap("Pause_Button.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.GameScreen);
        optionsIcon = new Button(width/2, height*2/3, 1, 1, g.newPixmap("Options.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.PauseScreen);
        pauseResumeBtn = new Button(width/2, height/2, 1.0f, 1.0f, g.newPixmap("Resume.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.PauseScreen);
        endPausebtn = new Button(width/2, height*5/6, 1.0f, 1.0f, g.newPixmap("End.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.PauseScreen);

        playScore = new StaticUI(102, 36, 1.0f, 1.0f, g.newPixmap("Score.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.GameScreen);
        options = new StaticUI(g.getWidth()/2, g.getHeight()*1/11, 1, 1, g.newPixmap("Options.png",Graphics.PixmapFormat.ARGB8888) , StaticUI.ScreenType.OptionsScreen);

        uiContainer.add(optionsIcon);
        uiContainer.add(options);
        uiContainer.add(playPauseBtn);
        uiContainer.add(pauseResumeBtn);
        uiContainer.add(endPausebtn);
        uiContainer.add(playScore);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for (TouchEvent event : touchEvents) {
            bIsTouching = joystick.isActiveAndSetDirection(event);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                joystickPos.x = event.x;
                joystickPos.y = event.y;

                if (playPauseBtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), playPauseBtn.transform.getLocation()) < playPauseBtn.getBoundingRadius())
                    {
                        pause();
                        playPauseBtn.setVisibility(false);
                        playScore.setVisibility(false);
                        optionsIcon.setVisibility(true);
                    }
                }
            }

            if (event.type == TouchEvent.TOUCH_UP) {
                if (pauseResumeBtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), pauseResumeBtn.transform.getLocation()) < pauseResumeBtn.getBoundingRadius())
                    {
                        pause();
                        pauseResumeBtn.setVisibility(false);
                        playScore.setVisibility(true);
                        optionsIcon.setVisibility(false);
                        options.setVisibility(false);
                    }
                }
                if (endPausebtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), endPausebtn.transform.getLocation()) < endPausebtn.getBoundingRadius())
                    {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
            }
        }

        if(!isPaused) {
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
        if(!isPaused)
        {
            if (bIsTouching) {
                g.drawPixmap(joystickPixmap, new Point(joystickPos.x - 128, joystickPos.y - 128), new Point(256));
            }
            pauseResumeBtn.setVisibility(false);
            playPauseBtn.setVisibility(true);
            playScore.setVisibility(true);
            endPausebtn.setVisibility(false);
            optionsIcon.setVisibility(false);
            options.setVisibility(false);
        }
        else
        {
            pauseResumeBtn.setVisibility(true);
            playScore.setVisibility(false);
            endPausebtn.setVisibility(true);
            optionsIcon.setVisibility(true);
        }
        gameContainer.drawContainer(g);
        uiContainer.drawContainer(g);
    }

    @Override
    public void pause() {
        if (isPaused) {isPaused = false;}
        else {isPaused = true;}
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

    public void checkCollisions(){
        for (FriendlyShip frSp : playerContainer.friendlyShipList) {
            for (BaseCharacter obj : gameContainer.containerList) {
                if (frSp.isCollidingWith(obj)) {
                    if (obj.getClass() == Asteroid.class) {
                        playerContainer.removeShip(frSp);
                        //frSp.returnToPool();
                        if (playerContainer.getShipListSize() == 0) {
                            loseGame();
                        }
                    }
                    else if (obj.getClass() == EnemyShip.class) {
                        playerContainer.removeShip(frSp);
                        //frSp.returnToPool();
                        if (playerContainer.getShipListSize() == 0) {
                            loseGame();
                        }
                    }
                    else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip)obj).getState() == FriendlyShip.ControllerStates.AI_CONTROLLED) {
                        ((FriendlyShip)obj).changeControllerState(FriendlyShip.ControllerStates.PLAYER_CONTROLLED);
                        playerContainer.addShip((FriendlyShip)obj);
                    }
                }
            }
        }
    }

    public void loseGame() {
        pause();
    }
}


