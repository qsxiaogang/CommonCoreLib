package com.ccclubs.common.adapter.recycler;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

/**
 * Adapter that should be used.
 */
public abstract class SuperAdapter<T> extends BaseSuperAdapter<T, BaseViewHolder> {

    /**
     * Constructor for single itemView type.
     */
    public SuperAdapter(Context context, List<T> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    /**
     * Constructor for multiple itemView types.
     */
    public SuperAdapter(Context context, List<T> items, IMultiItemViewType<T> multiItemViewType) {
        super(context, items, multiItemViewType);
    }

    @Override
    public BaseViewHolder onCreate(ViewGroup parent, int viewType) {
        final BaseViewHolder holder;
        if (viewType == TYPE_HEADER && hasHeaderView()) {
            return new BaseViewHolder(getHeaderView());
        } else if (viewType == TYPE_FOOTER && hasFooterView()) {
            return new BaseViewHolder(getFooterView());
        }

        if (mMultiItemViewType != null) {
            holder = new BaseViewHolder(mLayoutInflater.inflate(mMultiItemViewType.getLayoutId(viewType),
                    parent, false));
        } else {
            holder = new BaseViewHolder(mLayoutInflater.inflate(mLayoutResId, parent, false));
        }

        return holder;
    }
}
