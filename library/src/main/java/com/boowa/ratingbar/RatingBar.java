package com.boowa.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by boowa on 18/1/15
 */

public class RatingBar extends View {
    private int mStarMargin;
    private int mStarCount;
    private int mStarSize;
    private float mStarMark;

    private Drawable mStarFullDrawable, mStarEmptyDrawable;

    private OnStarChangeListener mOnStarChangeListener;

    private boolean mIntegerMark;

    private boolean mCanMove;

    //star绘制的top,使star始终从中间开始绘制
    private int mStarTop;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mStarMargin = typedArray.getDimensionPixelSize(R.styleable.RatingBar_starMargin, 0);
        mStarSize = typedArray.getDimensionPixelSize(R.styleable.RatingBar_starSize, 20);
        mStarCount = typedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        mStarEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        mStarFullDrawable = typedArray.getDrawable(R.styleable.RatingBar_starFull);

        mIntegerMark = typedArray.getBoolean(R.styleable.RatingBar_integerMark, false);
        mStarMark = typedArray.getFloat(R.styleable.RatingBar_starMark, 0);

        if (mIntegerMark) {
            mStarMark = (float) Math.ceil(mStarMark);
        }

        mCanMove = typedArray.getBoolean(R.styleable.RatingBar_canMove, true);

        typedArray.recycle();


    }


    public void setIntegerMark(boolean integerMark) {
        this.mIntegerMark = integerMark;
    }

    public void setStarMark(float mark) {
        float starMark;
        if (mIntegerMark) {
            starMark = (float) Math.ceil(mark);
        } else {
            starMark = mark;
        }
        if (mStarMark == starMark) {
            return;
        }
        mStarMark = starMark;
        if (this.mOnStarChangeListener != null) {
            this.mOnStarChangeListener.onStarChange(mStarMark);
        }

        invalidate();
    }

    public float getStarMark() {
        return mStarMark;
    }

    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.mOnStarChangeListener = onStarChangeListener;
    }

    public void setStarMargin(int starMargin) {
        mStarMargin = starMargin;
    }

    public void setStarCount(int starCount) {
        mStarCount = starCount;
    }

    public void setStarSize(int starSize) {
        mStarSize = starSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mStarSize * mStarCount + mStarMargin * (mStarCount - 1);
        int height = mStarSize;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStarTop = (h - mStarSize) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStarFullDrawable == null || mStarEmptyDrawable == null) {
            return;
        }


        int top = mStarTop;
        int bottom = mStarSize + mStarTop;
        for (int i = 0; i < mStarCount; i++) {
            int left = i * (mStarMargin + mStarSize);
            int right = left + mStarSize;


            if (mStarMark - 1 >= i) {

                mStarFullDrawable.setBounds(left, top, right, bottom);
                mStarFullDrawable.draw(canvas);

            } else if (mStarMark > i) {

                float border = left + (mStarMark % 1) * mStarSize;

                canvas.save();

                canvas.clipRect(border, top, right, bottom);

                mStarEmptyDrawable.setBounds(left, top, right, bottom);
                mStarEmptyDrawable.draw(canvas);
                canvas.restore();

                canvas.save();

                canvas.clipRect(left, top, border, bottom);

                mStarFullDrawable.setBounds(left, top, right, bottom);
                mStarFullDrawable.draw(canvas);

                canvas.restore();

            } else {

                mStarEmptyDrawable.setBounds(left, top, right, bottom);
                mStarEmptyDrawable.draw(canvas);

            }


        }


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!mCanMove) {
            return false;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        if (x < 0) {
            x = 0;
        }
        if (x > getMeasuredWidth()) {
            x = getMeasuredWidth();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / mStarCount));
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(event);
    }


}


