package com.negier.emojiview;

import android.support.annotation.IntRange;

public interface IIndicatorView {
    void setIndicators(@IntRange(from = 1) int size);
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
}
