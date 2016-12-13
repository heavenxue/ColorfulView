package com.aibei.lixue.myheros.customeview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * 自定义View,可以竖直滑动的(实现一个ScrollView)
 * Created by Administrator on 2016/12/5.
 */

public class MyScrollView extends ViewGroup{
    private int mscreenHeight;//屏幕高度
    private int lastY;//上一次的y的坐标
    private int startY;//开始的竖直坐标
    private int endY;//结束时的竖直坐标
    private Scroller mScroller;


    public MyScrollView(Context context) {
        super(context);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mscreenHeight = dm.heightPixels;
        Log.d("MyScrollView","mScreenHeight:" + mscreenHeight);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        //重新测量子View的宽高
        for (int i = 0;i < count;i ++){
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childCount = getChildCount();
        //设置ViewGroup的高度
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mscreenHeight * childCount;
        setLayoutParams(mlp);
        //重新设置子View的宽高
        for (int j = 0;j < childCount;j ++){
            View child = getChildAt(j);
            if (child.getVisibility() == View.VISIBLE){
                child.layout(i,j * mscreenHeight,i2,(j + 1) * mscreenHeight);
//                child.layout(i,i1,i2,i3);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        int y = (int) event.getY(); //最开始y坐标
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                startY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = lastY - y;//竖直间隔
                if (getScrollY() < 0){
                    dy = 0;
                }
                if (getScrollY() > getHeight() - mscreenHeight){
                    dy = 0;
                }
                scrollBy(0,dy);//滑动到指定的位置
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                int dScrollY = checkAligment();
                if (dScrollY > 0){
                    if (dScrollY < mscreenHeight / 3){
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,mscreenHeight - dScrollY);
                    }
                }else{
                    if (-dScrollY < mscreenHeight / 3){
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,-mscreenHeight-dScrollY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }
    }

    private int checkAligment(){
        endY = getScrollY();
        boolean isUp = ((endY - startY) > 0) ? true : false;
        int lastPrev = endY % mscreenHeight;
        int lastNext = mscreenHeight - lastPrev;
        if (isUp) {
            //向上的
            return lastPrev;
        } else {
            return -lastNext;
        }
    }

}
