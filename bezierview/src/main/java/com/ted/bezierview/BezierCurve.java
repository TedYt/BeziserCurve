package com.ted.bezierview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Copyright (C) 2008 The Android Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by Ted.Yt on 9/23/16.
 */
public class BezierCurve extends View {

    private Paint mPaint;
    private Path mPath;

    private static final int SMALL_CIRCLE_RADIUS = 25;
    //当距离小于等于GAP时，触发Sticky效果
    private static final float GAP = 1.5f * SMALL_CIRCLE_RADIUS;

    public BezierCurve(Context context) {
        this(context,null);
    }

    public BezierCurve(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierCurve(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint =  new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /*mPath.moveTo(w / 3, h / 2);//Bezier 曲线的 起点
        //1 2参数是控制点的坐标，3 4 参数是终点的坐标
        //mPath.quadTo(w / 2, h / 4, w / 3 * 2, h / 2);
        mPath.cubicTo(w / 3 + 150, h / 5, w / 3 * 2 - 150, h / 5, w / 3 * 2, h / 2);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w  = getWidth();
        int h = getHeight();

        mPath.reset();

        //小球下落，大球吸收小球
        showStep1Anim(canvas,w,h);
    }

    private void showStep1Anim(Canvas canvas, int w, int h) {
        mPath.moveTo(w / 3, h / 2);//Bezier 曲线的 起点
        //1 2参数是控制点的坐标，3 4 参数是终点的坐标
        //mPath.quadTo(w / 2, h / 4, w / 3 * 2, h / 2);
        mPath.cubicTo(  w / 3 + 100 * mValue, h / 3 - 150 * mValue,
                w / 3 * 2 - 100 * mValue, h / 3 - 150 * mValue,
                w / 3 * 2, h / 2);

        //上面的方法调用后，mPath 落在了点(w / 3 * 2, h / 2),
        //下面方法就会一这个点为起点
        mPath.cubicTo( w / 3 * 2, h / 3 * 2,
                w / 3   , h / 3 * 2,
                w / 3 , h / 2);

        float gap = (h / 3 - 150) - h / 8 + 120;
        mPath.addCircle( w /2 , h / 8 + gap * mValueofSC, SMALL_CIRCLE_RADIUS, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
    }

    public void beginAnima(){
        startAnim();
    }

    private float mValue;
    private float mValueofSC;//for small circle
    private void startAnim(){

        //ValueAnimator vaSmallCircle = createVaOfSmallCircle();
        ValueAnimator va1OfBigCircle = createVa1OfBigCircle();
        ValueAnimator va2OfBigCircle = createVa2OfBigCircle();

        AnimatorSet animSet = new AnimatorSet();
        //animSet.play(vaSmallCircle).before(va1OfBigCircle);
        animSet.play(va1OfBigCircle).before(va2OfBigCircle);
        animSet.start();
    }

    //大球的上半部分的动画，往回收缩的效果
    private ValueAnimator createVa2OfBigCircle() {

        ValueAnimator va = ValueAnimator.ofFloat(1f, 0f);
        va.setDuration(1000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        return va;
    }

    //大球的上半部分的动画，往外突出的效果
    private ValueAnimator createVa1OfBigCircle() {
        ValueAnimator va = ValueAnimator.ofFloat(0f,1f);
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        return va;
    }

    //小球下落的动画
    private ValueAnimator createVaOfSmallCircle() {
        ValueAnimator va = ValueAnimator.ofFloat(0f, 1.6f);
        va.setDuration(1500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValueofSC = (float)animation.getAnimatedValue();
                if (mValueofSC >= 0.52){
                    //animation.cancel();
                    startAnim();
                }

                invalidate();
            }
        });

        va.start();

        return va;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                //startAnim();
                createVaOfSmallCircle();
                break;
            default:
                break;
        }

        return true;
    }
}
