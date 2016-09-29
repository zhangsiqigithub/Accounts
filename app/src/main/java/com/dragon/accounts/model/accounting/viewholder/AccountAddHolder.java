package com.dragon.accounts.model.accounting.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;

public class AccountAddHolder extends BaseAccountingHolder implements IAccountingViewHolder {
    private TextView layout_account_icon_text;

    public AccountAddHolder(Context context, View itemView) {
        super(context, itemView);
        layout_account_icon_text = (TextView) itemView.findViewById(R.id.layout_account_icon_text);
    }

    @Override
    public void buildView(AccountIconInfo item) {
        layout_account_icon_text.setText(item.name);
    }
}
