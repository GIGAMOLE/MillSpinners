package com.gigamole.millspinners.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 10.04.2015.
 */
public class FigureSpinner extends FrameLayout {

    private FigureView[] figureViews = new FigureView[9];
    private int[] colors = new int[9];

    private float marginFigure;
    private float marginFigureCounter;

    private int sides;
    private int angle;
    private int radius;
    private int diameter;
    private int speed;
    private int halfSpeed;
    private int startOffset;
    private int startOffsetCounter;

    private boolean isRounded;
    private boolean isRotated;

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

    public void setRotated(boolean isRotated) {
        this.isRotated = isRotated;
    }

    public FigureSpinner(Context context) {
        this(context, null);
    }

    public FigureSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FigureSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FigureSpinner);

        try {
            this.speed = typedArray.getInteger(R.styleable.FigureSpinner_speed, 1000);
            this.halfSpeed = this.speed / 2;

            this.startOffsetCounter = this.halfSpeed;
            this.startOffset = this.halfSpeed / this.figureViews.length;

            this.sides = typedArray.getInteger(R.styleable.FigureSpinner_sides, 6);
            if (this.sides < 3) {
                throw new IllegalArgumentException("Figure should contain more than 2 sides.");
            }

            this.angle = 360 / this.sides;

            this.isRounded = typedArray.getBoolean(R.styleable.FigureSpinner_rounded, false);
            this.isRotated = typedArray.getBoolean(R.styleable.FigureSpinner_rotated, false);

            final int colorsId = typedArray.getResourceId(R.styleable.FigureSpinner_colors, 0);
            if (colorsId != 0) {
                this.colors = getResources().getIntArray(colorsId);
            } else {
                this.colors = getResources().getIntArray(R.array.bw_colors);
            }

            if (typedArray.getBoolean(R.styleable.FigureSpinner_reverse, false)) {
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
        this.marginFigure = (this.radius / this.figureViews.length) * 0.95f;

        init();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        final LayoutParams tempFigureLayoutParams = new LayoutParams(this.diameter, this.diameter);
        tempFigureLayoutParams.gravity = Gravity.CENTER;

        for (int i = 0; i < this.figureViews.length; i++) {
            final FigureView figureView = new FigureView(getContext());
            figureView.setColor(this.colors[i]);
            figureView.setFigureMargin(this.marginFigureCounter += this.marginFigure);

            if (this.isRounded) {
                figureView.setRounded();
            }

            Animation tempAnimation = new RotateAnimation(0, this.angle, this.radius, this.radius);
            tempAnimation.setStartOffset(this.startOffsetCounter);
            tempAnimation.setDuration(this.speed - this.startOffsetCounter);

            this.startOffsetCounter -= this.startOffset;

            figureView.setDrawingCacheEnabled(true);
            tempAnimation.setFillEnabled(true);
            tempAnimation.setFillBefore(true);
            tempAnimation.setFillAfter(true);
            tempAnimation.setRepeatMode(Animation.RESTART);
            tempAnimation.setRepeatCount(Animation.INFINITE);
            tempAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            figureView.startAnimation(tempAnimation);
            figureView.setLayoutParams(tempFigureLayoutParams);

            this.figureViews[i] = figureView;
            this.addView(figureView);
        }

        if (this.isRotated) {
            final Animation spinningAnimation = new RotateAnimation(0, 360, this.radius, this.radius);
            spinningAnimation.setDuration(this.speed * 5);
            spinningAnimation.setInterpolator(new LinearInterpolator());
            spinningAnimation.setRepeatMode(Animation.RESTART);
            spinningAnimation.setRepeatCount(Animation.INFINITE);
            startAnimation(spinningAnimation);
        }
    }

    private void reverseColors() {
        for (int start = 0, end = this.colors.length - 1; start <= end; start++, end--) {
            int aux = this.colors[start];
            this.colors[start] = this.colors[end];
            this.colors[end] = aux;
        }
    }

    private class FigureView extends View {

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setAntiAlias(true);
                setStyle(Paint.Style.FILL);
            }
        };
        private Path path = new Path();

        private float figureMargin;

        public FigureView(Context context) {
            this(context, null);
        }

        public FigureView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public FigureView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            final float centerX = getWidth() / 2;
            final float centerY = getHeight() / 2;

            if (sides % 3 == 0) {
                canvas.rotate(30, centerX, centerY);
            } else if (sides % 5 == 0) {
                canvas.rotate(53, centerX, centerY);
            } else if (sides % 7 == 0) {
                canvas.rotate(13, centerX, centerY);
            }

            final double section = 2.0 * Math.PI / sides;

            this.path.reset();
            this.path.moveTo(
                    (float) (centerX + (radius - this.figureMargin) * Math.cos(0)),
                    (float) (centerY + (radius - this.figureMargin) * Math.sin(0)));

            for (int i = 1; i < sides; i++) {
                this.path.lineTo(
                        (float) (centerX + (radius - this.figureMargin) * Math.cos(section * i)),
                        (float) (centerY + (radius - this.figureMargin) * Math.sin(section * i)));
            }
            this.path.close();

            canvas.drawPath(this.path, this.paint);
        }

        public void setFigureMargin(float figureMargin) {
            this.figureMargin = figureMargin;
        }

        public void setColor(int color) {
            this.paint.setColor(color);
        }

        public void setRounded() {
            this.paint.setPathEffect(new CornerPathEffect(diameter / 25f));
        }
    }
}
