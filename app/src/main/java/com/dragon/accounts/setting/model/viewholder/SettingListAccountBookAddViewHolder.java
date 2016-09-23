package com.dragon.accounts.setting.model.viewholder;

import android.content.Context;
import android.view.View;

import com.dragon.accounts.R;
import com.dragon.accounts.model.IAccountBook;

public class SettingListAccountBookAddViewHolder extends BaseSettingListViewHolder {

    private View setting_list_item_parent;

    public SettingListAccountBookAddViewHolder(Context context, View itemView) {
        super(context, itemView);
        setting_list_item_parent = itemView.findViewById(R.id.setting_list_item_parent);
    }

    @Override
    public void buildView(IAccountBook item) {
        super.buildView(item);
        setting_list_item_parent.setOnClickListener(this);
    }
}
