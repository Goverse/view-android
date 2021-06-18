package com.goverse.customview.recyclercard;

public interface ICardRecycler {

    /**
     * Interface definition for a callback to be invoked when a card is showing.
     */
    void onShow();

    /**
     * Interface definition for a callback to be invoked when a card is hiding.
     */
    void onHidden();

    /**
     * Interface definition for a callback to be invoked when a card is recycling.
     */
    void onDestory();

}
