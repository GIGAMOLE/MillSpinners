package com.gigamole.millspinners.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 11.04.2015.
 */
public class DyingLightSpinner extends FrameLayout {

    private int speed;
    private int color;
    private int diameter;

    private float radius;
    private float lineMargin;
    private float squarePadding;
    private float squareMargin;
    private float padding;

    private float mainMinSquareMargin;
    private float mainMaxSquareMargin;
    private float mainPositiveLineMargin;
    private float mainNegativeLineMargin;

    private float lineOffset;

    private LayoutParams layoutParams;
    private SquareView squareView;

    private Type type;
    public enum Type {
        CHAIN,
        BRACKET,
    }


    private final Paint squarePaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setAntiAlias(true);
            setStyle(Style.FILL);
        }
    };

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setAntiAlias(true);
            setStyle(Style.STROKE);
        }
    };

    private final Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setAntiAlias(true);
            setStyle(Style.STROKE);
        }
    };

    public void setColor(int color) {
        this.color = color;

        final int r = (this.color >> 16) & 0xFF;
        final int g = (this.color >> 8) & 0xFF;
        final int b = (this.color >> 0) & 0xFF;

        this.squarePaint.setColor(this.color);
        this.pointPaint.setColor(this.color);
        this.linePaint.setColor(Color.argb(192, r, g, b));
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void setType(int type) {
        switch (type) {
            case 1:
                setType(Type.BRACKET);
                break;
            case 0:
            default:
                setType(Type.CHAIN);
                break;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public DyingLightSpinner(Context context) {
        this(context, null);
    }

    public DyingLightSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DyingLightSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DyingLightSpinner);

        try {
            this.speed = typedArray.getInteger(R.styleable.DyingLightSpinner_speed, 250);

            setType(typedArray.getInt(R.styleable.DyingLightSpinner_type, 0));
            setColor(typedArray.getColor(R.styleable.DyingLightSpinner__color, getResources().getColor(R.color.white)));
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

        this.layoutParams = new LayoutParams(this.diameter, this.diameter);
        this.layoutParams.gravity = Gravity.CENTER;

        this.squareView = new SquareView(getContext());
        this.squareView.setLayoutParams(this.layoutParams);
        addView(this.squareView);

        this.radius = this.diameter / 2;

        if (this.type == Type.BRACKET) {
            this.squareMargin = this.radius / 1.30f;
            this.padding = this.radius / 3.35f;

            this.squarePadding = this.padding / 2.75f;
            this.lineMargin = this.squarePadding / 2.5f;

            this.linePaint.setStrokeWidth((int) this.squarePadding / 3);
            this.pointPaint.setStrokeWidth((int) this.squarePadding / 2.5f);

            this.mainMinSquareMargin = this.squareMargin - this.squarePadding;
            this.mainMaxSquareMargin = this.diameter - this.squareMargin + this.squarePadding;
            this.mainNegativeLineMargin = this.radius - this.lineMargin;
            this.mainPositiveLineMargin = this.radius + this.lineMargin;

            initBracketSpinner();
        } else {
            this.squareMargin = this.radius / 1.75f;
            this.padding = this.radius / 4f;
            this.squarePadding = this.diameter - this.padding;

            this.lineOffset = this.padding / 3.5f;

            this.linePaint.setStrokeWidth((int) this.padding / 8f);
            this.pointPaint.setStrokeWidth((int) this.padding);

            initChainSpinner();
        }


        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initChainSpinner() {
        final SurfaceView view = new SurfaceView(getContext());
        view.setLayoutParams(this.layoutParams);
        addView(view);

        this.squareView.setVisibility(INVISIBLE);
    }

    private void initBracketSpinner() {
        final FrameLayout surfaceView = new FrameLayout(getContext());
        surfaceView.setLayoutParams(this.layoutParams);
        addView(surfaceView);

        final LineView lineView_1 = new LineView(getContext(), 1);
        surfaceView.addView(lineView_1);

        final LineView lineView_2 = new LineView(getContext(), 2);
        surfaceView.addView(lineView_2);

        final LineView lineView_3 = new LineView(getContext(), 3);
        surfaceView.addView(lineView_3);

        final LineView lineView_4 = new LineView(getContext(), 4);
        surfaceView.addView(lineView_4);
    }

    private class SurfaceView extends View {

        public SurfaceView(Context context) {
            super(context);
        }

        private float leftPointX = radius;
        private float leftPointY = radius;
        private float rightPointX = radius;
        private float rightPointY = radius;
        private float topPointX = radius;
        private float topPointY = radius;
        private float bottomPointX = radius;
        private float bottomPointY = radius;

        private boolean leftAtEnd;
        private boolean topAtEnd;
        private boolean rightAtEnd;
        private boolean bottomAtEnd;
        private boolean allAtEnd;

        private boolean isSquareAnimationStarted;
        private final Handler handlerSquare = new Handler();
        private final Runnable handlerSquareUpdater = new Runnable() {
            @Override
            public void run() {
                isSquareAnimationStarted = true;

                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(30);
                animation.setStartOffset(20);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(3);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        squareView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                squareView.startAnimation(animation);
                handlerSquare.postDelayed(handlerSquareUpdater, 1000);
            }
        };

        private boolean isLinesAnimationStarted;
        private final Handler handlerLines = new Handler();
        private final Runnable handlerLinesUpdater = new Runnable() {
            @Override
            public void run() {
                isLinesAnimationStarted = true;

                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(50);
                animation.setStartOffset(20);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(1);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setVisibility(INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                startAnimation(animation);
                handlerLines.postDelayed(handlerLinesUpdater, 500);
            }
        };

        @Override
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawPoint(this.leftPointX, this.leftPointY, pointPaint);
            canvas.drawPoint(this.topPointX, this.topPointY, pointPaint);
            canvas.drawPoint(this.rightPointX, this.rightPointY, pointPaint);
            canvas.drawPoint(this.bottomPointX, this.bottomPointY, pointPaint);

            canvas.drawLine(this.leftPointX, this.leftPointY, this.topPointX, this.topPointY, linePaint);
            canvas.drawLine(this.leftPointX, this.leftPointY, this.rightPointX, this.rightPointY, linePaint);
            canvas.drawLine(this.leftPointX, this.leftPointY, this.bottomPointX, this.bottomPointY, linePaint);
            canvas.drawLine(this.topPointX, this.topPointY, this.rightPointX, this.rightPointY, linePaint);
            canvas.drawLine(this.topPointX, this.topPointY, this.bottomPointX, this.bottomPointY, linePaint);
            canvas.drawLine(this.bottomPointX, this.bottomPointY, this.rightPointX, this.rightPointY, linePaint);

            if (!this.allAtEnd) {
                if (!this.leftAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (leftPointX > padding) {
                                leftPointX -= lineOffset;
                            } else {
                                if (leftPointY > padding) {
                                    leftPointY -= lineOffset;
                                } else {
                                    leftAtEnd = true;
                                }
                            }
                        }
                    }, 200);
                }

                if (!this.topAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (topPointY > padding) {
                                topPointY -= lineOffset;
                            } else {
                                if (topPointX < squarePadding) {
                                    topPointX += lineOffset;
                                } else {
                                    topAtEnd = true;
                                }
                            }
                        }
                    }, 300);
                }

                if (!this.rightAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (rightPointX < squarePadding) {
                                rightPointX += lineOffset;
                            } else {
                                if (rightPointY < squarePadding) {
                                    rightPointY += lineOffset;
                                } else {
                                    rightAtEnd = true;
                                }
                            }
                        }
                    }, 0);
                }

                if (!this.bottomAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (bottomPointY < squarePadding) {
                                bottomPointY += lineOffset;
                            } else {
                                if (bottomPointX > padding) {
                                    bottomPointX -= lineOffset;
                                } else {
                                    bottomAtEnd = true;
                                }
                            }
                        }
                    }, 100);
                }

                if (this.leftAtEnd && this.topAtEnd && this.rightAtEnd && this.bottomAtEnd) {
                    if (!this.isSquareAnimationStarted) {
                        handlerSquareUpdater.run();
                    }

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            squareView.clearAnimation();
                            squareView.setVisibility(INVISIBLE);

                            isSquareAnimationStarted = false;
                            allAtEnd = true;

                            handlerSquare.removeCallbacks(handlerSquareUpdater);
                        }
                    }, 1500);
                }
            } else {
                if (this.leftAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (leftPointY < radius) {
                                leftPointY += lineOffset;
                            } else {
                                if (leftPointX < radius) {
                                    leftPointX += lineOffset;
                                } else {
                                    leftAtEnd = false;
                                }
                            }
                        }
                    }, 200);
                }

                if (this.topAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (topPointX > radius) {
                                topPointX -= lineOffset;
                            } else {
                                if (topPointY < radius) {
                                    topPointY += lineOffset;
                                } else {
                                    topAtEnd = false;
                                }
                            }
                        }
                    }, 300);
                }

                if (this.rightAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (rightPointY > radius) {
                                rightPointY -= lineOffset;
                            } else {
                                if (rightPointX > radius) {
                                    rightPointX -= lineOffset;
                                } else {
                                    rightAtEnd = false;
                                }
                            }
                        }
                    }, 0);
                }

                if (this.bottomAtEnd) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (bottomPointX < radius) {
                                bottomPointX += lineOffset;
                            } else {
                                if (bottomPointY > radius) {
                                    bottomPointY -= lineOffset;
                                } else {
                                    bottomAtEnd = false;
                                }
                            }
                        }
                    }, 100);
                }

                if (!this.leftAtEnd && !this.topAtEnd && !this.rightAtEnd && !this.bottomAtEnd) {

                    if (!this.isLinesAnimationStarted) {
                        handlerLinesUpdater.run();
                    }

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearAnimation();
                            setVisibility(VISIBLE);

                            isLinesAnimationStarted = false;
                            allAtEnd = false;

                            handlerLines.removeCallbacks(handlerLinesUpdater);
                        }
                    }, 750);
                }
            }

            postInvalidate();
        }
    }

    private class LineView extends View {

        private final Path path = new Path();
        private final int place;

        public LineView(Context context, int place) {
            super(context);

            this.place = place;

            final Animation parentAnimation = new RotateAnimation(0, -90, radius, radius);
            parentAnimation.setDuration(speed);
            parentAnimation.setInterpolator(new LinearInterpolator());

            Animation animation = null;

            switch (place) {
                case 1:
                    animation = new TranslateAnimation(0, -padding, 0, -padding);
                    break;
                case 2:
                    animation = new TranslateAnimation(0, padding, 0, -padding);
                    break;
                case 3:
                    animation = new TranslateAnimation(0, padding, 0, padding);
                    break;
                case 4:
                    animation = new TranslateAnimation(0, -padding, 0, padding);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        private int pointer;

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            if (this.pointer++ % 2 == 0) {
                                ((FrameLayout) getParent()).startAnimation(parentAnimation);
                            }
                        }
                    });
                    break;
            }

            animation.setDuration(speed);
            animation.setFillAfter(true);
            animation.setStartOffset(speed);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Animation.INFINITE);

            startAnimation(animation);


        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            this.path.reset();

            switch (place) {
                case 1:
                    path.moveTo(mainMinSquareMargin, mainNegativeLineMargin);
                    path.lineTo(mainMinSquareMargin, mainMinSquareMargin);
                    path.lineTo(mainNegativeLineMargin, mainMinSquareMargin);

                    canvas.drawPoint(mainMinSquareMargin, mainMinSquareMargin, pointPaint);
                    break;
                case 2:
                    path.moveTo(mainPositiveLineMargin, mainMinSquareMargin);
                    path.lineTo(mainMaxSquareMargin, mainMinSquareMargin);
                    path.lineTo(mainMaxSquareMargin, mainNegativeLineMargin);

                    canvas.drawPoint(mainMaxSquareMargin, mainMinSquareMargin, pointPaint);
                    break;
                case 3:
                    path.moveTo(mainMaxSquareMargin, mainPositiveLineMargin);
                    path.lineTo(mainMaxSquareMargin, mainMaxSquareMargin);
                    path.lineTo(mainPositiveLineMargin, mainMaxSquareMargin);

                    canvas.drawPoint(mainMaxSquareMargin, mainMaxSquareMargin, pointPaint);
                    break;
                case 4:
                    path.moveTo(mainNegativeLineMargin, mainMaxSquareMargin);
                    path.lineTo(mainMinSquareMargin, mainMaxSquareMargin);
                    path.lineTo(mainMinSquareMargin, mainPositiveLineMargin);

                    canvas.drawPoint(mainMinSquareMargin, mainMaxSquareMargin, pointPaint);
                    break;
            }

            canvas.drawPath(this.path, linePaint);
        }
    }

    private class SquareView extends View {

        public SquareView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawRect(
                    squareMargin,
                    squareMargin,
                    getWidth() - squareMargin,
                    getHeight() - squareMargin,
                    squarePaint
            );
        }
    }
}
