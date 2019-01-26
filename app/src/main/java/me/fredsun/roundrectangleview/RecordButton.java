package me.fredsun.roundrectangleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * @author ArcherYc
 * @date on 2018/7/11  下午2:28
 * @mail 247067345@qq.com
 */
public class RecordButton extends View {
    private Bitmap bitmapPhoto;
    private Context mContext;

    private Paint mRectPaint;

    private Paint mCirclePaint;

    private float corner;
    private float circleRadius;
    private float circleStrokeWidth;
    private float rectWidth;
    private float bitmapWidth;

    private float mMinCircleRadius;
    private float mMaxCircleRadius;
    private float mMinRectWidth;
    private float mMaxRectWidth;
    private float mMinCorner;
    private float mMaxCorner;
    private float mMinCircleStrokeWidth;
    private float mMaxCircleStrokeWidth;
    private float mMinBitmapWidth;
    private float mMaxBitmapWidth;


    private RectF mRectF = new RectF();
    private RectF mBitmapRectF = new RectF();

    private RecordMode mRecordMode = RecordMode.ORIGIN;

    private AnimatorSet mBeginAnimatorSet = new AnimatorSet();

    private AnimatorSet mEndAnimatorSet = new AnimatorSet();

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private Handler mHandler = new Handler();

//    private ClickRunnable mClickRunnable = new ClickRunnable();

    private OnRecordStateChangedListener mOnRecordStateChangedListener;

    private float mInitX;

    private float mInitY;

    private float mDownRawX;

    private float mDownRawY;

    private float mInfectionPoint;

    private ScrollDirection mScrollDirection;

    private boolean mHasCancel = false;
    private int rectColor;
    private int circleColor;
    private float multipleRate = 0.807f;
    private Rect mRectBitmap;
    private int mLittleResourcesId;
    private int mLargeResourcesId;

    public final static int ModeCamera = 1;
    public final static int ModeVideo = 2;
    public final static int ModeClip = 3;
    private int mCurrentMode = 0;
    private int mMaxCameraRectWidth;
    private int mMinCameraRectWidth;
    private long inSaveActTime;


    public RecordButton(Context context) {
        this(context, null);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs, defStyleAttr);
        bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_douyin_video_large);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//        //获取我们自定义的样式属性
//        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DouyinButtonStyle, defStyleAttr, 0);
//        int n = array.getIndexCount();
//        for (int i = 0; i < n; i++) {
//            int attr = array.getIndex(i);
//            switch (attr) {
//                case R.styleable.DouyinButtonStyle_RectColor:
//                    // 默认颜色设置为黑色
//                    rectColor = array.getColor(attr, Color.parseColor("#D8D8D8"));
//                    break;
//                case R.styleable.DouyinButtonStyle_CircleColor:
//                    // 默认颜色设置为黑色
//                    circleColor = array.getColor(attr, Color.parseColor("#F7F7F7"));
//                    break;
//            }
//        }
//        array.recycle();
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.parseColor("#FF8D00"));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#66FF8d00"));

        mMinCircleStrokeWidth = dip2px(mContext, 3);
        mMaxCircleStrokeWidth = dip2px(mContext, 12);
        circleStrokeWidth = mMinCircleStrokeWidth;
        mCirclePaint.setStrokeWidth(circleStrokeWidth);
        mCurrentMode = ModeVideo;
        mLittleResourcesId = R.drawable.icon_douyin_video_little;
        mLargeResourcesId= R.drawable.icon_douyin_video_large;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        mMaxRectWidth = width / 3;
        mMinRectWidth = mMaxRectWidth * 0.6f;
        
        mMaxCameraRectWidth = width / 3;
        mMinCameraRectWidth = width / 4;

        mMinCircleRadius = mMaxRectWidth / 2 + mMinCircleStrokeWidth + dip2px(mContext, 5);
        mMaxCircleRadius = width / 2 - mMaxCircleStrokeWidth;

        mMinCorner = dip2px(mContext, 5);
        mMaxCorner = mMaxRectWidth / 2;

        mMinBitmapWidth = mMinRectWidth;
        mMaxBitmapWidth = mMaxRectWidth * multipleRate;

        if (rectWidth == 0) {
            rectWidth = mMaxRectWidth;
        }
        if (circleRadius == 0) {
            circleRadius = mMinCircleRadius;
        }
        if (corner == 0) {
            corner = rectWidth / 2;
        }

        if (bitmapWidth == 0){
            bitmapWidth = mMaxBitmapWidth;
        }


        mCirclePaint.setColor(Color.parseColor("#33ff8D00"));
        canvas.drawCircle(centerX, centerY, circleRadius, mCirclePaint);
        mCirclePaint.setXfermode(mXfermode);

        mCirclePaint.setColor(Color.parseColor("#000000"));
        canvas.drawCircle(centerX, centerY, circleRadius - circleStrokeWidth, mCirclePaint);
        mCirclePaint.setXfermode(null);
        Log.i("rectWidth", rectWidth+"");

        mRectF.left = centerX - rectWidth / 2;
        mRectF.right = centerX + rectWidth / 2;
        mRectF.top = centerY - rectWidth / 2;
        mRectF.bottom = centerY + rectWidth / 2;

