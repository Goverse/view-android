package com.goverse.customview.recyclercard;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goverse.customview.R;


public class LinearComboCard extends ComboCard {

    @Override
    public int layoutId() {
        return R.layout.layout_linear_combo_card;
    }

    @Override
    public void renderView(Context context, View cardView) {
        super.renderView(context, cardView);

        TextView tvTitle = cardView.findViewById(R.id.tv_title);
        tvTitle.setText("Combo card");
        addCard(new ImageCard(Color.GRAY));
        addCard(new ImageCard(Color.BLACK));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addCard(new ImageCard(Color.YELLOW));
            }
        }, 3000);

    }

    @Override
    public void onLayout(View cardView) {

        LinearLayout cardLayout = cardView.findViewById(R.id.layout_card);
        cardLayout.removeAllViews();
        for (View view : getChildViewList()) {
            if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
            cardLayout.addView(view);
        }
    }

}
