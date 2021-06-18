package com.goverse.customview.recyclercard;

import android.content.Context;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * 组合卡片，用于实现卡片内嵌其他卡片的效果
 */
public abstract class ComboCard extends Card {

    private final String TAG = "ComboCard";
    private Card[] cards;
    private List<View> childViewList = new ArrayList<>();
    private View cardView;
    private Context context;

    public ComboCard(Card ...cards) {
        this.cards = cards;
    }

    public ComboCard() {

    }

    @Override
    public void renderView(Context context, View cardView) {
        super.renderView(context, cardView);
        Log.d(TAG, "renderView: " + this.getClass().getSimpleName());
        this.cardView = cardView;
        this.context = context;
        if (cards != null) {
            for (Card card : cards) addCard(card);
        }
    }

    public void addCard(Card card) {
        Log.d(TAG, "addCard: " + card.getClass().getSimpleName());
        View view = View.inflate(context, card.layoutId(), null);
        card.renderView(context, view);
        childViewList.add(view);
        onLayout(cardView);
    }

    public abstract void onLayout(View cardView);

    @Override
    public void onShow() {
        super.onShow();
        dispatchChildCardVisibility(true);
    }

    @Override
    public void onHidden() {
        super.onHidden();
        dispatchChildCardVisibility(false);
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (cards == null) return;
        for (Card card : cards) {
            card.onDestory();
        }
    }

    public List<View> getChildViewList() {
        return childViewList;
    }

    private void dispatchChildCardVisibility(boolean isShow) {
        Log.d(TAG, "dispatchChildCardVisibility: " + isShow);
        if (cards == null) return;
        for (Card card : cards) {
            if (isShow) card.onShow();
            else card.onHidden();
        }
    }
}
