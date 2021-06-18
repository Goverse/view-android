package com.goverse.customview.recyclercard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerCardLayout extends RecyclerView {

    private final String TAG = getClass().getSimpleName();

    public RecyclerCardLayout(@NonNull Context context) {
        super(context);
    }

    public RecyclerCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (e.getPointerCount() > 1){
                    return false;
                }
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                return super.onInterceptTouchEvent(e);
        }
    }
}
