package com.dragon.accounts.model.accounting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountIconManager;
import com.dragon.accounts.model.accounting.viewholder.AccountAddHolder;
import com.dragon.accounts.model.accounting.viewholder.AccountIconHolder;

public class AccountingListFactory {

    public static View getView(Context context, int type) {
        switch (type) {
            case AccountIconManager.ICON_ID_ADD:
                return View.inflate(context, R.layout.layout_account_add, null);
            default:
                return View.inflate(context, R.layout.layout_account_icon, null);
        }
    }

    public static RecyclerView.ViewHolder getHolder(Context context, int type) {
        switch (type) {
            case AccountIconManager.ICON_ID_ADD:
                return new AccountAddHolder(context, getView(context, type));
            default:
                return new AccountIconHolder(context, getView(context, type));
        }
    }

}
