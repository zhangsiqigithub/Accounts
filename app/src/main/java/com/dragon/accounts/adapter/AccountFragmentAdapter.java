package com.dragon.accounts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dragon.accounts.model.account.AccountListFactory;
import com.dragon.accounts.model.account.info.IAccountInfo;
import com.dragon.accounts.model.account.viewholder.IAccountViewHolder;

import java.util.List;

public class AccountFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<IAccountInfo> mAccountInfoList;

    public AccountFragmentAdapter(Context context, List<IAccountInfo> mAccountInfoList) {
        this.mContext = context;
        this.mAccountInfoList = mAccountInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AccountListFactory.getHolder(mContext, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null)
            return;
        IAccountViewHolder iViewHolder = (IAccountViewHolder) holder;
        if (iViewHolder == null)
            return;
        iViewHolder.buildView(mAccountInfoList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (mAccountInfoList != null) {
            IAccountInfo iAccountInfo = mAccountInfoList.get(position);
            if (iAccountInfo != null)
                return iAccountInfo.getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mAccountInfoList != null)
            return mAccountInfoList.size();
        return 0;
    }
}
