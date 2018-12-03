package me.fredsun.roundrectangleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by fred on 2018/8/28.
 */

public class RoundedRectangleHorizontalView extends View {
    private int progressColor, backgroundColor;
    private Paint paint;
    private ValueAnimator valueAnimator;
    private PathMeasure pathMeasure;
    private Path path;
    private int mWidth;
    private int mHeight;
    float mAnimatorValue;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private float mCurrentValue;
    private float mTotalValue;
    private PorterDuffXfermode mXfermode;

    public RoundedRectangleHorizontalView(Context context) {
        this(context, null);
    }

    public RoundedRectangleHorizontalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedRectangleHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        path = new Path();
        pathMeasure = new PathMeasure();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        backgroundColor = Color.parseColor("#F7F7F7");
        progressColor = Color.parseColor("#D8D8D8");
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        //获取我们自定义的样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedRectangleProgressView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.RoundedRectangleProgressView_progressColor:
                    // 默认颜色设置为黑色
                    progressColor = array.getColor(attr, Color.parseColor("#D8D8D8"));

                    break;
                case R.styleable.RoundedRectangleProgressView_backgroundColor:
                    // 默认颜色设置为黑色
                    backgroundColor = array.getColor(attr, Color.parseColor("#F7F7F7"));
                    break;
            }
        }
        array.recycle();
    }

    public void setValue(String currentValue, String totalValue){
        this.mCurrentValue = Float.valueOf(currentValue);
        this.mTotalValue = Float.valueOf(totalValue);
        Log.i("curr/total", (mTotalValue / mCurrentValue)+"");
        //创建一个值从0到xxx的动画
        valueAnimator = ValueAnimator.ofFloat(0, mCurrentValue / mTotalValue);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        //每过10毫秒 调用一次
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                Log.i("mAnimatorValue", mAnimatorValue+"");
                postInvalidate();
            }
        });
    }

    public void setColor(int backgroundColor, int progressColor){
        this.backgroundColor = backgroundColor;
        this.progressColor = progressColor;
    }

    public void startAnimate(){
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        paint.setStrokeWidth(mHeight);
        path.moveTo(mHeight/2, 0);
        path.lineTo(mHeight/2, mWidth);
        pathMeasure.setPath(path, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        //match时
//        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
//            setMeasuredDimension(14,60);
//        }else if(widthSpecMode == MeasureSpec.AT_MOST){
//            setMeasuredDimension(14, heightSpecSize);
//        }else if(heightSpecMode == MeasureSpec.AT_MOST){
//            setMeasuredDimension(widthSpecSize, 60);
//        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        pathMeasure.getPosTan(mAnimatorValue * pathMeasure.getLength(), pos, tan);
        int i = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        //画静态背景DST
        paint.setColor(backgroundColor);
        canvas.drawLine(mWidth/2, mHeight-mWidth, mWidth/2, mWidth, paint);
        canvas.drawLine(mHeight/2, mHeight/2, mWidth-mHeight/2, mHeight/2, paint);

        paint.setXfermode(mXfermode);

        //画动态柱状图SRC
        paint.setColor(progressColor);
        canvas.drawLine(-mHeight/2, mHeight/2, -mHeight/2+mAnimatorValue*mWidth, mHeight/2, paint);

        canvas.restoreToCount(i);
        paint.setXfermode(null);
    }
}
