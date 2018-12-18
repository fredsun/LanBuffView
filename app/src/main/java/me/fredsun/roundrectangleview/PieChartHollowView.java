package me.fredsun.roundrectangleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.List;

/**
 * Created by fred on 2018/8/30.
 */

public class PieChartHollowView extends View {
    private final Context context;
    private int minAngle = 30; //容纳文字最小角度
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 饼状图宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图起始角度
     */
    private float mStartAngle = 0f;
    /**
     * 用户数据
     */
    private List<PieData> mData;
    /**
     * 动画时间
     */
    private static final long ANIMATION_DURATION = 1000;
    /**
     * 自定义动画
     */
    private ValueAnimator valueAnimator;
    /**
     * 绘制方式
     */
    private int mDrawWay = PART;
    public static final int PART = 0;//分布绘制
    public static final int COUNT = 1;//连续绘制
    private Paint mTextPaint;
    private float r;
    private int mBorderWidth;
    private Paint mNoDataCenterTextPaint;
    private boolean flagHasData;

    public PieChartHollowView(Context context) {
        this(context, null);
    }

    public PieChartHollowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartHollowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防止抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔为填充
        //初始化动画
        valueAnimator = ValueAnimator.ofFloat(0,  1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(ANIMATION_DURATION);
        //每过10毫秒 调用一次
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float interpolatedTime = (float)animation.getAnimatedValue();
                Log.i("sxl", "animValue: " + animation.getAnimatedFraction());
                if (interpolatedTime < 1.0f) {
                    for (int i = 0; i < mData.size(); i++) {
                        PieData data = mData.get(i);
                        //通过总和来计算百分比
                        float percentage = data.getValue() / sumValue;
                        //通过百分比来计算对应的角度
                        float angle = percentage * 360;
                        //根据插入时间来计算角度
                        angle = angle * interpolatedTime;
                        data.setAngle(angle);
                    }
                } else {//默认显示效果
                    for (int i = 0; i < mData.size(); i++) {
                        //通过总和来计算百分比
                        PieData data = mData.get(i);
                        float percentage = data.getValue() / sumValue;
                        //通过百分比来计算对应的角度
                        float angle = percentage * 360;
                        data.setPercentage(percentage);
                        data.setAngle(angle);
                    }
                }
                invalidate();
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        mNoDataCenterTextPaint = new Paint();
        mNoDataCenterTextPaint.setColor(Color.parseColor("#D8D8D8"));
        mNoDataCenterTextPaint.setAntiAlias(true);
        mNoDataCenterTextPaint.setStyle(Paint.Style.STROKE);
        mNoDataCenterTextPaint.setTextAlign(Paint.Align.CENTER);


    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle
     */
    public void setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();//刷新
    }


    /**
     * 设置数据
     *
     * @param mData
     * @param count
     */
    public void setData(List<PieData> mData, int count) {
        setmData(mData);
    }

    /**
     * 设置数据和绘制方式
     *
     * @param mData
     */
    public void setData(List<PieData> mData, int sumValue,  int mDrawWay) {
        setmData(mData);
        this.mDrawWay = mDrawWay;
        this.sumValue = sumValue;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    private void setmData(List<PieData> mData) {
        sumValue = 0;
        this.mData = mData;
        initData(mData);
        flagHasData = false;
        for (PieData pieData:mData){
            if (pieData.getValue()!=0){
                flagHasData = true;
            }
        }
        if (flagHasData){
            valueAnimator.start();
        }else {
//            PieData pieData = new PieData("a", 1, Color.parseColor("#AE02A9"));
//            pieData.setAngle(360);
//            pieData.setPercentage(1);
//            mData.clear();
//            mData.add(0, pieData);

        }
        postInvalidate();
        invalidate();
    }

    float sumValue = 0;//数据值的总和

    /**
     * 初始化数据
     *
     * @param mData
     */
    private void initData(List<PieData> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        /**
         * 计算数据总和确定颜色
         */
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            sumValue += data.getValue();
            data.setColor(data.getColor());
        }
        /**
         * 计算百分比和角度
         */
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            data.setCurrentStartAngle(currentStartAngle);
            //通过总和来计算百分比
            float percentage = data.getValue() / sumValue;
            //通过百分比来计算对应的角度
            float angle = percentage * 360;
            //设置用户数据
            data.setPercentage(percentage);
            data.setAngle(angle);
            currentStartAngle += angle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBorderWidth = (int)(w * 0.2f);
        mNoDataCenterTextPaint.setTextSize(mWidth/12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData == null) {
            return;
        }
        //1.移动画布到中心点
        canvas.translate(mWidth / 2, mHeight / 2);
        //2.设置当前起始角度
        float currentStartAngle = mStartAngle;
        //3.确定饼图的半径
        r = (float) (Math.min(mWidth, mHeight) / 2 - mBorderWidth/2 );
        float rOut = r*1.2f;
        //4.确定饼图的矩形大小
        RectF rectF = new RectF(-r, -r, r, r);
        //4.1 确定外圈的矩形大小
        RectF rectFOut = new RectF(-rOut, -rOut, rOut, rOut);
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            //5.设置颜色
            mPaint.setColor(data.getColor());
            //6.绘制饼图
            if (mDrawWay == PART) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mBorderWidth);
                if (!flagHasData){
                    mPaint.setColor(Color.parseColor("#D8D8D8"));
                    canvas.drawArc(rectF, -90, 360, false, mPaint);
                    canvas.drawText("暂无数据", 0, 0, mNoDataCenterTextPaint);
                }else {
                    canvas.drawArc(rectF, data.getCurrentStartAngle(), data.getAngle(), false, mPaint);
                    currentStartAngle += data.getAngle();
                    //绘制文字
                    if (data.getPercentage()!=0){
                        drawText(canvas, data.getCurrentStartAngle()+data.getAngle()/2, (int)(data.getPercentage()*100)+"%", minAngle-1);
                    }
                }
                Log.i("data.getAngle", data.getValue() + " : " + data.getCurrentStartAngle()+" : "+ data.getAngle());
            } else if (mDrawWay == COUNT) {
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
                //画白圈
                mPaint.setStrokeWidth(20);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectFOut, currentStartAngle, data.getAngle(), false, mPaint);
                //7. 绘制文字
                if (data.getPercentage()!=0) {
                    drawText(canvas, currentStartAngle + data.getAngle() / 2, (int) (data.getPercentage() * 100) + "", data.getAngle());
                }
                //8.绘制下一块扇形时先将角度加上当前扇形的角度
                currentStartAngle += data.getAngle();
            }
        }
    }

