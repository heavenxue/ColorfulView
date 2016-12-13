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
    private Bitmap swithBitmap;
    private int marginLeft;//默认的滑动的icon的左间距
    private int lastX;//上一个位置
    private int currenX;//当前位置
    private boolean isSlipping;//是否正在滑动
    private VelocityTracker mVelocityTracker;
    private OnSlidingEndListener onSlidingEndListener;

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
        setBackground(getResources().getDrawable(R.drawable.slidingbg, null));
//        bg = BitmapFactory.decodeResource(getResources(),R.drawable.slidingbg);
        swithBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slidingicon4);
        marginLeft = 30;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        Log.d(TAG,"currentX:" + currenX);
        if (isSlipping){
            if (currenX > getWidth()){
                currenX = getWidth();
            }else if (currenX < getLeft()){
                currenX = marginLeft;
            }else if (currenX == 0){
                currenX = marginLeft;
            }
        }else {
            currenX = marginLeft;
        }

        //如果手指滑出了开关的范围，应当这样处理
        if (currenX < 0) {
            currenX = marginLeft;
        } else if (currenX > getRight() - marginLeft) {
            currenX = getRight() - swithBitmap.getWidth()/2;
        }

        canvas.drawBitmap(swithBitmap,currenX,25,mpaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) getX();
                if (event.getX() > getWidth() || event.getY() > getHeight()){
                    return false;
                }else{
                    if (getX() > 50){
                        isSlipping = true;
                        lastX = (int) event.getX();
                        currenX = lastX;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int movex = (int) getX();
                int delayX = movex - lastX;
                if (delayX > 50){//如果是向右滑动
                    currenX = movex + lastX;
                }
                break;
            case MotionEvent.ACTION_UP:
                //如果现在手指刚刚离开屏幕状态
                int upDelayX = (int)getX() - lastX;
                if (upDelayX > 50) {//如果是向右滑动
                    if (onSlidingEndListener != null){
                        onSlidingEndListener.OnSlidingEnd();
                        isSlipping = false;
                    }
                }

            default:
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            break;
        }
        invalidate();
        return true;
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
