/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class VerticalSlider extends View {
  private static final int DEFAULT_SEGMENTS = 5;
  //todo: draw ranges with dashed-lines
  int dashedLineHeight;
  private int singleProgressSegmentHeight;
  private int mLastTop;
  private int mSliderTop;
  private int mHeight;
  private int mWidth;
  private Paint sliderPaint;
  private Paint rangeLinePaint;
  private Path mPath;
  private Rect mRect;
  private Context mContext;
  private float touchDownY;
  private int deltaY;
  private SliderProgressListener sliderProgressListener;
  private boolean mRectInitialized;
  private int mLastDispatchedValue;
  private int PEEK_HEIGHT = 24;

  public VerticalSlider(Context context) {
    this(context, null);
  }

  public VerticalSlider(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VerticalSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    mPath = new Path();
    //for painting rectangle
    sliderPaint = new Paint();
    sliderPaint.setColor(Color.parseColor("#4CAF50"));
    sliderPaint.setStrokeWidth(4);
    sliderPaint.setAntiAlias(true);

    //for painting dashed-lines
    rangeLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rangeLinePaint.setColor(Color.parseColor("#4CAF50"));
    rangeLinePaint.setStrokeWidth(2);
    rangeLinePaint.setStyle(Paint.Style.STROKE);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getActionMasked();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        touchDownY = event.getY();
        deltaY = 0;
        break;
      case MotionEvent.ACTION_MOVE:
        deltaY = (int) (touchDownY - event.getY());
        mSliderTop = mLastTop - deltaY;
        dispatchSliderValue(mSliderTop);
        setNewTop(mSliderTop);
        invalidate();
        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        mLastTop = mSliderTop;
        settleSliderToNearestValue();
        break;
    }
    return true;
  }

  private void dispatchSliderValue(int mSliderTop) {
    int slided = mHeight - mSliderTop;
    int segmentNum = slided / singleProgressSegmentHeight;
    int progress = MathUtils.clamp(segmentNum, 1, 5);
    //skip redundant dispatch
    if (mLastDispatchedValue != progress) {
      mLastDispatchedValue = progress;
      if (sliderProgressListener != null) {
        sliderProgressListener.onSliderValueChanged(progress);
      }
    }
  }

  private void setNewTop(int top) {
    mRect.top = MathUtils.clamp(top, 0, mHeight - PEEK_HEIGHT);
  }

  //todo: soft animate the top
  private void settleSliderToNearestValue() {
    int tempSegHeight;
    int closestSegment = 0;
    int min = Integer.MAX_VALUE;
    for (int i = 0; i <= DEFAULT_SEGMENTS; i++) {
      tempSegHeight = getHeightForSegment(i);
      if (min > Math.abs(tempSegHeight - mLastTop)) {
        min = Math.abs(tempSegHeight - mLastTop);
        closestSegment = i;
      }
    }
    int targetSliderTop = getHeightForSegment(closestSegment);
    ValueAnimator valueAnimator = ValueAnimator
     .ofInt(mLastTop, targetSliderTop);
    valueAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
    valueAnimator.setDuration(200);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        setNewTop(value);
        invalidate();
      }
    });
    valueAnimator.start();
    mLastTop = targetSliderTop;
  }

  private int getHeightForSegment(int segmentNum) {
    return segmentNum * singleProgressSegmentHeight;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawRect(mRect, sliderPaint);
    for (int i = 1; i < DEFAULT_SEGMENTS; i++) {
      dashedLineHeight = getHeightForSegment(i);
      canvas.drawLine(0, dashedLineHeight, mWidth, dashedLineHeight, rangeLinePaint);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    mWidth = MeasureSpec.getSize(widthMeasureSpec);
    mHeight = MeasureSpec.getSize(heightMeasureSpec);
    mLastTop = mHeight;
    mSliderTop = mLastTop;
    singleProgressSegmentHeight = mHeight / DEFAULT_SEGMENTS;
    if (!mRectInitialized) {
      initializeRect();
    }
    setNewTop(mSliderTop);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private void initializeRect() {
    mRect = new Rect(0, 0, mWidth, mHeight);
    mRectInitialized = true;
  }

  private void log(String msg) {
    Log.d("VerticalSlider", msg);
  }

  public void setSliderProgressListener(SliderProgressListener sliderProgressListener) {
    this.sliderProgressListener = sliderProgressListener;
  }

  public interface SliderProgressListener {
    void onSliderValueChanged(int value);
  }
}
