package com.dragon.accounts.setting.model.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountBookInfo;
import com.dragon.accounts.model.IAccountBook;
import com.dragon.accounts.setting.model.ColorManager;

public class SettingListAccountBookViewHolder extends BaseSettingListViewHolder {

    private AccountBookInfo info;

    private View setting_list_item_parent;
    private View setting_list_item_bg;
    private TextView setting_list_item_title;
    private TextView setting_list_item_size;

    public SettingListAccountBookViewHolder(Context context, View itemView) {
        super(context, itemView);
        setting_list_item_parent = itemView.findViewById(R.id.setting_list_item_parent);
        setting_list_item_bg = itemView.findViewById(R.id.setting_list_item_bg);
        setting_list_item_title = (TextView) itemView.findViewById(R.id.setting_list_item_title);
        setting_list_item_size = (TextView) itemView.findViewById(R.id.setting_list_item_size);
    }

    @Override
    public void buildView(IAccountBook item) {
        super.buildView(item);
        if (item == null)
            return;
        info = (AccountBookInfo) item;
        if (info == null)
            return;
        setting_list_item_title.setText(info.title);
        setting_list_item_size.setText(info.size + mContext.getString(R.string.string_account_book_size_unit));
        setting_list_item_bg.setBackgroundColor(ColorManager.getColor(info.colorPosition));
        setting_list_item_parent.setOnClickListener(this);
    }
}
