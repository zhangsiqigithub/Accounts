package com.dragon.accounts.model.accounting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragon.accounts.R;
import com.dragon.accounts.model.account.info.IAccountInfo;
import com.dragon.accounts.model.account.viewholder.AccountDateViewHolder;
import com.dragon.accounts.model.account.viewholder.AccountViewHolder;

public class AccountingListFactory {

    public static View getView(Context context, int type) {
        switch (type) {
            case IAccountInfo.TYPE_ACCOUNT:
                return View.inflate(context, R.layout.layout_account, null);
            case IAccountInfo.TYPE_DATE:
                return View.inflate(context, R.layout.layout_account_date, null);
        }
        return null;
    }

    public static RecyclerView.ViewHolder getHolder(Context context, int type) {
        switch (type) {
            case IAccountInfo.TYPE_ACCOUNT:
                return new AccountViewHolder(context, getView(context, type));
            case IAccountInfo.TYPE_DATE:
                return new AccountDateViewHolder(context, getView(context, type));
        }
        return null;
    }

}
