package com.filip.androidgames.framework.impl;

import android.graphics.Typeface;
import com.filip.androidgames.framework.Font;

public class AndroidFont implements Font {
    float size;
    Typeface typeface;

    AndroidFont(float size, Typeface typeface) {
        this.size = size;
        this.typeface = typeface;
    }

    @Override
    public float getSize() {
        return size;
    }
}
