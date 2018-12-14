package me.fredsun.roundrectangleview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import me.fredsun.roundrectangleview.KcalData;

/**
 * Created by fred on 2018/9/11.
 */

public class LineChartView extends View {
    private final Paint paintBrokenLine;
    private final Path pathDottedLine;
    private final Path pathBrokenLine;
    private final Path pathShadowDst;
    private PathMeasure pathMeasureBrokenLine;
    private PathMeasure pathMeasureShadow;
    private Paint paintShadow;
    private Path pathShadow;
    private final Path pathBrokenLineDst;
    private final ValueAnimator valueAnimator;
    private Paint paintBorder;
    private final Paint paintBorderLeftText;
    private final Paint paintBorderBottomText;
    private final int borderMargin;
    private final float xBorderMargin;
    private final float yBorderMargin;
    private final Paint paintDottedLine;
    private int mHeight;
    private int mWidth;
    private int yTotalValue;
    private int xTotalValue;
    private List<Integer> yValueArray, xValueArray;
    private List<KcalData> kcalData;
    private float mAnimatorValue;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private LinearGradient mShader;
    private PorterDuffXfermode mXfermode;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //外边框
        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.FILL);
        paintBorder.setStrokeWidth(3);
        paintBorder.setColor(Color.parseColor("#D8D8D8"));
        //左侧y轴刻度
        paintBorderLeftText = new Paint();
        paintBorderLeftText.setStyle(Paint.Style.FILL);
        paintBorderLeftText.setColor(Color.parseColor("#9B9B9B"));
        paintBorderLeftText.setTextSize(30);
        paintBorderLeftText.setTextAlign(Paint.Align.RIGHT);
        //底部x轴刻度
        paintBorderBottomText = new Paint();
        paintBorderBottomText.setStyle(Paint.Style.FILL);
        paintBorderBottomText.setColor(Color.parseColor("#9B9B9B"));
        paintBorderBottomText.setTextSize(30);
        paintBorderBottomText.setTextAlign(Paint.Align.CENTER);
        borderMargin = 30;
        xBorderMargin = borderMargin * 2.0f;
        yBorderMargin = borderMargin * 1.5f;
        //x轴虚线
        pathDottedLine = new Path();
        //x轴虚线
        paintDottedLine = new Paint();
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{10, 20}, 10);
        paintDottedLine.setStyle(Paint.Style.STROKE);
        paintDottedLine.setColor(Color.parseColor("#D8D8D8"));
        paintDottedLine.setPathEffect(dashPathEffect);
        //折线
        pathBrokenLine = new Path();
        paintBrokenLine = new Paint();
        paintBrokenLine.setStyle(Paint.Style.STROKE);
        paintBrokenLine.setStrokeWidth(5);
        paintBrokenLine.setColor(Color.parseColor("#FF8D00"));
        paintBrokenLine.setAntiAlias(true);
        pathShadow = new Path();
        paintShadow = new Paint();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

        pathMeasureBrokenLine = new PathMeasure();
        pathMeasureShadow = new PathMeasure();

        pathBrokenLineDst = new Path();
        pathShadowDst = new Path();

        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        //每过10毫秒 调用一次
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
//                postInvalidate();
                postInvalidateOnAnimation();
            }
        });
    }

    public void init(int xTotalValue, int yTotalValue, List<KcalData> kcalData) {
//        this.yTotalValue = 20;
//        this.xTotalValue = 60;
//        this.yValueArray = new ArrayList<>();

        //单位:分钟
        this.xTotalValue = xTotalValue;
        //单位:卡路里
        this.yTotalValue = yTotalValue;
        this.kcalData = kcalData;
    }

    public void startAnim(){
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
//        yTotalValue = 20;
//        xTotalValue = 60;
        yValueArray = new ArrayList<>();
        int yAverage = yTotalValue / 4; //5
        for (int i =0; i<5; i++){ //y轴4个刻度
            yValueArray.add(yAverage * i);
        }

        xValueArray = new ArrayList<>();
        int xAverage = xTotalValue /6;
        for (int i =0; i<6; i++){
            xValueArray.add(xAverage * i);  ;
        }

//        kcalData = new ArrayList<>();
//        for (int i=0; i<60; i++){
//            kcalData.add(new KcalData(i, (int)(Math.random()*5)+1));
//        }

        //渐变色范围(canvas.translate 后反向了)
        int[] shadowColors = {
                Color.parseColor("#FDFDFD"),
                Color.parseColor("#FFCCAE")
                , Color.parseColor("#FFCCAE")
        };
        mShader = new LinearGradient(0, 0, -borderMargin * 1.5f, -getHeight(), shadowColors, null, Shader.TileMode.CLAMP);

        paintShadow.setShader(mShader);
    }

    @SuppressLint("DrawAllocation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //原点换到左下角
        canvas.translate(0, mHeight);

        //折线与阴影区path偏移
        pathBrokenLine.moveTo(borderMargin * 2.0f, -yBorderMargin);
        pathShadow.moveTo(borderMargin * 2.0f, -yBorderMargin);
        pathBrokenLineDst.moveTo(borderMargin * 2.0f, -yBorderMargin);
        pathShadowDst.moveTo(borderMargin * 2.0f, -yBorderMargin);

        for (int i =0; i<=xTotalValue; i++){
            pathBrokenLine.lineTo(borderMargin * 2.0f + (mWidth-borderMargin * 2.0f)/xTotalValue * i , -yBorderMargin-(kcalData.get(i).getKcal()* (mHeight-yBorderMargin)*0.8f / yTotalValue ));
            pathShadow.lineTo(borderMargin * 2.0f + (mWidth-borderMargin * 2.0f)/xTotalValue * i , -yBorderMargin-(kcalData.get(i).getKcal() * (mHeight-yBorderMargin)*0.8f / yTotalValue)+paintBrokenLine.getStrokeWidth()/2);
        }
        pathMeasureBrokenLine.setPath(pathBrokenLine, false);
        pathMeasureShadow.setPath(pathShadow, false);
        float currentLength = mAnimatorValue * pathMeasureBrokenLine.getLength();
        pathMeasureBrokenLine.getSegment(0, currentLength, pathBrokenLineDst, false);
        pathMeasureShadow.getSegment(0, currentLength, pathShadowDst, false);
        //画折线
        canvas.drawPath(pathBrokenLineDst, paintBrokenLine);

        //画阴影
        pathMeasureShadow.getPosTan(currentLength, pos,tan);
        pathShadowDst.lineTo(pos[0], -yBorderMargin-paintBorder.getStrokeWidth()/2);
        paintShadow.setXfermode(mXfermode);
        canvas.drawPath(pathShadowDst, paintShadow);

        for (int i =1; i < yValueArray.size(); i++){
            Integer yValue = yValueArray.get(i);
            //画y轴刻度
            canvas.drawText(yValue.toString(), borderMargin * 1.5f, -(mHeight-yBorderMargin)/5*i-borderMargin, paintBorderLeftText);
            //画x轴虚线
            pathDottedLine.moveTo(borderMargin * 2.0f, -(mHeight-yBorderMargin)/5*i-borderMargin);
            pathDottedLine.lineTo(mWidth, -(mHeight-yBorderMargin)/5*i-borderMargin);
            canvas.drawPath(pathDottedLine, paintDottedLine);
        }
        //画左侧y轴
        canvas.drawLine(borderMargin * 2.0f, -yBorderMargin, borderMargin * 2.0f, -mHeight, paintBorder);

        for (int i = 1; i < xValueArray.size(); i++){
            Integer xValue = xValueArray.get(i);
            canvas.drawText(xValue.toString()+"分钟", mWidth/6*i+borderMargin, -borderMargin*0.5f, paintBorderBottomText);
        }
        //画底部x轴
        canvas.drawLine(borderMargin * 2.0f, -yBorderMargin, mWidth, -yBorderMargin, paintBorder);
    }
}
