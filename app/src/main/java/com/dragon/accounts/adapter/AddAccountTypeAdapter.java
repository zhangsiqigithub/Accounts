package com.dragon.accounts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dragon.accounts.R;
import com.dragon.accounts.util.CompatUtils;

public class AddAccountTypeAdapter extends RecyclerView.Adapter {

    public interface AddAccountTypeAdapterCallback {
        void onItemClick(int position);
    }

    private Context mContext;
    private int[][] mList;
    private AddAccountTypeAdapterCallback mAddAccountTypeAdapterCallback;

    public AddAccountTypeAdapter(Context context, int[][] mList) {
        this.mContext = context;
        this.mList = mList;
    }

    public void setAddAccountTypeAdapterCallback(AddAccountTypeAdapterCallback addAccountTypeAdapterCallback) {
        this.mAddAccountTypeAdapterCallback = addAccountTypeAdapterCallback;
    }

    public void setDatas(int[][] mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.layout_account_icon_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mList != null && position < mList.length) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.buildView(mList[position][0]);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.length;
        }
        return 0;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.account_icon_item_img);
            view.setOnClickListener(this);
        }

        public void buildView(int resId) {
            imageView.setImageResource(resId);
        }

        @Override
        public void onClick(View v) {
            if (mAddAccountTypeAdapterCallback != null) {
                mAddAccountTypeAdapterCallback.onItemClick(getAdapterPosition());
            }
        }
    }
}
