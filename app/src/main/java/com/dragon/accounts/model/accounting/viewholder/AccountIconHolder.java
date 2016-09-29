package com.dragon.accounts.model.accounting.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;

public class AccountIconHolder extends BaseAccountingHolder implements IAccountingViewHolder {

    private ImageView layout_account_icon_img;
    private TextView layout_account_icon_text;

    public AccountIconHolder(Context context, View itemView) {
        super(context, itemView);
        layout_account_icon_img = (ImageView) itemView.findViewById(R.id.layout_account_icon_img);
        layout_account_icon_text = (TextView) itemView.findViewById(R.id.layout_account_icon_text);
    }

    @Override
    public void buildView(AccountIconInfo item) {
        layout_account_icon_img.setImageResource(item.iconId);
        layout_account_icon_text.setText(item.name);
    }
}
