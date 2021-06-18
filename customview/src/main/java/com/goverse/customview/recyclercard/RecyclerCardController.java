package com.goverse.customview.recyclercard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goverse.customview.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerCardController {

    private final String TAG = getClass().getSimpleName();

    private WeakReference<Context> mContext;

    private CardAdapter mCardAdapter;

    private List<Card> mCardList = new ArrayList<>();

    private Map<Card, View> mCardConvertViewMap = new HashMap<>();

    private RecyclerCardLayout mRecyclerCardLayout;

    private CardObserver observer = new CardObserver() {
        @Override
        public void onCardChanged(Card card, Card.CardObservable cardObservable) {
            super.onCardChanged(card, cardObservable);
            int position = mCardList.indexOf(card);
            if (!mRecyclerCardLayout.isComputingLayout()) mCardAdapter.notifyItemChanged(position);
        }
    };

    public RecyclerCardController(Context context, RecyclerCardLayout recyclerCardLayout) {

        mContext = new WeakReference<>(context);
        mCardAdapter = new CardAdapter();
        mRecyclerCardLayout = recyclerCardLayout;
        recyclerCardLayout.setLayoutManager(new LinearLayoutManager(mContext.get()));
        recyclerCardLayout.setAdapter(mCardAdapter);
        recyclerCardLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

            }

            @Override
            public void onViewDetachedFromWindow(View view) {

                for (Card card : mCardList) {
                    if (card.isShowing()) card.onHidden();
                    card.onDestory();
                    card.unRegisterObservable(observer);
                }
            }
        });
    }

    final class CardHolder extends RecyclerView.ViewHolder {

        private Card mCard;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindCard(Card card) {
            mCard = card;
        }

        public Card getCard() {
            return mCard;
        }

        public void onViewAttached() {
            if (mCard != null) {
                mCard.onShow();
            }
        }

        public void onViewDetached() {
            if (mCard != null) {
                mCard.onHidden();
            }
        }

        public void setVisibility(boolean visibility) {

            RecyclerCardLayout.LayoutParams layoutParams = (RecyclerCardLayout.LayoutParams) itemView.getLayoutParams();
            if (visibility) {
                layoutParams.width = RecyclerCardLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = RecyclerCardLayout.LayoutParams.WRAP_CONTENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                layoutParams.height = 0;
                layoutParams.width = 0;
                itemView.setVisibility(View.GONE);
            }
            itemView.setLayoutParams(layoutParams);
        }
    }

    final class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        @NonNull
        @Override
        public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardHolder cardHolder = new CardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_card_layout, parent, false));
            Log.d(TAG, "onCreateViewHolder: " + cardHolder);
            return cardHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CardHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + holder);
            final Card card = mCardList.get(position);
            holder.bindCard(card);
            View convertView = mCardConvertViewMap.get(card);
            if (convertView == null) {
                convertView = View.inflate(mContext.get(), card.layoutId(), null);
                card.registerObservable(observer);
                card.renderView(mContext.get(), convertView);
                mCardConvertViewMap.put(card, convertView);
            }
            holder.setVisibility(card.getVisibility());
            convertView.setOnClickListener(view -> card.onCardClick(view));
            ((ViewGroup)(holder.itemView)).removeAllViews();
            ViewParent parent = convertView.getParent();
            if (parent != null && parent instanceof ViewGroup) ((ViewGroup)(parent)).removeView(convertView);
            ((ViewGroup)(holder.itemView)).addView(convertView);
        }

        @Override
        public int getItemCount() {
            return mCardList.size();
        }

        @Override
        public void onViewAttachedToWindow(@NonNull CardHolder holder) {
            super.onViewAttachedToWindow(holder);
            Log.d(TAG, "onViewAttachedToWindow: " + holder);
            holder.onViewAttached();
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull CardHolder holder) {
            super.onViewDetachedFromWindow(holder);
            Log.d(TAG, "onViewDetachedFromWindow: " + holder);
            holder.onViewDetached();
        }

        @Override
        public void onViewRecycled(@NonNull CardHolder holder) {
            super.onViewRecycled(holder);
            Log.d(TAG, "onViewRecycled: " + holder);
            ((ViewGroup)(holder.itemView)).removeAllViews();
        }
    }

    public void addCard(Card card) {

        mCardList.add(card);
        mCardAdapter.notifyDataSetChanged();
    }

    public void addCardList(List<Card> cardList) {
        mCardList.clear();
        mCardList.addAll(cardList);
        mCardAdapter.notifyDataSetChanged();
    }

    public List<Card> getCardList() {
        return mCardList;
    }

    public RecyclerCardLayout getRecyclerCardLayout() {
        return mRecyclerCardLayout;
    }

    static abstract class CardObserver {
        public void onCardChanged(Card card, Card.CardObservable cardObservable) {}
    }
}
