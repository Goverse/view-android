package com.goverse.customview.recyclercard;

import android.content.Context;
import android.database.Observable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public abstract class Card implements ICardRender, ICardRecycler, OnCardClickListener{

    private CardObservable observable = new CardObservable();

    private final String TAG = this.getClass().getSimpleName();

    private SparseArray<View> mCardViewMap = new SparseArray<>();

    private boolean mIsShowing = false;

    private boolean mVisibility = true;

    public abstract int layoutId();

    public boolean isShowing() {
        return mIsShowing;
    }

    public boolean getVisibility() {
        return mVisibility;
    }

    public void setVisibility(boolean visibility) {
        if (mVisibility != visibility) {
            mVisibility = visibility;
            this.observable.notifyCardChanged(this, this.observable);
        }
    }

    public void registerObservable(RecyclerCardController.CardObserver cardObserver) {
        if (!this.observable.hasObserver(cardObserver)) {
            this.observable.registerObserver(cardObserver);
        }
    }

    public void unRegisterObservable(RecyclerCardController.CardObserver cardObserver) {
        if (this.observable.hasObserver(cardObserver)) {
            this.observable.unregisterObserver(cardObserver);
        }
    }

    @Override
    public void onShow() {
        Log.d(TAG, "onShow: " + this.getClass().getSimpleName());
        mIsShowing = true;
    }

    @Override
    public void onHidden() {
        Log.d(TAG, "onHidden: " + this.getClass().getSimpleName());
        mIsShowing = false;
    }

    @Override
    public void renderView(Context context, View cardView) {
        Log.d(TAG, "renderView: " + this.getClass().getSimpleName());
    }

    private View mCardView;

    protected final View findViewById(View cardView, int resId) {

        if (mCardView == cardView) {
            View view = mCardViewMap.get(resId);
            if (view == null) {
                view = cardView.findViewById(resId);
                mCardViewMap.put(resId, view);
            }
            return view;
        }
        mCardView = cardView;
        mCardViewMap.clear();
        View view = mCardView.findViewById(resId);
        mCardViewMap.put(resId, view);
        return view;
    }

    @Override
    public void onCardClick(View view) {
        Log.d(TAG, "onCardClick: " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestory() {
        Log.d(TAG, "onDestory: " + this.getClass().getSimpleName());
    }

    static class CardObservable extends Observable<RecyclerCardController.CardObserver> {

        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public boolean hasObserver(RecyclerCardController.CardObserver cardObserver) {
            synchronized(mObservers) {
                if (mObservers.contains(cardObserver)) {
                    return true;
                }
            }
            return false;
        }

        public void notifyCardChanged(Card card, CardObservable cardObservable) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onCardChanged(card, cardObservable);
            }
        }

    }
}