//        mBitmapRectF.left = mRectF.left * multipleRate;
//        mBitmapRectF.right = mRectF.right * multipleRate;
//        mBitmapRectF.top = mRectF.top * multipleRate;
//        mBitmapRectF.bottom = mRectF.bottom * multipleRate;
//
//        mBitmapRectF.left = mRectF.left - rectWidth / 2 * multipleRate;
//        mBitmapRectF.right = mRectF.right + rectWidth / 2 * multipleRate;
//        mBitmapRectF.top = mRectF.top - rectWidth / 2 * multipleRate;
//        mBitmapRectF.bottom = mRectF.bottom + rectWidth / 2 * multipleRate;

//        mBitmapRectF.left = mRectF.left;
//        mBitmapRectF.right = mRectF.right;
//        mBitmapRectF.top = mRectF.top;
//        mBitmapRectF.bottom = mRectF.bottom;

        mBitmapRectF.left = centerX - bitmapWidth/2;
        mBitmapRectF.right = centerX + bitmapWidth/2;
        mBitmapRectF.top = centerY - bitmapWidth/2;
        mBitmapRectF.bottom = centerY + bitmapWidth/2;

        canvas.drawRoundRect(mRectF, corner, corner, mRectPaint);
        mRectBitmap = new Rect(0, 0, bitmapPhoto.getWidth(), bitmapPhoto.getHeight());
        canvas.drawBitmap(bitmapPhoto, mRectBitmap, mBitmapRectF,  mRectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentMode == ModeCamera){
                    if ((System.currentTimeMillis() - inSaveActTime) < 2000) break;
                    inSaveActTime = System.currentTimeMillis();
                    mOnRecordStateChangedListener.onDouYinCameraClick();
                    mRecordMode = RecordMode.ORIGIN;
                    mBeginAnimatorSet.cancel();
                    clickCamera();
                }else {
                    if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
//                        mDownRawX = event.getRawX();
//                        mDownRawY = event.getRawY();
                        startBeginAnimation();
//                        mHandler.postDelayed(mClickRunnable, 200);
                        if (mCurrentMode == ModeVideo){
                            mOnRecordStateChangedListener.onDouYinVideoStart();
                        }
                        if (mCurrentMode == ModeClip){
                            mOnRecordStateChangedListener.onDouYinClipStart();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mHasCancel) {
                    if (mRecordMode == RecordMode.LONG_CLICK) {
//                        ScrollDirection mOldDirection = mScrollDirection;
//                        float oldY = getY();
//                        setX(mInitX+event.getRawX()-mDownRawX);
//                        setY(mInitY+event.getRawY()-mDownRawY);
//                        float newY = getY();
//
//                        if (newY <= oldY) {
//                            mScrollDirection = ScrollDirection.UP;
//                        } else {
//                            mScrollDirection = ScrollDirection.DOWN;
//                        }
//
//                        if (mOldDirection != mScrollDirection) {
//                            mInfectionPoint = oldY;
//                        }
//                        float zoomPercentage = (mInfectionPoint - getY()) / mInitY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mHasCancel) {
                    if (mCurrentMode == ModeCamera){

                    }else {
                        if (mRecordMode == RecordMode.LONG_CLICK) {
                        } else if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
                            mRecordMode = RecordMode.SINGLE_CLICK;
                        } else if (mRecordMode == RecordMode.SINGLE_CLICK && inEndRange(event)) {
                            if (mCurrentMode == ModeVideo){
                                mOnRecordStateChangedListener.onDouYinVideoFinish();
                            }
                            if (mCurrentMode == ModeClip){
                                mOnRecordStateChangedListener.onDouYinClipFinish();
                            }
                            resetSingleClick();
                        }
                    }

                } else {
                    mHasCancel = false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean inBeginRange(MotionEvent event) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        int minX = (int) (centerX - mMinCircleRadius);
        int maxX = (int) (centerX + mMinCircleRadius);
        int minY = (int) (centerY - mMinCircleRadius);
        int maxY = (int) (centerY + mMinCircleRadius);
        boolean isXInRange = event.getX() >= minX && event.getX() <= maxX;
        boolean isYInRange = event.getY() >= minY && event.getY() <= maxY;
        return isXInRange && isYInRange;
    }

    private boolean inEndRange(MotionEvent event) {
        int minX = 0;
        int maxX = getMeasuredWidth();
        int minY = 0;
        int maxY = getMeasuredHeight();
        boolean isXInRange = event.getX() >= minX && event.getX() <= maxX;
        boolean isYInRange = event.getY() >= minY && event.getY() <= maxY;
        return isXInRange && isYInRange;
    }

    private void resetLongClick() {
        mRecordMode = RecordMode.ORIGIN;
        mBeginAnimatorSet.cancel();
        startEndAnimation();
        setX(mInitX);
        setY(mInitY);
    }

    private void resetSingleClick() {
        mRecordMode = RecordMode.ORIGIN;
        mBeginAnimatorSet.cancel();
        startEndAnimation();
    }

    public void reset() {
        if (mRecordMode == RecordMode.LONG_CLICK) {
            resetLongClick();
        } else if (mRecordMode == RecordMode.SINGLE_CLICK) {
            resetSingleClick();
        } else if (mRecordMode == RecordMode.ORIGIN) {
            if (mBeginAnimatorSet.isRunning()) {
                mHasCancel = true;
                mBeginAnimatorSet.cancel();
                startEndAnimation();
//                mHandler.removeCallbacks(mClickRunnable);
                mRecordMode = RecordMode.ORIGIN;
            }
        }
    }

    public void startClockRecord() {
        if (mRecordMode == RecordMode.ORIGIN) {
            startBeginAnimation();
            mRecordMode = RecordMode.SINGLE_CLICK;
        }
    }

    private void clickCamera(){
        AnimatorSet startAnimatorSet = new AnimatorSet();
        ObjectAnimator zoomOutAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMaxCameraRectWidth, mMinCameraRectWidth)
                .setDuration(200);
        ObjectAnimator zoomInAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMinCameraRectWidth, mMaxCameraRectWidth)
                .setDuration(200);
        startAnimatorSet.playSequentially(zoomOutAnimator, zoomInAnimator);
        startAnimatorSet.start();
    }

    private void startBeginAnimation() {
        AnimatorSet startAnimatorSet = new AnimatorSet();
        ObjectAnimator cornerAnimator = ObjectAnimator.ofFloat(this, "corner",
                mMaxCorner, mMinCorner)
                .setDuration(200);
        ObjectAnimator rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMaxRectWidth, mMinRectWidth)
                .setDuration(200);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
                mMinCircleRadius, mMaxCircleRadius)
                .setDuration(200);
        ObjectAnimator bitmapAnimator = ObjectAnimator.ofFloat(this, "bitmapWidth",
                mMaxBitmapWidth, mMinBitmapWidth)
                .setDuration(200);
        startAnimatorSet.playTogether(cornerAnimator, rectSizeAnimator, radiusAnimator, bitmapAnimator);

        startAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后替换图片
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), mLittleResourcesId);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
                mMinCircleStrokeWidth, mMaxCircleStrokeWidth, mMinCircleStrokeWidth)
                .setDuration(1500);
        circleWidthAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mBeginAnimatorSet.playSequentially(startAnimatorSet, circleWidthAnimator);
        mBeginAnimatorSet.start();
    }

    private void startEndAnimation() {
        ObjectAnimator cornerAnimator = ObjectAnimator.ofFloat(this, "corner",
                mMinCorner, mMaxCorner)
                .setDuration(200);
        ObjectAnimator rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMinRectWidth, mMaxRectWidth)
                .setDuration(200);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
                mMaxCircleRadius, mMinCircleRadius)
                .setDuration(200);
        ObjectAnimator circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
                mMaxCircleStrokeWidth, mMinCircleStrokeWidth)
                .setDuration(200);
        ObjectAnimator bitmapAnimator = ObjectAnimator.ofFloat(this, "bitmapWidth",
                mMinBitmapWidth, mMaxBitmapWidth)
                .setDuration(200);

        mEndAnimatorSet.playTogether(cornerAnimator, rectSizeAnimator, radiusAnimator, circleWidthAnimator, bitmapAnimator);
        mEndAnimatorSet.start();
        mEndAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), mLargeResourcesId);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setCorner(float corner) {
        this.corner = corner;
        invalidate();
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
    }

    public void setCircleStrokeWidth(float circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
        invalidate();
    }

    public void setBitmapPhoto(int mode){
        if (mRecordMode == RecordMode.SINGLE_CLICK){
            if (mCurrentMode == ModeVideo){
                mOnRecordStateChangedListener.onDouYinVideoFinish();
            }
            if (mCurrentMode == ModeClip){
                mOnRecordStateChangedListener.onDouYinClipFinish();
            }
            resetSingleClick();
        }
        mCurrentMode = mode;
        switch (mode){
            case ModeCamera:
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_douyin_camera);
                this.mLargeResourcesId = R.drawable.icon_douyin_camera;
                this.mLittleResourcesId = R.drawable.icon_douyin_camera;
                break;
            case ModeVideo:
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_douyin_video_large);
                this.mLargeResourcesId = R.drawable.icon_douyin_video_large;
                this.mLittleResourcesId = R.drawable.icon_douyin_video_little;
                break;
            case ModeClip:
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_douyin_clip_large);
                this.mLargeResourcesId = R.drawable.icon_douyin_clip_large;
                this.mLittleResourcesId = R.drawable.icon_douyin_clip_little;
                break;
            default:
                mCurrentMode = ModeCamera;
                bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.icon_douyin_camera);
                this.mLargeResourcesId = R.drawable.icon_douyin_camera;
                this.mLittleResourcesId = R.drawable.icon_douyin_camera;
                break;
        }

        invalidate();
    }

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
        invalidate();
    }

    public void setBitmapWidth(float bitmapWidth){
        this.bitmapWidth = bitmapWidth;
    }

//    class ClickRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            if (!mHasCancel) {
//                mRecordMode = RecordMode.LONG_CLICK;
//                mInitX = getX();
//                mInitY = getY();
//                mInfectionPoint = mInitY;
//                mScrollDirection = ScrollDirection.UP;
//            }
//        }
//    }

    public void setOnRecordStateChangedListener(OnRecordStateChangedListener listener) {
        this.mOnRecordStateChangedListener = listener;
    }

    public interface OnRecordStateChangedListener {
        void onDouYinCameraClick();
        void onDouYinVideoStart();
        void onDouYinVideoFinish();
        void onDouYinClipStart();
        void onDouYinClipFinish();
    }

    private enum RecordMode {
        /**
         * 单击录制模式
         */
        SINGLE_CLICK,
        /**
         * 长按录制模式
         */
        LONG_CLICK,
        /**
         * 初始化
         */
        ORIGIN;

        RecordMode() {

        }
    }

    private enum ScrollDirection {
        /**
         * 滑动方向 上
         */
        UP,
        /**
         * 滑动方向 下
         */
        DOWN;

        ScrollDirection() {

        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
