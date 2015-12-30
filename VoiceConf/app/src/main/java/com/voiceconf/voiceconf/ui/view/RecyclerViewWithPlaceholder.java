package com.voiceconf.voiceconf.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by Attila Blenesi on 30 Dec 2015
 */
public class RecyclerViewWithPlaceholder extends RecyclerView {
    private View mEmptyView;
    private boolean mFirstStart; //Handles the initial (hidden) state of the drawable.

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyView(false);
        }
    };

    public RecyclerViewWithPlaceholder(Context context) {
        super(context);
        mFirstStart = true;
    }

    public RecyclerViewWithPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFirstStart = true;
    }

    public RecyclerViewWithPlaceholder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mFirstStart = true;
    }

    /**
     * @param emptyView Designate a view as the empty view. When the backing adapter has no
     *                  data this view will be made visible and the recycler view hidden.
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mDataObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mDataObserver);
        }
        super.setAdapter(adapter);
        updateEmptyView(false);
    }

    public void updateEmptyView(boolean show) {
        if (show) {
            mFirstStart = false;
        }
        if (!mFirstStart) {
            if (mEmptyView != null && getAdapter() != null) {
                boolean showEmptyView = getAdapter().getItemCount() == 0;
                if (mEmptyView instanceof ViewStub && showEmptyView) {
                    mEmptyView = ((ViewStub) mEmptyView).inflate();
                }
                mEmptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
            }
            mFirstStart = false;
        }
    }
}
