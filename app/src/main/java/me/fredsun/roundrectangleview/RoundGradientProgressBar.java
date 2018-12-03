package me.fredsun.roundrectangleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by fred on 2018/8/31.
 */

public class RoundGradientProgressBar extends View {
    private Paint paintProgress, paintBackground, paintProgressTail, paintTextHint, paintTextRate;
    private ValueAnimator valueAnimator;
    private RectF rectF;
    private int radius;
    private int strokeWidth = 30;
    private float mAnimatorValue;
    int [] mMinColors={0xFF355EFB, 0xFFFC466B};
    private float degree;
    private float percent;

    public RoundGradientProgressBar(Context context) {
        this(context, null);
    }

    public RoundGradientProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundGradientProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgressTail = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextHint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextRate = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        valueAnimator = ValueAnimator.ofFloat(0,  1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(1000);
        //每过10毫秒 调用一次
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                Log.i("progressAnimaValue", mAnimatorValue+"");
                postInvalidate();
            }
        });
//        degree = 270;
    }

    public void setDegree(float percent){
        this.percent = percent;
        this.degree = percent /100 * 360;
    }

    public void setProgressWidth(int progressWidth){
        this.strokeWidth = progressWidth;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h)/2;
        radius = radius - strokeWidth;
        rectF.set(-radius,-radius,radius,radius);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        paintBackground.setStyle(Paint.Style.STROKE);
        paintBackground.setStrokeWidth(strokeWidth);
        paintBackground.setColor(Color.parseColor("#ffffff"));
        canvas.translate(getWidth()/2, getHeight()/2);
        canvas.drawCircle(0, 0, radius, paintBackground);
        //绘制progress
        SweepGradient mSweepGradient = new SweepGradient(0,
                0, //以圆弧中心作为扫描渲染的中心以便实现需要的效果
                mMinColors, //这是我定义好的颜色数组，包含2个颜色：#35C3D7、#2894DD
                new float[]{0f, degree / 360f - 0.017f});//提前结束渐变色[为了tail不突兀]
        Matrix matrix = new Matrix();
        matrix.setRotate(-90f, 0, 0);
        mSweepGradient.setLocalMatrix(matrix);
        paintProgress.setStyle(Paint.Style.STROKE);
        paintProgress.setShader(mSweepGradient);
        paintProgress.setStrokeWidth(strokeWidth);
        canvas.drawArc(rectF, -90,mAnimatorValue * degree, false, paintProgress);
        //绘制progress尾巴
        paintProgressTail = new Paint();
        paintProgressTail.setAntiAlias(true);
        paintProgressTail.setStrokeWidth(strokeWidth);
        paintProgressTail.setColor(mMinColors[mMinColors.length-1]);
        float tempDegree = degree * mAnimatorValue- 90; //抵消屏幕坐标系差异
        tempDegree = (float) (Math.PI / 180f * tempDegree); //换成弧度
        canvas.drawCircle((float) (rectF.left + rectF.width() / 2f + radius * Math.cos(tempDegree)), //圆心x
                (float) (rectF.top + rectF.height() / 2f + radius * Math.sin(tempDegree)), //圆心y
                strokeWidth/2f,  //半径
                paintProgressTail);
        //绘制命中率标题
        paintTextHint.setTextSize(strokeWidth*0.8f);
        paintTextHint.setTextAlign(Paint.Align.CENTER);
        paintTextHint.setColor(Color.WHITE);
        canvas.drawText("平均命中率",0,-strokeWidth, paintTextHint);
        paintTextRate.setTextSize(strokeWidth*1.8f);
        paintTextRate.setTextAlign(Paint.Align.CENTER);
        paintTextRate.setColor(Color.WHITE);
        canvas.drawText((int)(mAnimatorValue * percent)+"%", 0, strokeWidth, paintTextRate);
    }

}
