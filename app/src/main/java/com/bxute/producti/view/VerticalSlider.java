/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.bxute.producti.R;

public class VerticalSlider extends View {
  private static final int PROGRESS_STEPS_COUNT = 10;
  int dashedLineHeight;
  private int mSlidedColor;
  private int oneStepProgressHeight;
  private int mLastFinalizedTop = -1;
  private int mSliderCurrentTop;
  private int mParentHeight;
  private int mParentWidth;
  private Paint sliderPaint;
  private Paint rangeLinePaint;
  private Rect mSliderProgressRect;
  private float mStartTouchY;
  private int mMovedDistanceY;
  private SliderProgressListener sliderProgressListener;
  private boolean mRectInitialized;
  private int mLastDispatchedValue;
  private boolean onceDrawn = false;

  public VerticalSlider(Context context) {
    this(context, null);
  }

  public VerticalSlider(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VerticalSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    try {
      TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.VerticalSlider);
      mSlidedColor = ta.getColor(R.styleable.VerticalSlider_progressColor, Color.parseColor("#4CAF50"));
      ta.recycle();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    initialize();
  }

  private void initialize() {
    //for painting rectangle
    sliderPaint = new Paint();
    sliderPaint.setColor(mSlidedColor);
    sliderPaint.setStrokeWidth(4);
    sliderPaint.setAntiAlias(true);
    //for painting dashed-lines
    rangeLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rangeLinePaint.setColor(mSlidedColor);
    rangeLinePaint.setStrokeWidth(2);
    rangeLinePaint.setStyle(Paint.Style.STROKE);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getActionMasked();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mStartTouchY = event.getY();
        mMovedDistanceY = 0;
        break;
      case MotionEvent.ACTION_MOVE:
        mMovedDistanceY = (int) (mStartTouchY - event.getY());
        mSliderCurrentTop = mLastFinalizedTop - mMovedDistanceY;
        mSliderCurrentTop = MathUtils.clamp(mSliderCurrentTop, 0, mParentHeight);
        dispatchSliderValue(mSliderCurrentTop);
        setNewTop(mSliderCurrentTop);
        invalidate();
        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        mLastFinalizedTop = mSliderCurrentTop;
        settleSliderToNearestValue();
        break;
    }
    return true;
  }

  /**
   * calculate the progress and call the callback method.
   *
   * @param mSliderTop top of the slider
   */
  private void dispatchSliderValue(int mSliderTop) {
    int slided = mParentHeight - mSliderTop;
    int segmentNum = slided / oneStepProgressHeight;
    int progress = MathUtils.clamp(segmentNum, 0, PROGRESS_STEPS_COUNT);
    //skip redundant dispatch
    if (mLastDispatchedValue != progress) {
      mLastDispatchedValue = progress;
      if (sliderProgressListener != null) {
        sliderProgressListener.onSliderValueChanged(progress);
      }
    }
  }

  private void setNewTop(int top) {
    mSliderProgressRect.top = MathUtils.clamp(top, 0, mParentHeight);
  }

  private void settleSliderToNearestValue() {
    int tempSegHeight;
    int closestSegment = 0;
    int min = Integer.MAX_VALUE;
    for (int i = 0; i <= PROGRESS_STEPS_COUNT; i++) {
      tempSegHeight = getHeightForSegment(i);
      if (min > Math.abs(tempSegHeight - mLastFinalizedTop)) {
        min = Math.abs(tempSegHeight - mLastFinalizedTop);
        closestSegment = i;
      }
    }
    int targetSliderTop = getHeightForSegment(closestSegment);
    ValueAnimator valueAnimator = ValueAnimator
     .ofInt(mLastFinalizedTop, targetSliderTop);
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
    mLastFinalizedTop = targetSliderTop;
    dispatchSliderValue(mLastFinalizedTop);
  }

  private int getHeightForSegment(int segmentNum) {
    return segmentNum * oneStepProgressHeight;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawRect(mSliderProgressRect, sliderPaint);
    for (int i = 1; i < PROGRESS_STEPS_COUNT; i++) {
      dashedLineHeight = getHeightForSegment(i);
      canvas.drawLine(0, dashedLineHeight, mParentWidth, dashedLineHeight, rangeLinePaint);
    }
    onceDrawn = true;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    mParentWidth = MeasureSpec.getSize(widthMeasureSpec);
    mParentHeight = MeasureSpec.getSize(heightMeasureSpec);
    oneStepProgressHeight = mParentHeight / PROGRESS_STEPS_COUNT;
    if (!mRectInitialized) {
      initializeRect();
      mRectInitialized = true;
    }
    if (!onceDrawn) {
      mLastFinalizedTop = mParentHeight;
      mSliderCurrentTop = mLastFinalizedTop;
      setNewTop(mLastFinalizedTop);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private void initializeRect() {
    mSliderProgressRect = new Rect(0, 0, mParentWidth, mParentHeight);
    mRectInitialized = true;
  }

  public void setSliderProgressListener(SliderProgressListener sliderProgressListener) {
    this.sliderProgressListener = sliderProgressListener;
  }

  public interface SliderProgressListener {
    void onSliderValueChanged(int value);
  }
}
