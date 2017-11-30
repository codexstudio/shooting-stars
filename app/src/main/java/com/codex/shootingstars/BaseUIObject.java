package com.codex.shootingstars;

import com.filip.androidgames.framework.Pixmap;

public abstract class BaseUIObject extends DrawableObject {

    //Members
    public static enum ScreenType {
        None,
        MenuScreen,
        GameScreen,
        PauseScreen,
        DeathScreen,
        LeaderBoardScreen,
        CharacterScreen,
        OptionsScreen
    }

    ;

    ScreenType screenType;

    //Default Constructor
    public BaseUIObject() {
        super();
    }

    //Constructor
    public BaseUIObject(float xLocation, float yLocation, float xScale, float yScale, Pixmap pixmap, ScreenType type) {
        super(xLocation, yLocation, xScale, yScale, pixmap);
        screenType = type;
    }
    //Setter & Getters

    protected ScreenType getType(){return screenType;}
    //Methods
    protected void update()
    {
        super.update();
    }
}
