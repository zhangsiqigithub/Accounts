package com.dragon.accounts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dragon.accounts.model.accountbook.AccountBookListFactory;
import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;
import com.dragon.accounts.model.accountbook.viewholder.IAccountBookViewHolder;

import java.util.List;

public class AccountBookListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<IAccountBookInfo> list;

    public AccountBookListAdapter(Context context, List<IAccountBookInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AccountBookListFactory.getHolder(mContext, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && list != null && getItemCount() > 0 && position < getItemCount()) {
            IAccountBookViewHolder iSettingListViewHolder = (IAccountBookViewHolder) holder;
            iSettingListViewHolder.buildView(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null) {
            IAccountBookInfo iAccountBookInfo = list.get(position);
            if (iAccountBookInfo != null)
                return iAccountBookInfo.getType();
        }
        return super.getItemViewType(position);
    }
}
