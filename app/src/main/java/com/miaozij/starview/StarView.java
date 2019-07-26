package com.miaozij.starview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class StarView extends View {
    private int mStarCount = 5;
    private Bitmap mStarNormalBitmap;
    private Bitmap mStarFocusBitmap;
    private int mStarMargin;
    private int mStarFocusCount;
    public StarView(Context context) {
        this(context,null);
    }

    public StarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarView);
        mStarCount = typedArray.getInt(R.styleable.StarView_starCount,mStarCount);
        mStarMargin = (int)typedArray.getDimension(R.styleable.StarView_starMargin,mStarMargin);
        int mStarNormalId = typedArray.getResourceId(R.styleable.StarView_starNormal, 0);
        if(mStarNormalId == 0){
            throw new RuntimeException("请先设置属性");
        }
        int mStarFocusId = typedArray.getResourceId(R.styleable.StarView_starFocus,0);
        if(mStarFocusId == 0){
            throw new RuntimeException("请先设置属性");
        }
        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), mStarNormalId);
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(),mStarFocusId);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = mStarNormalBitmap.getWidth();
        //高度 为 图片的高度 宽度为 图片数量乘以图片宽度
        width = (mStarCount-1) * mStarMargin + getPaddingLeft() + getPaddingRight() + width * mStarCount;
        int height = mStarNormalBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for( int i = 0 ; i < mStarCount ; i ++ ) {
            int width = mStarNormalBitmap.getWidth() * i + getPaddingLeft() + i * mStarMargin;
            canvas.drawBitmap(mStarNormalBitmap,width,0,null);
            if(mStarFocusCount > i){
                canvas.drawBitmap(mStarFocusBitmap,width,0,null);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();//相对控件按下位置
//                event.getRawX() 相对屏幕按下位置
                Log.d("TAG==>按下","downX=="+downX +"   padding==" +getPaddingLeft());
                doUpdate(downX);
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                doUpdate(x1);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    private void doUpdate(float downX){
        int count = 0;
        if(0 < downX && downX < getWidth()){
            downX = downX - getPaddingLeft();
            if(downX <= 0){
                count = 0;
            }else {
                int bx = mStarNormalBitmap.getWidth() + mStarMargin;
                int a = (int)( downX / bx );
                count = a + 1;
            }
            Log.d("TAG","count="+count + "  mStarCount="+mStarFocusCount);
            if(count != mStarFocusCount){
                mStarFocusCount = count;
                invalidate();
            }
        }
    }
}