//    class PieChartAnimation extends Animation {
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            super.applyTransformation(interpolatedTime, t);
//
//        }
//    }

    /**画文字
     *
     * @param mCanvas
     * @param textAngle
     * @param kinds 文字内容
     * @param needDrawAngle 画内部画外部
     */
    private void drawText(Canvas mCanvas, float textAngle, String kinds, float needDrawAngle) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(30);
        mTextPaint.getTextBounds(kinds, 0, kinds.length(), rect);
        if (textAngle >= 0 && textAngle <= 90) { //画布坐标系第一象限(数学坐标系第四象限)
            if (needDrawAngle < minAngle) { //如果小于某个度数,就把文字画在饼状图外面
                mCanvas.drawText(kinds, (float) (r * 1.0 * Math.cos(Math.toRadians(textAngle))), (float) (r * 1.0 * Math.sin(Math.toRadians(textAngle)))+rect.height()/2, mTextPaint);
            } else {
                mCanvas.drawText(kinds, (float) (r * 0.75 * Math.cos(Math.toRadians(textAngle))), (float) (r * 0.75 * Math.sin(Math.toRadians(textAngle)))+rect.height()/2, mTextPaint);
            }
        } else if (textAngle > 90 && textAngle <= 180) { //画布坐标系第二象限(数学坐标系第三象限)
            if (needDrawAngle < minAngle) {
                mCanvas.drawText(kinds, (float) (-r * 1.0 * Math.cos(Math.toRadians(180 - textAngle))), (float) (r * 1.0 * Math.sin(Math.toRadians(180 - textAngle)))+rect.height()/2, mTextPaint);
            } else {
                mCanvas.drawText(kinds, (float) (-r * 0.75 * Math.cos(Math.toRadians(180 - textAngle))), (float) (r * 0.75 * Math.sin(Math.toRadians(180 - textAngle)))+rect.height()/2, mTextPaint);
            }
        } else if (textAngle > 180 && textAngle <= 270) { //画布坐标系第三象限(数学坐标系第二象限)
            if (needDrawAngle < minAngle) {
                mCanvas.drawText(kinds, (float) (-r * 1.0 * Math.cos(Math.toRadians(textAngle - 180))), (float) (-r * 1.0 * Math.sin(Math.toRadians(textAngle - 180)))+rect.height()/2, mTextPaint);
            } else {
                mCanvas.drawText(kinds, (float) (-r * 0.75 * Math.cos(Math.toRadians(textAngle - 180))), (float) (-r * 0.75 * Math.sin(Math.toRadians(textAngle - 180)))+rect.height()/2, mTextPaint);
            }
        } else { //画布坐标系第四象限(数学坐标系第一象限)
            if (needDrawAngle < minAngle) {
                mCanvas.drawText(kinds, (float) (r * 1.0 * Math.cos(Math.toRadians(360 - textAngle))), (float) (-r * 1.0 * Math.sin(Math.toRadians(360 - textAngle)))+rect.height()/2, mTextPaint);
            } else {
                mCanvas.drawText(kinds, (float) (r * 0.75 * Math.cos(Math.toRadians(360 - textAngle))), (float) (-r * 0.75 * Math.sin(Math.toRadians(360 - textAngle)))+rect.height()/2, mTextPaint);
            }
        }
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}
