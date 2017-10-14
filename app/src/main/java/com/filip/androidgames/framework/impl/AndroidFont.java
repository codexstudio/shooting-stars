package com.filip.androidgames.framework.impl;

import android.graphics.Typeface;
import com.filip.androidgames.framework.Font;

public class AndroidFont implements Font {
    float size;
    int color;
    Typeface typeface;

    public AndroidFont(float size, Typeface typeface, int color) {
        this.size = size;
        this.typeface = typeface;
        this.color = color;
    }

    @Override
    public float getSize() {
        return size;
    }

@Override
    public int getColor() { return color; }
}
