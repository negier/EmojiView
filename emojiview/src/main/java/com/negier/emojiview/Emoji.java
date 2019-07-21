package com.negier.emojiview;

import android.support.annotation.DrawableRes;

public class Emoji {
    private @DrawableRes int resId;
    private String key;

    public Emoji(int resId, String key) {
        this.resId = resId;
        this.key = key;
    }

    public int getResId() {
        return resId;
    }

    public String getKey() {
        return key;
    }
}
