package com.filip.androidgames.framework.impl;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.codex.shootingstars.R;
import com.filip.androidgames.framework.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public abstract class AndroidGame extends BaseGameActivity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    AdView adView;
    InterstitialAd interstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 1280 : 800;
        int frameBufferHeight = isLandscape ? 800 : 1280;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
        
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        
        // determine the scale based on our framebuffer and our display sizes
        float scaleX = (float) frameBufferWidth / size.x;
        float scaleY = (float) frameBufferHeight / size.y;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);

        MobileAds.initialize(this, "ca-app-pub-5402971235820519~4289993839");

        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.SMART_BANNER);

        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.addView(renderView);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainLayout.addView(adView, adParams);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("B57D33CB645F80B0C9B0A4DA157C3113").build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder()
                        .addTestDevice("B57D33CB645F80B0C9B0A4DA157C3113").build());
            }
        });

        screen = getStartScreen();
        setContentView(mainLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen is null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public boolean isSignedIn() {
        return getGameHelper().isSignedIn();
    }

    @Override
    public void signIn() {
        getGameHelper().beginUserInitiatedSignIn();
    }

    @Override
    public void submitScore(int score) {
        Games.Leaderboards.submitScore(getGameHelper().getApiClient(),
                getString(R.string.leaderboard_top_score),
                score);
    }

    @Override
    public void showLeaderboard() {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                getString(R.string.leaderboard_top_score)), 100);
    }

    @Override
    public void showAchievements() {
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 200);
    }

    @Override
    public void unlockAchievement(int resourceID) {
        if (isSignedIn())
            Games.Achievements.unlock(getApiClient(), getString(resourceID));
    }

    @Override
    public void showBanner() {
        this.runOnUiThread(() -> {
            adView.setVisibility(View.VISIBLE);
//            adView.loadAd(new AdRequest.Builder().build());
            adView.loadAd(new AdRequest.Builder()
                    .addTestDevice("B57D33CB645F80B0C9B0A4DA157C3113").build());
        });
    }

    @Override
    public void hideBanner() {
        this.runOnUiThread(() -> {
           adView.setVisibility(View.GONE);
        });
    }

    @Override
    public void showInterstitialAd() {
        this.runOnUiThread(() -> {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        });
    }
}