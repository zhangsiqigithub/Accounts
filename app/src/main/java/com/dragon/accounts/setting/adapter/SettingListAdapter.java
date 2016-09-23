package com.dragon.accounts.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dragon.accounts.model.IAccountBook;
import com.dragon.accounts.setting.factory.SettingListFactory;
import com.dragon.accounts.setting.model.viewholder.ISettingListViewHolder;

import java.util.List;

public class SettingListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<IAccountBook> list;

    public SettingListAdapter(Context context, List<IAccountBook> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SettingListFactory.getHolder(mContext, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && list != null && getItemCount() > 0 && position < getItemCount()) {
            ISettingListViewHolder iSettingListViewHolder = (ISettingListViewHolder) holder;
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
            return list.get(position).getType();
        }
        return super.getItemViewType(position);
    }
}
