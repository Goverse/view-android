package com.goverse.customview.pickview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.goverse.customview.R;

public class PickItemView extends View {

    public interface OnPickedListener {
        void onPicked(boolean isPicked, PickItemView pickItemView);
        void onPickedOutRange();
    }

    public static final String TAG = "PickItemView";

    private PickItemBean pickItemBean;

    private OnPickedListener onPickedListener;

    private volatile boolean picked = false;

    public boolean isPicked() {
        return picked;
    }

    public PickItemBean getPickItemBean() {
        return pickItemBean;
    }

    public PickItemView(Context context, PickItemBean pickItemBean) {
        super(context);
        this.pickItemBean = pickItemBean;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PickItemView---OnClick:" + PickItemView.this.pickItemBean);
                PickItemView.this.setPicked(!PickItemView.this.isPicked());
            }
        });
    }

    public PickItemView(Context context, @Nullable AttributeSet attrs, PickItemBean pickItemBean) {
        super(context, attrs);
        this.pickItemBean = pickItemBean;
    }

    public PickItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, PickItemBean pickItemBean) {
        super(context, attrs, defStyleAttr);
        this.pickItemBean = pickItemBean;
    }

    public void setOnPickedListener(OnPickedListener onPickedListener) {
        this.onPickedListener = onPickedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure---widthMeasureSpec: " + widthMeasureSpec + ",heightMeasureSpec: " + heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw---getHeight: " + canvas.getHeight() + ",getWidth: " + canvas.getWidth());
        setBackgroundResource(picked ? R.drawable.view_picked_bg : R.drawable.view_unpicked_bg);
        if (pickItemBean != null && !TextUtils.isEmpty(this.pickItemBean.getPreferenceName())) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.WHITE);
            paint.setTextSize(sp2px(getContext(), 14));
            float v = paint.measureText(this.pickItemBean.getPreferenceName());
            canvas.drawText(this.pickItemBean.getPreferenceName(), (canvas.getWidth() - v) / 2, canvas.getHeight() / 2, paint);
        }
        startAnimation();
    }

    private void startAnimation() {
        ObjectAnimator animatorInX = ObjectAnimator.ofFloat(this, "scaleX", 0.9f, 0.8f);
        ObjectAnimator animatorInY = ObjectAnimator.ofFloat(this, "scaleY", 0.9f, 0.8f);
        animatorInX.setDuration(300);
        animatorInY.setDuration(300);
        ObjectAnimator animatorOutX = ObjectAnimator.ofFloat(this, "scaleX", 0.7f, 0.9f);
        ObjectAnimator animatorOutY = ObjectAnimator.ofFloat(this, "scaleY", 0.7f, 0.9f);
        animatorOutX.setDuration(300);
        animatorOutY.setDuration(300);
        AnimatorSet animatorSetOut = new AnimatorSet();
        AnimatorSet animatorSetIn = new AnimatorSet();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSetOut.playTogether(animatorOutX, animatorOutY);
        animatorSetIn.playTogether(animatorInX, animatorInY);
        animatorSet.playSequentially(animatorSetOut, animatorSetIn);
        animatorSet.start();
    }

    public void setPicked(boolean isPicked) {
        if (isPicked != picked) {
            picked = isPicked;
            setBackgroundResource(picked ? R.drawable.view_picked_bg : R.drawable.view_unpicked_bg);
            if (onPickedListener != null) onPickedListener.onPicked(picked, this);
        }
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity * spValue + 0.5f);
    }
}
