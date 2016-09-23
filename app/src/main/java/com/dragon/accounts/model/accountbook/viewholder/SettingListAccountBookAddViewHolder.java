package com.dragon.accounts.model.accountbook.viewholder;

import android.content.Context;
import android.view.View;

import com.dragon.accounts.R;
import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;

public class SettingListAccountBookAddViewHolder extends BaseSettingListViewHolder {

    private View setting_list_item_parent;

    public SettingListAccountBookAddViewHolder(Context context, View itemView) {
        super(context, itemView);
        setting_list_item_parent = itemView.findViewById(R.id.setting_list_item_parent);
    }

    @Override
    public void buildView(IAccountBookInfo item) {
        super.buildView(item);
        setting_list_item_parent.setOnClickListener(this);
    }
}
