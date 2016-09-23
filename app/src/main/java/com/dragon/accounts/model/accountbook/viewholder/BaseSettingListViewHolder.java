package com.dragon.accounts.model.accountbook.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;

public abstract class BaseSettingListViewHolder extends RecyclerView.ViewHolder implements IAccountBookViewHolder, View.OnClickListener {

    protected Context mContext;
    protected IAccountBookInfo item;

    public BaseSettingListViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    @Override
    public void buildView(IAccountBookInfo item) {
        this.item = item;
    }

    @Override
    public void onClick(View v) {
        if (item != null) {
            IAccountBookInfo.Callback callback = item.getCallback();
            if (callback != null) {
                callback.onListItemClick(getItemViewType(), getAdapterPosition());
            }
        }
    }
}
