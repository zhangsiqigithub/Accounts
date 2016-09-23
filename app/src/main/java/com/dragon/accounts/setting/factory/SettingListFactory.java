package com.dragon.accounts.setting.factory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragon.accounts.R;
import com.dragon.accounts.model.IAccountBook;
import com.dragon.accounts.setting.model.viewholder.SettingListAccountBookAddViewHolder;
import com.dragon.accounts.setting.model.viewholder.SettingListAccountBookViewHolder;

public class SettingListFactory {

    public static View getView(Context context, int type) {
        switch (type) {
            case IAccountBook.TYPE_ACCOUNT_BOOK:
                return View.inflate(context, R.layout.layout_setting_list_item_account_book, null);
            case IAccountBook.TYPE_ACCOUNT_ADD:
                return View.inflate(context, R.layout.layout_setting_list_item_account_book_add, null);
        }
        return null;
    }

    public static RecyclerView.ViewHolder getHolder(Context context, int type) {
        switch (type) {
            case IAccountBook.TYPE_ACCOUNT_BOOK:
                return new SettingListAccountBookViewHolder(context, getView(context, type));
            case IAccountBook.TYPE_ACCOUNT_ADD:
                return new SettingListAccountBookAddViewHolder(context, getView(context, type));
        }
        return null;
    }

}
