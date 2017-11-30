package com.codex.shootingstars;

import android.graphics.Color;
import android.graphics.Typeface;
import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.impl.AndroidFont;
import com.filip.androidgames.framework.types.Vector2;

import java.util.List;

public class MainMenuScreen extends Screen {
    private static Pixmap background;
    private Pixmap explosion;
    private Font font;

    Button playGameMenubtn;
    Button optionsMenubtn;
    Button lbMenubtn;
    Button charMenubtn;
    Button backBtn;

    CanvasContainer<BaseUIObject> uiContainer;

    public MainMenuScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        //explosion = g.newPixmap("explosion.png", Graphics.PixmapFormat.ARGB8888);
        //g.addAnimation(explosion, 300, 300, 6, 8, 256, 256, true);
        font = new AndroidFont(48, Typeface.DEFAULT, Color.WHITE);


        uiContainer = new CanvasContainer<BaseUIObject>();


        playGameMenubtn = new Button(g.getWidth()/2, g.getHeight()*2/5, 1.0f, 1.0f, g.newPixmap("Play_Game.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.MenuScreen);
        optionsMenubtn = new Button(g.getWidth()/2, g.getHeight()*2.5f/5, 1.0f, 1.0f, g.newPixmap("Options.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.MenuScreen);
        lbMenubtn = new Button(g.getWidth()/2, g.getHeight()*3/5, 1.0f, 1.0f, g.newPixmap("Leaderboards.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.MenuScreen);
        charMenubtn = new Button(g.getWidth()/2, g.getHeight()*3.5f/5, 1.0f, 1.0f, g.newPixmap("Characters.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.MenuScreen);
        backBtn = new Button(g.getWidth()-200, g.getHeight()-100, 1.0f, 1.0f, g.newPixmap("Back.png",Graphics.PixmapFormat.ARGB8888) , Button.ScreenType.None);

        backBtn.setVisibility(false);

        uiContainer.add(playGameMenubtn);
        uiContainer.add(optionsMenubtn);
        uiContainer.add(lbMenubtn);
        uiContainer.add(charMenubtn);
        uiContainer.add(backBtn);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (playGameMenubtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), playGameMenubtn.transform.getLocation()) < playGameMenubtn.getBoundingRadius())
                    {
                        game.setScreen(new GameScreen(game));
                    }
                }
                if (optionsMenubtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), optionsMenubtn.transform.getLocation()) < optionsMenubtn.getBoundingRadius())
                    {
                        playGameMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                    }
                }
                if (lbMenubtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), lbMenubtn.transform.getLocation()) < lbMenubtn.getBoundingRadius())
                    {
                        playGameMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                    }
                }
                if (charMenubtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), charMenubtn.transform.getLocation()) < charMenubtn.getBoundingRadius())
                    {
                        playGameMenubtn.setVisibility(false);
                        lbMenubtn.setVisibility(false);
                        charMenubtn.setVisibility(false);
                        optionsMenubtn.setVisibility(false);
                        backBtn.setVisibility(true);
                    }
                }
                if (backBtn.getVisibility() == true){
                    if (Vector2.Distance(new Vector2(event.x, event.y), backBtn.transform.getLocation()) < backBtn.getBoundingRadius())
                    {
                        playGameMenubtn.setVisibility(true);
                        lbMenubtn.setVisibility(true);
                        optionsMenubtn.setVisibility(true);
                        charMenubtn.setVisibility(true);
                        backBtn.setVisibility(false);
                    }
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
