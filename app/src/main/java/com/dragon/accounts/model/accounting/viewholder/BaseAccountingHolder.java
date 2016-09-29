package com.dragon.accounts.model.accounting.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseAccountingHolder extends RecyclerView.ViewHolder implements IAccountingViewHolder {

    protected Context mContext;

    public BaseAccountingHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }
}
