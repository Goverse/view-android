package com.goverse.customview.recyclercard;

import android.content.Context;
import android.view.View;

public interface ICardRender {

    /**
     * Interface definition for rendering a card.
     * @param context context
     * @param cardView cardView
     */
    void renderView(Context context, View cardView);
}
