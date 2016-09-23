package com.dragon.accounts.setting.model.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragon.accounts.model.IAccountBook;

public abstract class BaseSettingListViewHolder extends RecyclerView.ViewHolder implements ISettingListViewHolder, View.OnClickListener {

    protected Context mContext;
    protected IAccountBook item;

    public BaseSettingListViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    @Override
    public void buildView(IAccountBook item) {
        this.item = item;
    }

    @Override
    public void onClick(View v) {
        if (item != null) {
            IAccountBook.Callback callback = item.getCallback();
            if (callback != null) {
                callback.onListItemClick(getItemViewType(), getAdapterPosition());
            }
        }
    }
}
