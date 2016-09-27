package com.dragon.accounts.model.account.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.model.account.info.IAccountInfo;

public class AccountViewHolder extends BaseAccountHolder {

    private View fragment_account_layout_left;
    private View fragment_account_layout_right;
    private ImageView fragment_account_img;
    private TextView fragment_account_layout_left_title;
    private TextView fragment_account_layout_left_content;
    private TextView fragment_account_layout_left_money;
    private TextView fragment_account_layout_right_title;
    private TextView fragment_account_layout_right_countent;
    private TextView fragment_account_layout_right_money;

    private AccountInfo info;

    public AccountViewHolder(Context context, View itemView) {
        super(context, itemView);
        fragment_account_layout_left = itemView.findViewById(R.id.fragment_account_layout_left);
        fragment_account_layout_right = itemView.findViewById(R.id.fragment_account_layout_right);
        fragment_account_img = (ImageView) itemView.findViewById(R.id.fragment_account_img);
        fragment_account_layout_left_title = (TextView) itemView.findViewById(R.id.fragment_account_layout_left_title);
        fragment_account_layout_left_content = (TextView) itemView.findViewById(R.id.fragment_account_layout_left_content);
        fragment_account_layout_left_money = (TextView) itemView.findViewById(R.id.fragment_account_layout_left_money);
        fragment_account_layout_right_title = (TextView) itemView.findViewById(R.id.fragment_account_layout_right_title);
        fragment_account_layout_right_countent = (TextView) itemView.findViewById(R.id.fragment_account_layout_right_content);
        fragment_account_layout_right_money = (TextView) itemView.findViewById(R.id.fragment_account_layout_right_money);
    }

    @Override
    public void buildView(IAccountInfo item) {
        info = (AccountInfo) item;
        float money = (float) (Math.round(info.money * 100)) / 100;
        switch (info.accountType) {
            case AccountManager.ACCOUNT_TYPE_REVENUE:
                fragment_account_layout_left.setVisibility(View.VISIBLE);
                fragment_account_layout_right.setVisibility(View.GONE);
                fragment_account_layout_left_title.setText(info.title);
                fragment_account_layout_left_content.setText(info.content);
                fragment_account_layout_left_money.setText(String.valueOf(money));
                break;
            case AccountManager.ACCOUNT_TYPE_EXPENSES:
                fragment_account_layout_left.setVisibility(View.GONE);
                fragment_account_layout_right.setVisibility(View.VISIBLE);
                fragment_account_layout_right_title.setText(info.title);
                fragment_account_layout_right_countent.setText(info.content);
                fragment_account_layout_right_money.setText(String.valueOf(money));
                break;
        }
    }
}
