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

public class GameScreen extends Screen implements GameEventListener {

    //Sprite Resources
//    private static Pixmap background;
    private static Pixmap joystickPixmap;

//    private Point bkgPos;
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

    private CanvasContainer<BaseUIObject> uiContainer;

    private GameObjectsContainer gameObjectsContainer;
    private PlayerView playerView;
    private PlayerContainer playerContainer;

    private SpaceBackground bkg;

    GameScreen(final Game game) {
        super(game);

        Graphics g = game.getGraphics();
        width = g.getWidth();
        height = g.getHeight();

        gameObjectsContainer = new GameObjectsContainer(g);
        playerView = new PlayerView(width, height);
        playerContainer = new PlayerContainer(this);

        bkg = new SpaceBackground(playerView, width, height);

//        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        joystickPixmap = g.newPixmap("virtual-joystick-bkg.png", Graphics.PixmapFormat.ARGB4444);

        joystick = new VirtualJoystick();
//        bkgPos = new Point();
        joystickPos = new Point();
        font = new AndroidFont(96, Typeface.DEFAULT, Color.WHITE);

        uiContainer = new CanvasContainer<>();

//        FriendlyShip starterShip = friendlyPool.newObject();
//        starterShip.transform.setLocation(new Vector2(width / 2, height / 2));
//        starterShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
//        playerContainer.addShip(starterShip);

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

        game.unlockAchievement(R.string.achievement_first_game);
    }

    @Override
    public void update(float deltaTime) {
        uiContainer.update(deltaTime);
        gameObjectsContainer.update(playerView);

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for (TouchEvent event : touchEvents) {
            bIsTouching = joystick.isActiveAndSetDirection(event);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                joystickPos.x = event.x;
                joystickPos.y = event.y;

                if (playPauseBtn.isVisible()) {
                    if (Vector2.distance(new Vector2(event.x, event.y), playPauseBtn.transform.getLocation()) < playPauseBtn.getBoundingRadius()) {
                        pause();
                        pauseResumeBtn.setVisibility(true);
                        paused.setVisibility(true);
                    }
                }
            }

            if (event.type == TouchEvent.TOUCH_UP) {
                if (pauseResumeBtn.isVisible()) {
                    if (Vector2.distance(new Vector2(event.x, event.y), pauseResumeBtn.transform.getLocation()) < pauseResumeBtn.getBoundingRadius()) {
                        pause();
                    }
                }
                if (endPauseBtn.isVisible()) {
                    if (endPauseBtn.getBoundingRect().contains(event.x, event.y)) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
                if (death.isVisible()) {
                    if (Vector2.distance(new Vector2(event.x, event.y), death.transform.getLocation()) < death.getBoundingRadius()) {
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
                Vector2 bkgPos = new Vector2();
                bkgPos.setX(playerView.getLocation().getX() + scrollSpeed * Vector2.projection(joystick.getDirection(), Vector2.RIGHT_VECTOR));
                bkgPos.setY(playerView.getLocation().getY() - scrollSpeed * Vector2.projection(joystick.getDirection(), Vector2.UP_VECTOR));

                playerView.setLocation(bkgPos);
                playerContainer.rotateShips(joystick.getDirection());
                score += 1;
            }
        }

        if (score > 5) {
            game.unlockAchievement(R.string.achievement_five_is_alive);
        }
        if (score > 10) {
            game.unlockAchievement(R.string.achievement_ten_ten);
        }
        if (score > 500) {
            game.unlockAchievement(R.string.achievement_answer_to_everything);
        }
    }


    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(Color.WHITE);

        g.drawPixmap(bkg.getBackground(playerView));

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

        gameObjectsContainer.draw(g);
        uiContainer.draw(g);
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

    }

    @Override
    public void onPlayerRemoved(FriendlyShip fs) {

    }

    private void gameOver() {
        pause();
        restart.setVisibility(true);
        gameOver.setVisibility(true);
        pauseResumeBtn.setVisibility(false);
    }

}