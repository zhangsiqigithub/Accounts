package com.dragon.accounts.model.accounting.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dragon.accounts.model.accounting.AccountingListFactory;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;
import com.dragon.accounts.model.accounting.viewholder.IAccountingViewHolder;

import java.util.List;

public class AccountingListAdapter extends RecyclerView.Adapter {

    private List<AccountIconInfo> list;

    public AccountingListAdapter(List<AccountIconInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AccountingListFactory.getHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null)
            return;
        IAccountingViewHolder iAccountingViewHolder = (IAccountingViewHolder) holder;
        if (iAccountingViewHolder == null)
            return;
        if (list == null && position < 0 && position >= list.size())
            return;
        iAccountingViewHolder.buildView(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null && position >= 0 && position < list.size()) {
            return list.get(position).iconId;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
