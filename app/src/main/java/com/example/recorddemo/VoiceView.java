package com.example.recorddemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VoiceView extends View {

    private static final int LINE_WIDTH = 4;//宽度
    private Paint paint = new Paint();
    private Runnable task;
    private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private RectF rectLeft = new RectF();

    int updateSpeed;
    private int lineColor;
    private float lineWidth;
    private final float lineSpace = 3;
    private static final int UPDATE_INTERVAL_TIME = 100;//更新时间
    private static final int[] DEFAULT_WAVE_HEIGHT = {2, 2, 2, 2, 2};
    private LinkedList<Integer> mWaveList = new LinkedList<>();
    private float maxDb;
    private static final int MAX_WAVE_HEIGHT = 12;//最大高
    private static final int MIN_WAVE_HEIGHT = 2;//最小高

    public VoiceView(Context context) {
        super(context);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, context);
        resetView(mWaveList, DEFAULT_WAVE_HEIGHT);
        task = new LineJitterTask();
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(AttributeSet attrs, Context context) {
        //获取布局属性里的值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.VoiceView);
        lineColor = mTypedArray.getColor(R.styleable.VoiceView_voiceLineColor, context.getResources().getColor(R.color.black));
        lineWidth = mTypedArray.getDimension(R.styleable.VoiceView_voiceLineWidth, LINE_WIDTH);
        updateSpeed = mTypedArray.getColor(R.styleable.VoiceView_updateSpeed, UPDATE_INTERVAL_TIME);
        mTypedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int heightCentre = getHeight() / 2;
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        int maxSize = (int) (getWidth() / (lineWidth + lineSpace));
        for (int i = 0; i < maxSize; i++) {
            rectLeft.left = i * (lineWidth + lineSpace);
            rectLeft.right = i * (lineWidth + lineSpace) + lineWidth;
            rectLeft.top = heightCentre - mWaveList.get(i % 5) / 2;
            rectLeft.bottom = heightCentre + mWaveList.get(i % 5) / 2;
            canvas.drawRoundRect(rectLeft, 6, 6, paint);
        }

    }

    private void resetView(LinkedList<Integer> list, int[] array) {
        list.clear();
        for (int anArray : array) {
            list.add(anArray);
        }
    }

    public boolean isStart = false;

    class LineJitterTask implements Runnable {
        @Override
        public void run() {
            while (isStart) {
                refreshElement();
                try {
                    Thread.sleep(updateSpeed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    }

    public synchronized void startRecord(double db) {
        isStart = true;
        //  if (db!=0){
        maxDb = (float) db;
        Log.e("VoiceView", db + "");
        executorService.execute(task);
        //    }
    }

    synchronized void refreshElement() {
        Random random = new Random();
        maxDb = maxDb == 0 ? 2 : random.nextInt(5) + 2;
        int waveH = MIN_WAVE_HEIGHT + Math.round(maxDb * (MAX_WAVE_HEIGHT - MIN_WAVE_HEIGHT));
        mWaveList.add(0, waveH);
        mWaveList.removeLast();
    }

    public synchronized void stopRecord() {
        isStart = false;
        mWaveList.clear();
        resetView(mWaveList, DEFAULT_WAVE_HEIGHT);
        //   postInvalidate();
    }
}
