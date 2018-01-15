package com.boowa.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by boowa on 18/1/15
 */

public class RatingBar extends LinearLayout {
    private int mStarSize, mStarMargin, mStarCount;
    private int mStarRes;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mStarSize = typedArray.getDimensionPixelSize(R.styleable.RatingBar_starSize, 20);
        mStarMargin = typedArray.getDimensionPixelSize(R.styleable.RatingBar_starMargin, 10);
        mStarCount = typedArray.getDimensionPixelSize(R.styleable.RatingBar_starCount, 5);
        mStarRes = typedArray.getResourceId(R.styleable.RatingBar_star, -1);
        typedArray.recycle();

        if (mStarRes != -1) {
            init(context);
        }
    }

    private void init(Context context) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mStarSize,mStarSize);
        for (int i = 0; i < mStarCount; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(mStarRes);
            addView(imageView,layoutParams);

        }


    }


}
