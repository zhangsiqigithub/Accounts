package com.dragon.accounts.model.accounting.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;

public class AccountAddHolder extends BaseAccountingHolder implements IAccountingViewHolder , View.OnClickListener{

    private View layout_account_icon_parent;
    private TextView layout_account_icon_text;
    private AccountIconInfo item;

    public AccountAddHolder(Context context, View itemView) {
        super(context, itemView);
        layout_account_icon_parent = itemView.findViewById(R.id.layout_account_icon_parent);
        layout_account_icon_text = (TextView) itemView.findViewById(R.id.layout_account_icon_text);
    }

    @Override
    public void buildView(AccountIconInfo item) {
        if(item == null)
            return;
        this.item = item;
        layout_account_icon_text.setText(item.name);
        layout_account_icon_parent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(item != null && item.mAccountingCallback != null){
            item.mAccountingCallback.onClick(getAdapterPosition());
        }
    }
}
