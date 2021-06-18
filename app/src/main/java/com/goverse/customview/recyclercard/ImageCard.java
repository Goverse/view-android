package com.goverse.customview.recyclercard;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.goverse.customview.R;

public class ImageCard extends Card {

    private int color;

    public ImageCard(int color) {
        this.color = color;
    }

    @Override
    public int layoutId() {
        return R.layout.card_image;
    }

    @Override
    public void renderView(Context context, View cardView) {
        super.renderView(context, cardView);

        ImageView image = (ImageView) findViewById(cardView, R.id.iv_image);
        image.setImageDrawable(new ColorDrawable(color));
        image.setBackgroundColor(this.color);
    }

    @Override
    public void onShow() {
        super.onShow();
    }
}
