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
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 14.04.2015.
 */
public class CubeSpinner extends FrameLayout {

    private CubeView[] cubeViews = new CubeView[27];

    private int[] colors = new int[3];

    private int speed;
    private int speedTick;
    private int diameter;
    private int radius;

    private float triangleHeight;
    private float cubeDiameter;
    private float cubeRadius;
    private float cubeHalfRadius;

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public CubeSpinner(Context context) {
        this(context, null);
    }

    public CubeSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubeSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CubeSpinner);

        try {
            this.speed = typedArray.getInteger(R.styleable.CubeSpinner_speed, 3400);
            this.speedTick = this.speed / 6;

            final int colorsId = typedArray.getResourceId(R.styleable.CubeSpinner_colors, 0);
            if (colorsId != 0) {
                this.colors = getResources().getIntArray(colorsId);
            } else {
                this.colors = getResources().getIntArray(R.array.wave_colors);
            }
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w < h) {
            this.diameter = w;
        } else {
            this.diameter = h;
        }
        this.radius = this.diameter / 2;

        this.cubeDiameter = this.diameter / 5;
        this.cubeRadius = this.cubeDiameter / 2;
        this.cubeHalfRadius = this.cubeRadius / 2;
        this.triangleHeight = (int) (Math.sqrt(3) * this.cubeRadius / 2);

        this.cubeViews[0] = new CubeView(getContext(), this.cubeDiameter, 0);
        this.cubeViews[1] = new CubeView(getContext(), this.cubeDiameter - this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[2] = new CubeView(getContext(), this.cubeRadius, this.triangleHeight * 2);
        this.cubeViews[3] = new CubeView(getContext(), this.cubeDiameter - this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[4] = new CubeView(getContext(), this.cubeRadius, 0);
        this.cubeViews[5] = new CubeView(getContext(), this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[6] = new CubeView(getContext(), this.cubeRadius, -this.triangleHeight * 2);
        this.cubeViews[7] = new CubeView(getContext(), this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[8] = new CubeView(getContext(), 0, 0);

        this.cubeViews[9] = new CubeView(getContext(), this.cubeRadius, 0);
        this.cubeViews[10] = new CubeView(getContext(), this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[11] = new CubeView(getContext(), 0, this.triangleHeight * 2);
        this.cubeViews[12] = new CubeView(getContext(), this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[13] = new CubeView(getContext(), 0, 0);
        this.cubeViews[14] = new CubeView(getContext(), -this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[15] = new CubeView(getContext(), 0, -this.triangleHeight * 2);
        this.cubeViews[16] = new CubeView(getContext(), -this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[17] = new CubeView(getContext(), -this.cubeRadius, 0);

        this.cubeViews[18] = new CubeView(getContext(), 0, 0);
        this.cubeViews[19] = new CubeView(getContext(), -this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[20] = new CubeView(getContext(), -this.cubeRadius, this.triangleHeight * 2);
        this.cubeViews[21] = new CubeView(getContext(), -this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[22] = new CubeView(getContext(), -this.cubeRadius, 0);
        this.cubeViews[23] = new CubeView(getContext(), -this.cubeDiameter + this.cubeHalfRadius, this.triangleHeight);
        this.cubeViews[24] = new CubeView(getContext(), -this.cubeRadius, -this.triangleHeight * 2);
        this.cubeViews[25] = new CubeView(getContext(), -this.cubeDiameter + this.cubeHalfRadius, -this.triangleHeight);
        this.cubeViews[26] = new CubeView(getContext(), -this.cubeDiameter, 0);

        for (int i = 8; i >= 0; i--) {
            addView(this.cubeViews[i]);
        }

        for (int i = 17; i >= 9; i--) {
            addView(this.cubeViews[i]);
        }

        for (int i = 26; i >= 18; i--) {
            addView(this.cubeViews[i]);
        }

        final Animation mainAnimation = new AlphaAnimation(1.0f, 1.0f);
        mainAnimation.setRepeatCount(Animation.INFINITE);
        mainAnimation.setRepeatMode(Animation.RESTART);
        mainAnimation.setDuration(0);
        mainAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mainAnimation.setDuration(speed);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                doMainAnimation();
            }
        });

        startAnimation(mainAnimation);
    }

    final Handler firstHandler = new Handler();
    final Handler secondHandler = new Handler();
    final Handler thirdHandler = new Handler();
    final Handler fourthHandler = new Handler();
    final Handler fifthHandler = new Handler();

    final Runnable firstRunnable = new Runnable() {
        @Override
        public void run() {
            final Animation animationLeftBottom = new TranslateAnimation(triangleHeight, 0, cubeHalfRadius, cubeHalfRadius * 2);
            animationLeftBottom.setDuration(speedTick);
            animationLeftBottom.setFillAfter(true);
            cubeViews[0].startAnimation(animationLeftBottom);
            cubeViews[9].startAnimation(animationLeftBottom);
            cubeViews[18].startAnimation(animationLeftBottom);

            final Animation animatonLeftCenter = new TranslateAnimation(0, -triangleHeight, 0, cubeHalfRadius);
            animatonLeftCenter.setDuration(speedTick);
            animatonLeftCenter.setFillAfter(true);
            cubeViews[3].startAnimation(animatonLeftCenter);
            cubeViews[12].startAnimation(animatonLeftCenter);
            cubeViews[21].startAnimation(animatonLeftCenter);

            final Animation animationLeftTop = new TranslateAnimation(-triangleHeight, -triangleHeight * 2, -cubeHalfRadius, 0);
            animationLeftTop.setDuration(speedTick);
            animationLeftTop.setFillAfter(true);
            cubeViews[6].startAnimation(animationLeftTop);
            cubeViews[15].startAnimation(animationLeftTop);
            cubeViews[24].startAnimation(animationLeftTop);

            final Animation animationBackRight = new TranslateAnimation(triangleHeight, triangleHeight * 2, cubeHalfRadius, 0);
            animationBackRight.setDuration(speedTick);
            animationBackRight.setFillAfter(true);
            cubeViews[2].startAnimation(animationBackRight);
            cubeViews[11].startAnimation(animationBackRight);
            cubeViews[20].startAnimation(animationBackRight);

            final Animation animationBackCenter = new TranslateAnimation(0, triangleHeight, 0, -cubeHalfRadius);
            animationBackCenter.setDuration(speedTick);
            animationBackCenter.setFillAfter(true);
            cubeViews[5].startAnimation(animationBackCenter);
            cubeViews[14].startAnimation(animationBackCenter);
            cubeViews[23].startAnimation(animationBackCenter);

            final Animation animationBackTop = new TranslateAnimation(-triangleHeight, 0, -cubeHalfRadius, -cubeHalfRadius * 2);
            animationBackTop.setDuration(speedTick);
            animationBackTop.setFillAfter(true);
            cubeViews[8].startAnimation(animationBackTop);
            cubeViews[17].startAnimation(animationBackTop);
            cubeViews[26].startAnimation(animationBackTop);
        }
    };
    final Runnable secondRunnable = new Runnable() {
        @Override
        public void run() {
            final Animation animation_0 = new TranslateAnimation(0, 0, cubeHalfRadius * 2, cubeDiameter);
            animation_0.setDuration(speedTick);
            animation_0.setFillAfter(true);
            cubeViews[0].startAnimation(animation_0);

            final Animation animation_1 = new TranslateAnimation(triangleHeight, triangleHeight, cubeHalfRadius, cubeRadius + cubeHalfRadius);
            animation_1.setDuration(speedTick);
            animation_1.setFillAfter(true);
            cubeViews[1].startAnimation(animation_1);

            final Animation animation_3 = new TranslateAnimation(-triangleHeight, -triangleHeight, cubeHalfRadius, cubeRadius + cubeHalfRadius);
            animation_3.setDuration(speedTick);
            animation_3.setFillAfter(true);
            cubeViews[3].startAnimation(animation_3);

            final Animation animation_2 = new TranslateAnimation(triangleHeight * 2, triangleHeight * 2, 0, cubeRadius);
            animation_2.setDuration(speedTick);
            animation_2.setFillAfter(true);
            cubeViews[2].startAnimation(animation_2);

            final Animation animation_6 = new TranslateAnimation(-triangleHeight * 2, -triangleHeight * 2, 0, cubeRadius);
            animation_6.setDuration(speedTick);
            animation_6.setFillAfter(true);
            cubeViews[6].startAnimation(animation_6);

            final Animation animation_26 = new TranslateAnimation(0, 0, -cubeHalfRadius * 2, -cubeDiameter);
            animation_26.setDuration(speedTick);
            animation_26.setFillAfter(true);
            cubeViews[26].startAnimation(animation_26);

            final Animation animation_23 = new TranslateAnimation(triangleHeight, triangleHeight, -cubeHalfRadius, -cubeRadius - cubeHalfRadius);
            animation_23.setDuration(speedTick);
            animation_23.setFillAfter(true);
            cubeViews[23].startAnimation(animation_23);

            final Animation animation_25 = new TranslateAnimation(-triangleHeight, -triangleHeight, -cubeHalfRadius, -cubeRadius - cubeHalfRadius);
            animation_25.setDuration(speedTick);
            animation_25.setFillAfter(true);
            cubeViews[25].startAnimation(animation_25);

            final Animation animation_20 = new TranslateAnimation(triangleHeight * 2, triangleHeight * 2, 0, -cubeRadius);
            animation_20.setDuration(speedTick);
            animation_20.setFillAfter(true);
            cubeViews[20].startAnimation(animation_20);

            final Animation animation_24 = new TranslateAnimation(-triangleHeight * 2, -triangleHeight * 2, 0, -cubeRadius);
            animation_24.setDuration(speedTick);
            animation_24.setFillAfter(true);
            cubeViews[24].startAnimation(animation_24);

            final Animation animation_19 = new TranslateAnimation(triangleHeight, triangleHeight, cubeHalfRadius, -cubeHalfRadius);
            animation_19.setDuration(speedTick);
            animation_19.setFillAfter(true);
            cubeViews[19].startAnimation(animation_19);

            final Animation animation_21 = new TranslateAnimation(-triangleHeight, -triangleHeight, cubeHalfRadius, -cubeHalfRadius);
            animation_21.setDuration(speedTick);
            animation_21.setFillAfter(true);
            cubeViews[21].startAnimation(animation_21);

            final Animation animation_22 = new TranslateAnimation(0, 0, 0, -cubeRadius);
            animation_22.setDuration(speedTick);
            animation_22.setFillAfter(true);
            cubeViews[22].startAnimation(animation_22);

            final Animation animation_18 = new TranslateAnimation(0, 0, cubeRadius, 0);
            animation_18.setDuration(speedTick);
            animation_18.setFillAfter(true);
            cubeViews[18].startAnimation(animation_18);

            final Animation animation_5 = new TranslateAnimation(triangleHeight, triangleHeight, 0, cubeHalfRadius);
            animation_5.setDuration(speedTick);
            animation_5.setFillAfter(true);
            cubeViews[5].startAnimation(animation_5);

            final Animation animation_7 = new TranslateAnimation(-triangleHeight, -triangleHeight, 0, cubeHalfRadius);
            animation_7.setDuration(speedTick);
            animation_7.setFillAfter(true);
            cubeViews[7].startAnimation(animation_7);

            final Animation animation_8 = new TranslateAnimation(0, 0, 0, cubeDiameter);
            animation_8.setDuration(speedTick);
            animation_8.setFillAfter(true);
            cubeViews[8].startAnimation(animation_8);

            final Animation animation_4 = new TranslateAnimation(0, 0, 0, cubeRadius);
            animation_4.setDuration(speedTick);
            animation_4.setFillAfter(true);
            cubeViews[4].startAnimation(animation_4);
        }
    };
    final Runnable thirdRunnable = new Runnable() {
        @Override
        public void run() {
            final Animation animation_0 = new TranslateAnimation(0, -triangleHeight, cubeDiameter, cubeDiameter - cubeHalfRadius);
            animation_0.setDuration(speedTick);
            animation_0.setFillAfter(true);
            cubeViews[0].startAnimation(animation_0);

            final Animation animation_9 = new TranslateAnimation(0, -triangleHeight, cubeRadius, cubeRadius - cubeHalfRadius);
            animation_9.setDuration(speedTick);
            animation_9.setFillAfter(true);
            cubeViews[9].startAnimation(animation_9);

            final Animation animation_18 = new TranslateAnimation(0, -triangleHeight, 0, -cubeHalfRadius);
            animation_18.setDuration(speedTick);
            animation_18.setFillAfter(true);
            cubeViews[18].startAnimation(animation_18);

            final Animation animation_1 = new TranslateAnimation(triangleHeight, 0, cubeDiameter - cubeHalfRadius, cubeRadius);
            animation_1.setDuration(speedTick);
            animation_1.setFillAfter(true);
            cubeViews[1].startAnimation(animation_1);

            final Animation animation_10 = new TranslateAnimation(triangleHeight, 0, cubeHalfRadius, 0);
            animation_10.setDuration(speedTick);
            animation_10.setFillAfter(true);
            cubeViews[10].startAnimation(animation_10);

            final Animation animation_19 = new TranslateAnimation(triangleHeight, 0, -cubeHalfRadius, -cubeRadius);
            animation_19.setDuration(speedTick);
            animation_19.setFillAfter(true);
            cubeViews[19].startAnimation(animation_19);

            final Animation animation_2 = new TranslateAnimation(triangleHeight * 2, triangleHeight, cubeRadius, cubeHalfRadius);
            animation_2.setDuration(speedTick);
            animation_2.setFillAfter(true);
            cubeViews[2].startAnimation(animation_2);

            final Animation animation_11 = new TranslateAnimation(triangleHeight * 2, triangleHeight, 0, -cubeHalfRadius);
            animation_11.setDuration(speedTick);
            animation_11.setFillAfter(true);
            cubeViews[11].startAnimation(animation_11);

            final Animation animation_20 = new TranslateAnimation(triangleHeight * 2, triangleHeight, -cubeRadius, -cubeRadius - cubeHalfRadius);
            animation_20.setDuration(speedTick);
            animation_20.setFillAfter(true);
            cubeViews[20].startAnimation(animation_20);

            final Animation animation_6 = new TranslateAnimation(-triangleHeight * 2, -triangleHeight, cubeRadius, cubeRadius + cubeHalfRadius);
            animation_6.setDuration(speedTick);
            animation_6.setFillAfter(true);
            cubeViews[6].startAnimation(animation_6);

            final Animation animation_15 = new TranslateAnimation(-triangleHeight * 2, -triangleHeight, 0, cubeHalfRadius);
            animation_15.setDuration(speedTick);
            animation_15.setFillAfter(true);
            cubeViews[15].startAnimation(animation_15);

            final Animation animation_24 = new TranslateAnimation(-triangleHeight * 2, -triangleHeight, -cubeRadius, -cubeHalfRadius);
            animation_24.setDuration(speedTick);
            animation_24.setFillAfter(true);
            cubeViews[24].startAnimation(animation_24);

            final Animation animation_7 = new TranslateAnimation(-triangleHeight, 0, cubeHalfRadius, cubeRadius);
            animation_7.setDuration(speedTick);
            animation_7.setFillAfter(true);
            cubeViews[7].startAnimation(animation_7);

            final Animation animation_16 = new TranslateAnimation(-triangleHeight, 0, -cubeHalfRadius, 0);
            animation_16.setDuration(speedTick);
            animation_16.setFillAfter(true);
            cubeViews[16].startAnimation(animation_16);

            final Animation animation_25 = new TranslateAnimation(-triangleHeight, 0, -cubeDiameter + cubeHalfRadius, -cubeRadius);
            animation_25.setDuration(speedTick);
            animation_25.setFillAfter(true);
            cubeViews[25].startAnimation(animation_25);

            final Animation animation_8 = new TranslateAnimation(0, triangleHeight, 0, cubeHalfRadius);
            animation_8.setDuration(speedTick);
            animation_8.setFillAfter(true);
            cubeViews[8].startAnimation(animation_8);

            final Animation animation_17 = new TranslateAnimation(0, triangleHeight, -cubeRadius, -cubeRadius + cubeHalfRadius);
            animation_17.setDuration(speedTick);
            animation_17.setFillAfter(true);
            cubeViews[17].startAnimation(animation_17);

            final Animation animation_26 = new TranslateAnimation(0, triangleHeight, -cubeDiameter, -cubeDiameter + cubeHalfRadius);
            animation_26.setDuration(speedTick);
            animation_26.setFillAfter(true);
            cubeViews[26].startAnimation(animation_26);
        }
    };
    final Runnable fourthRunnable = new Runnable() {
        @Override
        public void run() {
            final Animation animationLeftBottom = new TranslateAnimation(-triangleHeight, 0, cubeRadius + cubeHalfRadius, cubeRadius);
            animationLeftBottom.setDuration(speedTick);
            animationLeftBottom.setFillAfter(true);
            cubeViews[0].startAnimation(animationLeftBottom);
            cubeViews[3].startAnimation(animationLeftBottom);
            cubeViews[6].startAnimation(animationLeftBottom);

            final Animation animationLeftCenter = new TranslateAnimation(-triangleHeight, 0, cubeHalfRadius, 0);
            animationLeftCenter.setDuration(speedTick);
            animationLeftCenter.setFillAfter(true);
            cubeViews[9].startAnimation(animationLeftCenter);
            cubeViews[12].startAnimation(animationLeftCenter);
            cubeViews[15].startAnimation(animationLeftCenter);

            final Animation animationLeftTop = new TranslateAnimation(-triangleHeight, 0, -cubeHalfRadius, -cubeRadius);
            animationLeftTop.setDuration(speedTick);
            animationLeftTop.setFillAfter(true);
            cubeViews[18].startAnimation(animationLeftTop);
            cubeViews[21].startAnimation(animationLeftTop);
            cubeViews[24].startAnimation(animationLeftTop);

            final Animation animationRightBottom = new TranslateAnimation(triangleHeight, 0, cubeHalfRadius, cubeRadius);
            animationRightBottom.setDuration(speedTick);
            animationRightBottom.setFillAfter(true);
            cubeViews[2].startAnimation(animationRightBottom);
            cubeViews[5].startAnimation(animationRightBottom);
            cubeViews[8].startAnimation(animationRightBottom);

            final Animation animationRightCenter = new TranslateAnimation(triangleHeight, 0, -cubeHalfRadius, 0);
            animationRightCenter.setDuration(speedTick);
            animationRightCenter.setFillAfter(true);
            cubeViews[11].startAnimation(animationRightCenter);
            cubeViews[14].startAnimation(animationRightCenter);
            cubeViews[17].startAnimation(animationRightCenter);

            final Animation animationRightTop = new TranslateAnimation(triangleHeight, 0, -cubeRadius - cubeHalfRadius, -cubeRadius);
            animationRightTop.setDuration(speedTick);
            animationRightTop.setFillAfter(true);
            cubeViews[20].startAnimation(animationRightTop);
            cubeViews[23].startAnimation(animationRightTop);
            cubeViews[26].startAnimation(animationRightTop);
        }
    };
    final Runnable fifthRunnable = new Runnable() {
        @Override
        public void run() {
            final Animation animationBottom = new TranslateAnimation(0, 0, cubeRadius, 0);
            animationBottom.setDuration(speedTick);
            animationBottom.setFillAfter(true);
            cubeViews[0].startAnimation(animationBottom);
            cubeViews[1].startAnimation(animationBottom);
            cubeViews[2].startAnimation(animationBottom);
            cubeViews[3].startAnimation(animationBottom);
            cubeViews[4].startAnimation(animationBottom);
            cubeViews[5].startAnimation(animationBottom);
            cubeViews[6].startAnimation(animationBottom);
            cubeViews[7].startAnimation(animationBottom);
            cubeViews[8].startAnimation(animationBottom);

            final Animation animationTop = new TranslateAnimation(0, 0, -cubeRadius, 0);
            animationTop.setDuration(speedTick);
            animationTop.setFillAfter(true);
            cubeViews[18].startAnimation(animationTop);
            cubeViews[19].startAnimation(animationTop);
            cubeViews[20].startAnimation(animationTop);
            cubeViews[21].startAnimation(animationTop);
            cubeViews[22].startAnimation(animationTop);
            cubeViews[23].startAnimation(animationTop);
            cubeViews[24].startAnimation(animationTop);
            cubeViews[25].startAnimation(animationTop);
            cubeViews[26].startAnimation(animationTop);
        }
    };

    private void doMainAnimation() {
        for (CubeView cubeView : this.cubeViews) {
            cubeView.clearAnimation();
        }

        this.firstHandler.removeCallbacks(this.firstRunnable);
        this.secondHandler.removeCallbacks(this.secondRunnable);
        this.thirdHandler.removeCallbacks(this.thirdRunnable);
        this.fourthHandler.removeCallbacks(this.fourthRunnable);
        this.fifthHandler.removeCallbacks(this.fifthRunnable);

        final Animation animationBack = new TranslateAnimation(0, this.triangleHeight, 0, this.cubeHalfRadius);
        animationBack.setDuration(this.speedTick);
        animationBack.setFillAfter(true);
        this.cubeViews[0].startAnimation(animationBack);
        this.cubeViews[1].startAnimation(animationBack);
        this.cubeViews[2].startAnimation(animationBack);
        this.cubeViews[9].startAnimation(animationBack);
        this.cubeViews[10].startAnimation(animationBack);
        this.cubeViews[11].startAnimation(animationBack);
        this.cubeViews[18].startAnimation(animationBack);
        this.cubeViews[19].startAnimation(animationBack);
        this.cubeViews[20].startAnimation(animationBack);

        final Animation animationFront = new TranslateAnimation(0, -this.triangleHeight, 0, -this.cubeHalfRadius);
        animationFront.setDuration(this.speedTick);
        animationFront.setFillAfter(true);
        this.cubeViews[6].startAnimation(animationFront);
        this.cubeViews[7].startAnimation(animationFront);
        this.cubeViews[8].startAnimation(animationFront);
        this.cubeViews[15].startAnimation(animationFront);
        this.cubeViews[16].startAnimation(animationFront);
        this.cubeViews[17].startAnimation(animationFront);
        this.cubeViews[24].startAnimation(animationFront);
        this.cubeViews[25].startAnimation(animationFront);
        this.cubeViews[26].startAnimation(animationFront);

        firstHandler.postDelayed(this.firstRunnable, this.speedTick);
        secondHandler.postDelayed(this.secondRunnable, this.speedTick * 2);
        thirdHandler.postDelayed(this.thirdRunnable, this.speedTick * 3);
        fourthHandler.postDelayed(this.fourthRunnable, this.speedTick * 4);
        fifthHandler.postDelayed(this.fifthRunnable, this.speedTick * 5);
    }

    private class CubeView extends View {

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setAntiAlias(true);
                setStyle(Style.FILL_AND_STROKE);
            }
        };

        private Path leftSide = new Path();
        private Path rightSide = new Path();
        private Path topSide = new Path();

        private float topMargin;
        private float sideMargin;

        public CubeView(Context context, float topMargin, float sideMargin) {
            super(context);

            this.topMargin = topMargin;
            this.sideMargin = sideMargin;

            final LayoutParams params = new LayoutParams(diameter, diameter);
            params.gravity = Gravity.CENTER;
            setLayoutParams(params);
            requestLayout();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.translate(radius - cubeRadius, radius - cubeRadius);

            this.rightSide.moveTo(sideMargin + cubeRadius, topMargin + cubeRadius);
            this.rightSide.lineTo(sideMargin + cubeRadius + triangleHeight, topMargin + cubeRadius - cubeHalfRadius);
            this.rightSide.lineTo(sideMargin + cubeRadius + triangleHeight, topMargin + cubeRadius + cubeHalfRadius);
            this.rightSide.lineTo(sideMargin + cubeRadius, topMargin + cubeDiameter);
            this.rightSide.moveTo(sideMargin + cubeRadius, topMargin + cubeRadius);
            this.paint.setStrokeWidth(1.5f);
            this.paint.setColor(colors[0]);
            canvas.drawPath(this.rightSide, this.paint);

            this.leftSide.moveTo(sideMargin + cubeRadius, topMargin + cubeDiameter);
            this.leftSide.lineTo(sideMargin + cubeRadius - triangleHeight, topMargin + cubeRadius + cubeHalfRadius);
            this.leftSide.lineTo(sideMargin + cubeRadius - triangleHeight, topMargin + cubeRadius - cubeHalfRadius);
            this.leftSide.lineTo(sideMargin + cubeRadius, topMargin + cubeRadius);
            this.leftSide.moveTo(sideMargin + cubeRadius, topMargin + cubeDiameter);
            this.paint.setStrokeWidth(1.5f);
            this.paint.setColor(colors[1]);
            canvas.drawPath(this.leftSide, this.paint);


            this.topSide.moveTo(sideMargin + cubeRadius, topMargin + cubeRadius);
            this.topSide.lineTo(sideMargin + cubeRadius - triangleHeight, topMargin + cubeRadius - cubeHalfRadius);
            this.topSide.lineTo(sideMargin + cubeRadius, topMargin);
            this.topSide.lineTo(sideMargin + cubeRadius + triangleHeight, topMargin + cubeRadius - cubeHalfRadius);
            this.topSide.moveTo(sideMargin + cubeRadius, topMargin + cubeRadius);
            this.paint.setStrokeWidth(0.5f);
            this.paint.setColor(colors[2]);
            canvas.drawPath(this.topSide, this.paint);
        }

        public void setColor(int color) {
            this.paint.setColor(color);
        }
    }
}