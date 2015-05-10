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
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 13.04.2015.
 */
public class CirclesWaveSpinner extends FrameLayout {

    private CircleView[] circleViews = new CircleView[20];

    private int colorCounter;
    private int[] colors = new int[] {};

    private float marginCircle;
    private float marginCircleCounter;

    private int speed;
    private int halfSpeed;
    private int radius;
    private int diameter;
    private int startOffset;

    private float strokeWidth;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public CirclesWaveSpinner(Context context) {
        this(context, null);
    }

    public CirclesWaveSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclesWaveSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclesWaveSpinner);

        try {
            this.speed = typedArray.getInteger(R.styleable.CirclesWaveSpinner_speed, 3000) / 2;
            this.halfSpeed = this.speed / 2;

            final int colorsId = typedArray.getResourceId(R.styleable.CirclesWaveSpinner_colors, 0);
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
        if (w < h) {
            this.diameter = w;
        } else {
            this.diameter = h;
        }

        this.radius = this.diameter / 2;

        final float segment = this.radius / this.circleViews.length * 0.95f;
        this.marginCircle = segment;
        this.strokeWidth = segment;

        this.startOffset = this.speed / this.circleViews.length;

        init();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        final LayoutParams tempArcViewLayoutParams = new LayoutParams(this.diameter, this.diameter);
        tempArcViewLayoutParams.gravity = Gravity.CENTER;

        for (int i = 0; i < this.circleViews.length; i++) {
            final CircleView tempCircleView = new CircleView(getContext());
            tempCircleView.setColor(this.colors[colorCounter++]);
            if (this.colorCounter == 3) {
                this.colorCounter = 0;
            }

            tempCircleView.setStartOffset((this.circleViews.length - 1 - i) * this.startOffset);
            tempCircleView.setStrokeWidth(this.strokeWidth);
            tempCircleView.setCircleMargin(this.marginCircleCounter += this.marginCircle);

            tempCircleView.setLayoutParams(tempArcViewLayoutParams);

            this.circleViews[i] = tempCircleView;
        }

        for (int i = this.circleViews.length - 1; i >= 0; i--) {
            this.addView(this.circleViews[i]);
            this.circleViews[i].init();
        }

        final Animation animation = new RotateAnimation(0, -45, this.radius, this.radius);
        animation.setFillAfter(true);
        startAnimation(animation);
    }

    private class CircleView extends View {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setAntiAlias(true);
                setStyle(Paint.Style.STROKE);
            }
        };

        private int startOffset;
        private float circleMargin;
        private View thisView;

        public CircleView(Context context) {
            super(context);

            this.thisView = this;
        }

        public void init() {
            final FlipAnimation flipAnimation = new FlipAnimation(0, -90);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimation(flipAnimation);
                }
            }, this.startOffset);
        }

        protected void onDraw(final Canvas canvas) {
            canvas.drawCircle(radius, radius, radius - this.circleMargin, this.paint);
        }

        public void setCircleMargin(float circleMargin) {
            this.circleMargin = circleMargin;
        }

        public void setStrokeWidth(float strokeWidth) {
            this.paint.setStrokeWidth(strokeWidth);
        }

        public void setStartOffset(int startOffset) {
            this.startOffset = startOffset;
        }

        public void setColor(int color) {
            this.paint.setColor(color);
        }

        private class FlipAnimation extends Animation {
            private final float fromYDegrees;
            private final float toYDegrees;

            public FlipAnimation(float fromYDegrees, float toYDegrees) {
                this.fromYDegrees = fromYDegrees;
                this.toYDegrees = toYDegrees;

                setDuration(halfSpeed);
                setInterpolator(new LinearInterpolator());
                setRepeatMode(Animation.REVERSE);
                setRepeatCount(Animation.INFINITE);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float yDegrees = this.fromYDegrees + ((this.toYDegrees - this.fromYDegrees) * interpolatedTime) - 1;
                ViewCompat.setRotationY(thisView, yDegrees);
            }
        }
    }
}
