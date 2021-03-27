package com.example.recorddemo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class RecordView extends FrameLayout {
    protected MotionLayout mRecordingGroup;
    protected ImageView mRecordingIcon;
    protected ImageView mCloseIcon;
    protected TextView mRecordingTips;
    protected VoiceView mVoiceView;
    private boolean mAudioCancel;

    public RecordView(Context context) {
        this(context,null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mRecordingGroup.getVisibility() == VISIBLE) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.record_view,this);
        mVoiceView = view.findViewById(R.id.voiceView);
        mRecordingGroup = view.findViewById(R.id.voice_recording_view);
        mRecordingIcon = view.findViewById(R.id.recording_icon);
        mRecordingTips = view.findViewById(R.id.recording_tips);
        mCloseIcon = view.findViewById(R.id.close_icon);
        view.findViewById(R.id.button_first).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                start();
                mRecordingGroup.setVisibility(View.VISIBLE);
                return true;
            }

        });
        mRecordingGroup.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
                Log.e("MotionLayout Started", i + "   ");

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                Log.e("MotionLayout Change", i + "   ");
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                if (i == R.id.start) {

                    mCloseIcon.setImageResource(R.drawable.ic_svg_close_unchecked);
                    ViewGroup.LayoutParams layoutParams = mCloseIcon.getLayoutParams();
                    layoutParams.width = PixelUtils.dip2px(motionLayout.getContext(), 60);
                    layoutParams.height = PixelUtils.dip2px(motionLayout.getContext(), 60);
                    mCloseIcon.setLayoutParams(layoutParams);
                } else {
                    mCloseIcon.setImageResource(R.drawable.ic_svg_close);
                    ViewGroup.LayoutParams layoutParams = mCloseIcon.getLayoutParams();
                    layoutParams.width = PixelUtils.dip2px(motionLayout.getContext(), 70);
                    layoutParams.height = PixelUtils.dip2px(motionLayout.getContext(), 70);
                    mCloseIcon.setLayoutParams(layoutParams);
                }
                Log.e("MotionLayout Completedr", i + "   ");
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
                Log.e("MotionLayout Trigger", i + "   " + v + "");
            }
        });
    }


    int paddingArea = 40;//触摸边距
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mRecordingGroup.getVisibility() == VISIBLE) {
                    Log.e("mRecordingGroup",mCloseIcon.getLeft()+"  "+event.getX() +"  "+mCloseIcon.getRight()+"  "+mCloseIcon.getTop()+"  "+event.getRawY()+"  "+mCloseIcon.getBottom());
                    if (mCloseIcon.getLeft() - paddingArea < event.getX() && event.getX() < mCloseIcon.getRight() + paddingArea
                            && mCloseIcon.getTop() - paddingArea < event.getY() && event.getY() < mCloseIcon.getBottom() + paddingArea) {
                        mRecordingGroup.transitionToEnd();
                    //    Log.e("mRecordingGroup","   ACTION_MOVE");
                    } else {
                        mRecordingGroup.transitionToStart();
                  //      Log.e("mRecordingGroup","   ACTION_MOVE1");
                    }
                    return true;
                }
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVoiceView.stopRecord();
                mRecordingGroup.setVisibility(GONE);
                onRecordStop(mCloseIcon.getLeft() - paddingArea < event.getRawX() && event.getRawX() < mCloseIcon.getRight() + paddingArea
                        && mCloseIcon.getTop() - paddingArea < event.getRawY() && event.getRawY() < mCloseIcon.getBottom() + paddingArea);
                mRecordingGroup.transitionToStart();

                return true;
            default:
                return super.onTouchEvent(event);

        }
    }

    private void onRecordStop(boolean b) {
        mAudioCancel = b;
//        if (mChatInputHandler != null) {
//            mChatInputHandler.onRecordStatusChanged(ChatInputHandler.RECORD_STOP);
//        }
        AudioPlayer.getInstance().stopRecord();

    }

    public void start() {
        AudioPlayer.getInstance().stopPlay();
        mRecordingGroup.setVisibility(View.VISIBLE);
        mVoiceView.startRecord(0);
        // mRecordingIcon.setImageResource(R.drawable.recording_volume);
//                        mVolumeAnim = (AnimationDrawable) mRecordingIcon.getDrawable();
//                        mVolumeAnim.start();
        mRecordingTips.setTextColor(Color.WHITE);
        //     mRecordingTips.setText("手指上滑，取消发送");
    }

    public interface ChatInputHandler {

        int RECORD_START = 1;
        int RECORD_STOP = 2;
        int RECORD_CANCEL = 3;
        int RECORD_TOO_SHORT = 4;
        int RECORD_FAILED = 5;

        void onInputAreaClick();

        void onRecordStatusChanged(int status);

        void onMicDbCallback(double db);
    }

}
