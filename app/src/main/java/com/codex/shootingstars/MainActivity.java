package com.codex.shootingstars;

import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new MainMenuScreen(this);
    }
}
