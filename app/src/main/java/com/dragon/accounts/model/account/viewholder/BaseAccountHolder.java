package com.dragon.accounts.model.account.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseAccountHolder extends RecyclerView.ViewHolder implements IAccountViewHolder {

    private Context mContext;

    public BaseAccountHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }
}
