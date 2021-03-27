package com.example.recorddemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoTouchMoveView extends androidx.appcompat.widget.AppCompatTextView {
    public NoTouchMoveView(@NonNull Context context) {
        super(context);
    }

    public NoTouchMoveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchMoveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
