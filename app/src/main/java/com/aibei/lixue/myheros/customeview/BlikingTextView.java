package com.aibei.lixue.myheros.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 闪烁的文字
 * Created by Administrator on 2016/12/1.
 */

public class BlikingTextView extends TextView {
    private LinearGradient linearGradient;
    private Paint mpaint;
    private int mViewWidth;
    private Matrix gradientMatrix;
    private int translate;

    public BlikingTextView(Context context) {
        super(context);
    }

    public BlikingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlikingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlikingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gradientMatrix != null){
            translate += mViewWidth / 5;
            if (translate > mViewWidth * 2){
                translate = -mViewWidth;
            }
            gradientMatrix.setTranslate(translate,0);//向右平移
            linearGradient.setLocalMatrix(gradientMatrix);
            postInvalidateDelayed(200);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mpaint = getPaint();

        if (mViewWidth == 0)    mViewWidth = getMeasuredWidth();
        if (mViewWidth > 0){
            linearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{Color.BLUE,Color.RED,Color.BLUE},null, Shader.TileMode.CLAMP);
            gradientMatrix = new Matrix();
            mpaint.setShader(linearGradient);
        }
    }
}
