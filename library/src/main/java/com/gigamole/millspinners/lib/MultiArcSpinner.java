/*
 * Copyright (C) 2015 Basil Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigamole.millspinners.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 30.03.2015.
 */
public class MultiArcSpinner extends FrameLayout {

    private ArcView[] arcViews = new ArcView[8];

    private int[] colors = new int[8];

    private Animation tempAnimation;

    private int marginCircle;
    private int marginCircleCounter;

    private int angleCircle = 45;
    private int angleCircleCounter = 155;

    private int strokeWidth;
    private int radius;
    private int diameter;
    private int speed;
    private int halfSpeed;

    private boolean isAdded;
    private boolean isRounded;
    private boolean isSlowdowned;
    private boolean isAutostarted;

    public void setColors(int[] colors, boolean isReverse) {
        this.colors = colors;

        if (isReverse) {
            reverseColors();
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setRounded(boolean isRounded) {
        this.isRounded = isRounded;
    }

    public void setSlowdowned(boolean isSlowdowned) {
        this.isSlowdowned = isSlowdowned;
    }

    public void setAutostarted(boolean isAutostarted) {
        this.isAutostarted = isAutostarted;
    }

    public void start() {
        for(ArcView tempArcView : arcViews) {
            tempArcView.start();
        }
    }

    public void finish() {
        for(ArcView tempArcView : arcViews) {
            tempArcView.finish();
        }
    }

    public MultiArcSpinner(Context context) {
        this(context, null);
    }

    public MultiArcSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiArcSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiArcSpinner);

        try {
            this.speed = typedArray.getInteger(R.styleable.MultiArcSpinner_speed, 2700);
            this.halfSpeed = this.speed / 2;

            this.isRounded = typedArray.getBoolean(R.styleable.MultiArcSpinner_rounded, false);
            this.isSlowdowned = typedArray.getBoolean(R.styleable.MultiArcSpinner_slowdown, true);
            this.isAutostarted = typedArray.getBoolean(R.styleable.MultiArcSpinner_autostart, true);

            final int colorsId = typedArray.getResourceId(R.styleable.MultiArcSpinner_colors, 0);
            if (colorsId != 0) {
                this.colors = getResources().getIntArray(colorsId);
            } else {
                this.colors = getResources().getIntArray(R.array.default_colors);
            }

            if (typedArray.getBoolean(R.styleable.MultiArcSpinner_reverse, false)) {
                reverseColors();
            }
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w < h) {
            this.diameter = w;
        } else {
            this.diameter = h;
        }

        this.radius = this.diameter / 2;

        final int segment = (int) ((this.radius / this.arcViews.length) * 0.9);

        this.marginCircle = segment;
        this.strokeWidth = segment / 2;

        if (!this.isAdded) {
            init();
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        this.isAdded = true;

        final LayoutParams tempArcViewLayoutParams = new LayoutParams(this.diameter, this.diameter);
        tempArcViewLayoutParams.gravity = Gravity.CENTER;

        for (int i = 0; i < this.arcViews.length; i++) {

            final ArcView tempArcView = new ArcView(getContext());
            tempArcView.setColor(this.colors[i]);
            tempArcView.setSpeed(this.speed, this.halfSpeed);
            tempArcView.setStrokeWidth(this.strokeWidth);
            tempArcView.setCircleMargin(this.marginCircleCounter += this.marginCircle);
            tempArcView.setStartAngle(this.angleCircleCounter -= this.angleCircle);

            if (this.isRounded) {
                tempArcView.setRoundCap();
            }

            if (!this.isAutostarted) {
                tempArcView.disableAutostart();
            }

            switch (i) {
                case 0:
                    this.tempAnimation = new RotateAnimation(0, 360 * 6, this.radius, this.radius);
                    break;
                case 1:
                    this.tempAnimation = new RotateAnimation(0, 360 * 5, this.radius, this.radius);
                    break;
                case 2:
                    this.tempAnimation = new RotateAnimation(0, 360 * 4, this.radius, this.radius);
                    break;
                case 3:
                    this.tempAnimation = new RotateAnimation(0, 360 * 3, this.radius, this.radius);
                    break;
                case 4:
                    this.tempAnimation = new RotateAnimation(0, 360 * 2, this.radius, this.radius);
                    break;
                case 5:
                    this.tempAnimation = new RotateAnimation(0, 360, this.radius, this.radius);
                    break;
                case 6:
                    this.tempAnimation = new RotateAnimation(0, -45, this.radius, this.radius);
                    this.tempAnimation.setDuration(halfSpeed);
                    this.tempAnimation.setRepeatMode(Animation.REVERSE);
                    tempArcView.setStartAngle((int) (this.angleCircleCounter + this.angleCircle / 1.75));
                    break;
                case 7:
                    this.tempAnimation = new RotateAnimation(0, -360, this.radius, this.radius);
                    break;
            }

            if (this.isSlowdowned) {
                this.tempAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                this.tempAnimation.setInterpolator(new LinearInterpolator());
            }

            tempArcView.setRotateAnimation(this.tempAnimation);

            tempArcView.setLayoutParams(tempArcViewLayoutParams);
            tempArcView.requestLayout();

            this.arcViews[i] = tempArcView;
            this.addView(tempArcView);
        }
    }

    private void reverseColors() {
        for (int start = 0, end = this.colors.length - 1; start <= end; start++, end--) {
            int aux = this.colors[start];
            this.colors[start] = this.colors[end];
            this.colors[end] = aux;
        }
    }

    private class ArcView extends View {

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Animation anim;

        private int strokeWidth;
        private int circleMargin;
        private int startAngle;
        private int currentAngle;
        private int speed;
        private int halfSpeed;

        public ArcView(Context context) {
            super(context);

            this.setBackgroundColor(Color.TRANSPARENT);

            this.mPaint.setDither(true);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
        }

        private void start() {
            this.isFinishing = false;
            this.isStarting = true;

            setWillNotDraw(false);
            invalidate();
        }

        private void finish() {
            this.isStarting = false;
            this.isFinishing = true;
        }

        protected void onDraw(final Canvas canvas) {
            final int xStart = this.circleMargin;
            final int xEnd = getWidth() - this.circleMargin;

            final float centerX = xStart + ((xEnd - xStart) / 2);

            final double xLen = (xEnd - xStart);
            final float radius = (float) (Math.sqrt(xLen * xLen) / 2);

            final RectF oval = new RectF((float) (centerX - radius),
                    (float) (centerX - radius), (float) (centerX + radius),
                    (float) (centerX + radius));

            canvas.drawArc(oval, this.startAngle, this.currentAngle, false, this.mPaint);
            invalidate();
        }

        public void setRotateAnimation(Animation anim) {
            this.anim = anim;

            this.setDrawingCacheEnabled(true);

            if (this.anim.getRepeatMode() != Animation.REVERSE && this.anim.getDuration() != this.halfSpeed) {
                this.anim.setRepeatMode(Animation.RESTART);
                this.anim.setDuration(this.speed);
            }

            this.anim.setRepeatCount(Animation.INFINITE);
            this.anim.setFillEnabled(true);
            this.anim.setFillAfter(true);

            final AnimationSet animationSet = new AnimationSet(getContext(), null);
            animationSet.addAnimation(this.anim);
            animationSet.addAnimation(new ArcAnimation());

            animationSet.setInterpolator(this.anim.getInterpolator());

            clearAnimation();
            startAnimation(animationSet);

            start();
        }

        public void disableAutostart() {
            setWillNotDraw(true);
        }

        public void setCircleMargin(int circleMargin) {
            this.circleMargin = circleMargin;
        }

        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            this.mPaint.setStrokeWidth(this.strokeWidth);
        }

        public void setStartAngle(int startAngle) {
            this.startAngle = startAngle;
        }

        public void setRoundCap() {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        public void setSpeed(int speed, int halfSpeed) {
            this.speed = speed;
            this.halfSpeed = halfSpeed;
        }

        public void setColor(int color) {
            this.mPaint.setColor(color);
        }

        private boolean isStarting;
        private boolean isFinishing;

        private class ArcAnimation extends Animation {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if (isStarting) {
                    if (ArcView.this.currentAngle != 180) {
                        ArcView.this.currentAngle++;
                    } else {
                        ArcView.this.isStarting = false;
                    }
                }

                if (isFinishing) {
                    if (ArcView.this.currentAngle != 0) {
                        ArcView.this.currentAngle--;
                    } else {
                        ArcView.this.isFinishing = false;
                    }
                }
            }
        }
    }
}
