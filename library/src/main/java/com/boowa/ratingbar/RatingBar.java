package com.boowa.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

    private Bitmap mStarFullBitmap;
    private Drawable mStarEmptyDrawable;

    private OnStarChangeListener mOnStarChangeListener;
    private Paint mPaint;
    private boolean mIntegerMark;

    private boolean mCanMove;

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
        mStarFullBitmap = drawableToBitmap(typedArray.getDrawable(R.styleable.RatingBar_starFull));

        mIntegerMark = typedArray.getBoolean(R.styleable.RatingBar_integerMark, false);
        mStarMark = typedArray.getFloat(R.styleable.RatingBar_starMark, 0);

        if(mIntegerMark){
            mStarMark = (float) Math.ceil(mStarMark);
        }

        mCanMove = typedArray.getBoolean(R.styleable.RatingBar_canMove,true);

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(mStarFullBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }


    public void setIntegerMark(boolean integerMark) {
        this.mIntegerMark = integerMark;
    }

    public void setStarMark(float mark) {
        if (mIntegerMark) {
            mStarMark = (float) Math.ceil(mark);
        } else {
//            mStarMark = Math.round(mark * 10) * 1.0f / 10;
            mStarMark = mark;
        }
        if (this.mOnStarChangeListener != null) {
            this.mOnStarChangeListener.onStarChange(mStarMark);  //调用监听接口
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mStarSize * mStarCount + mStarMargin * (mStarCount - 1), mStarSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStarFullBitmap == null || mStarEmptyDrawable == null) {
            return;
        }
        for (int i = 0; i < mStarCount; i++) {
            mStarEmptyDrawable.setBounds((mStarMargin + mStarSize) * i, 0, (mStarMargin + mStarSize) * i + mStarSize, mStarSize);
            mStarEmptyDrawable.draw(canvas);
        }
        if (mStarMark > 1) {
            canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
//            if (mStarMark - (int) (mStarMark) == 0) {
            if (mStarMark % 1 == 0) {
                for (int i = 1; i < mStarMark; i++) {
                    canvas.translate(mStarMargin + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
                }
            } else {
                for (int i = 1; i < mStarMark - 1; i++) {
                    canvas.translate(mStarMargin + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
                }
                canvas.translate(mStarMargin + mStarSize, 0);
//                canvas.drawRect(0, 0, mStarSize * (Math.round((mStarMark - (int) (mStarMark)) * 10) * 1.0f / 10), mStarSize, mPaint);
                canvas.drawRect(0, 0, mStarSize * (mStarMark % 1), mStarSize, mPaint);
            }
        } else {
            canvas.drawRect(0, 0, mStarSize * mStarMark, mStarSize, mPaint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mCanMove){
            return super.onTouchEvent(event);
        }

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

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(mStarSize, mStarSize, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mStarSize, mStarSize);
        drawable.draw(canvas);
        return bitmap;
    }


}
