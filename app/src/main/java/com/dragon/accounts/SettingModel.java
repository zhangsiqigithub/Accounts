package com.dragon.accounts;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dragon.accounts.model.AccountAddBookInfo;
import com.dragon.accounts.model.AccountBookInfo;
import com.dragon.accounts.model.IAccountBook;
import com.dragon.accounts.provider.AccountContentProvider;
import com.dragon.accounts.provider.IProivderMetaData;
import com.dragon.accounts.setting.adapter.SettingListAdapter;
import com.dragon.accounts.setting.view.AccountAddDialog;
import com.dragon.accounts.util.CompatUtils;
import com.dragon.accounts.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingModel implements IAccountBook.Callback {

    private static final int LIST_COLUMN = 3;

    private Context mContext;

    private RecyclerView setting_recyclerview;
    private SettingListAdapter mSettingListAdapater;

    private List<IAccountBook> list = new ArrayList<>();

    private static final int MSG_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
                    if (mSettingListAdapater == null) {
                        mSettingListAdapater = new SettingListAdapter(mContext, list);
                        setting_recyclerview.setAdapter(mSettingListAdapater);
                    } else {
                        mSettingListAdapater.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public SettingModel(Context context, View view) {
        this.mContext = context;
        init(view);
    }

    private void init(View view) {
        initView(view);
        initRecyclerView();
        queryAccounts();
    }

    private void initView(View view) {
        setting_recyclerview = (RecyclerView) view.findViewById(R.id.setting_recyclerview);
    }

    private void initRecyclerView() {
        setting_recyclerview.setLayoutManager(new GridLayoutManager(mContext, LIST_COLUMN));

        setting_recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int margin = CompatUtils.dp2px(mContext, 5);
                outRect.set(margin, margin, margin, margin);
            }
        });
    }

    private void queryAccounts() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<IAccountBook> bookList = AccountContentProvider.queryAccounts(mContext, SettingModel.this);
                list.clear();
                list.addAll(bookList);
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        }.start();
    }

    private AccountAddDialog mAccountAddDialog;

    @Override
    public void onListItemClick(int type, int position) {
        switch (type) {
            case IAccountBook.TYPE_ACCOUNT_BOOK:

                break;
            case IAccountBook.TYPE_ACCOUNT_ADD:
                showAccountAddDialog();
                break;
        }
    }

    private void showAccountAddDialog() {
        hideAccountAddDialog();
        if (mAccountAddDialog == null) {
            AccountAddDialog.Builder builder = new AccountAddDialog.Builder(mContext);
            builder.setCallback(new AccountAddDialog.Callback() {
                @Override
                public void onOkClick(Dialog dialog, String name, int colorPosition) {
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.string_account_add_error_tips), Toast.LENGTH_SHORT).show();
                    } else {
                        AccountContentProvider.insertAccounts(mContext, name, 0, colorPosition);
                        queryAccounts();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelClick(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            mAccountAddDialog = builder.create();
        }
        mAccountAddDialog.show();
    }

    private void hideAccountAddDialog() {
        if (mAccountAddDialog != null && mAccountAddDialog.isShowing()) {
            mAccountAddDialog.hide();
            mAccountAddDialog = null;
        }
    }

    protected void log(String log) {
        LogUtil.d(getClass().getSimpleName() + "-->" + log);
    }
}
