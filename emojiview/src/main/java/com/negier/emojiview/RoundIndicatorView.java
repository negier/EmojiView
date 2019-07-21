package com.negier.emojiview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RoundIndicatorView extends FrameLayout implements IIndicatorView{
    private Context context;
    private int indicatorDistance;
    private ImageView mRoundIndicator;
    private LinearLayout mRoundIndicatorBg;

    public RoundIndicatorView( @NonNull Context context) {
        this(context,null);
    }

    public RoundIndicatorView(@NonNull Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundIndicatorView( @NonNull Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        indicatorDistance = dp2px(8);

        View view = LayoutInflater.from(context).inflate(R.layout.round_indicator_view, this, true);

        mRoundIndicator = view.findViewById(R.id.round_indicator);
        mRoundIndicatorBg = view.findViewById(R.id.round_indicator_bg);
    }

    private int dp2px(float dp){
        float density=context.getResources().getDisplayMetrics().density;
        int px=(int) (dp*density+0.5f);
        return px;
    }

    @Override
    public void setIndicators(int size) {
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.shape_round_indicator_bg);
            mRoundIndicatorBg.addView(imageView);
            if (i != 0){
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.leftMargin = indicatorDistance;
                imageView.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mRoundIndicator.setTranslationX((mRoundIndicator.getWidth()+indicatorDistance)*position +
                Math.round((mRoundIndicator.getWidth()+indicatorDistance)*positionOffset));
    }
}
