package com.dragon.accounts.model.accounting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dragon.accounts.model.accounting.info.AccountIconInfo;

import java.util.List;

public class AccountingListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AccountIconInfo> list;

    public AccountingListAdapter(Context context, List<AccountIconInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
