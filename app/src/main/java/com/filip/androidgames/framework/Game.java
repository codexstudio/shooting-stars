package com.filip.androidgames.framework;

public interface Game 
{
    Input getInput();
    FileIO getFileIO();
    Graphics getGraphics();
    Audio getAudio();
    void setScreen(Screen screen);
    Screen getCurrentScreen();
    Screen getStartScreen();

    boolean isSignedIn();
    void signIn();
    void submitScore(int score);
    void showLeaderboard();
    void showAchievements();
    void unlockAchievement(int resourceID);

    void showBanner();
    void hideBanner();

    void showInterstitialAd();
}

