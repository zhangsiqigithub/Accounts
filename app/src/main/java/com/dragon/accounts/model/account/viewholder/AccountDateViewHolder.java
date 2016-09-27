package com.dragon.accounts.model.account.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.account.info.AccountDateInfo;
import com.dragon.accounts.model.account.info.IAccountInfo;

public class AccountDateViewHolder extends BaseAccountHolder {

    private TextView layout_account_date_date;
    private TextView layout_account_date_money;

    private AccountDateInfo info;

    public AccountDateViewHolder(Context context, View itemView) {
        super(context, itemView);
        layout_account_date_date = (TextView) itemView.findViewById(R.id.layout_account_date_date);
        layout_account_date_money = (TextView) itemView.findViewById(R.id.layout_account_date_money);
    }

    @Override
    public void buildView(IAccountInfo item) {
        if (item == null)
            return;
        info = (AccountDateInfo) item;
        if (info == null)
            return;
        layout_account_date_date.setText(info.date);
        float money = (float) (Math.round(info.money * 100)) / 100;
        layout_account_date_money.setText(String.valueOf(money));

    }
}
