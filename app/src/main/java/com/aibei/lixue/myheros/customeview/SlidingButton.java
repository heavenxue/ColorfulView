/*
 * Copyright (C) 2016 Xue Li <lixue100000@sina.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aibei.lixue.myheros.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Button;

import com.aibei.lixue.myheros.R;

/**
 * 滑动的按钮
 *
 * 作者：lixue on 2016/12/9 10:49
 */

public class SlidingButton extends Button {
    private final static String TAG = SlidingButton.class.getSimpleName();
    private final static int DEFAULT_WIDTH = 500;
    private final static int DEFAULT_HEIGHT = 200;
    private Bitmap swithBitmap;
    private int marginLeft;//默认的滑动的icon的左间距
    private float lastX;//上一个位置
    private int currenX;//当前位置
    private VelocityTracker mVelocityTracker;
    private OnSlidingEndListener onSlidingEndListener;
    private int width;//此控件宽度
    private int height;//此控件高度
    private int maxOffset;//最大偏移量

    public SlidingButton(Context context) {
        super(context);
        init();
    }

    public SlidingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化
     */
    private void init(){
        //设置圆角背景
        if (Build.VERSION.SDK_INT >= 21 ){
            setBackground(getResources().getDrawable(R.drawable.slidingbg, null));
        }else{
            setBackground(getResources().getDrawable(R.drawable.slidingbg));
        }
        swithBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slidingicon4);
        marginLeft = 30;
        currenX = marginLeft;
        Log.d(TAG,"width:" + width);
        Log.d(TAG,"height:" + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        Log.d(TAG,"currentX:" + currenX);
        canvas.drawBitmap(swithBitmap,currenX, 20 ,mpaint);
        maxOffset = getWidth() - swithBitmap.getWidth();
        Log.d(TAG,"maxOffset:" + maxOffset);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final float theX = event.getX();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = theX;
                Log.d(TAG,"lastX:" + lastX);
                break;
            case MotionEvent.ACTION_MOVE:
                float movex = theX;
                Log.d(TAG,"moveX:" + movex);
                int delayX = (int)(movex - lastX + 0.5f);
                currenX += delayX;
                if (currenX < 0){
                    currenX = marginLeft;
                }
                if (currenX > maxOffset){//超出右边边距
                    currenX = maxOffset - marginLeft;
                }
                if (movex - lastX > 50) {//当滑向右动了一段距离后，再刷新
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //如果现在手指刚刚离开屏幕状态
                if (currenX > maxOffset) {
                    currenX = maxOffset;
                }
                if (currenX < marginLeft){
                    currenX = maxOffset - marginLeft;
                }
                Log.d(TAG,"upx:" + theX+",lastx:" + lastX);
                if (theX - lastX > 50){//当滑向右动了一段距离后，再注册事件
                    if (onSlidingEndListener != null){
                        onSlidingEndListener.OnSlidingEnd();
                        currenX = marginLeft;
                    }
                    invalidate();
                }
                break;
            default:
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heigthSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            width = DEFAULT_WIDTH;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = heigthSize;
        }else{
            height = DEFAULT_HEIGHT;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setOnSlidingEndListener(OnSlidingEndListener listener){
        this.onSlidingEndListener = listener;
    }

    public interface OnSlidingEndListener{
        void OnSlidingEnd();
    }
}
