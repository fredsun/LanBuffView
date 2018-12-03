package me.fredsun.roundrectangleview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fred on 2018/11/28.
 */

public class ShadowRoundRectangleView extends View {
    private int mHeight;
    private int mWidth;

    public ShadowRoundRectangleView(Context context) {
        super(context, null);
    }

    public ShadowRoundRectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowRoundRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ShadowRoundRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#cccccc"));
        paint.setStyle(Paint.Style.FILL);
        BlurMaskFilter filter = new BlurMaskFilter(50, BlurMaskFilter.Blur.OUTER);
        paint.setMaskFilter(filter);

//        paint.setStyle(Paint.Style.STROKE);
//        paint.setShadowLayer(100, 0, 0 ,Color.BLUE);
        RectF rectf = new RectF(100, 100, 500, 300);
        canvas.drawRoundRect(rectf,20, 20, paint);
    }
}
