package com.aibei.lixue.myheros.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义view模板
 * Created by Administrator on 2016/11/30.
 */

public class BaseCustomView  extends View{
    public BaseCustomView(Context context) {
        super(context);
    }

    public BaseCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpecWidth){
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpecWidth);
        int size = MeasureSpec.getSize(measureSpecWidth);
        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            result = 200;
            //此时的size可能父控件的size有可能是父控件的size,也有可能是自适应的不超过父控件的size
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }
}
