package com.dragon.accounts.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.accounts.R;
import com.dragon.accounts.adapter.AccountBookListAdapter;
import com.dragon.accounts.model.accountbook.info.AccountBookInfo;
import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;
import com.dragon.accounts.util.AccountUtil;
import com.dragon.accounts.util.CompatUtils;
import com.dragon.accounts.view.AccountAddDialog;

import java.util.ArrayList;
import java.util.List;

public class SettingModel implements IAccountBookInfo.Callback {

    public interface SettingModelCallback {
        void onAccountSelect();
    }

    private static final int LIST_COLUMN = 3;

    private Context mContext;
    private Activity mActivity;

    private TextView setting_total_revenue_size;
    private TextView setting_total_expenses_size;
    private TextView setting_total_balance_size;
    private RecyclerView setting_recyclerview;
    private AccountBookListAdapter mSettingListAdapater;
    private SettingModelCallback mSettingModelCallback;
    private AccountAddDialog mAccountAddDialog;

    private List<IAccountBookInfo> list = new ArrayList<>();

    private static final int MSG_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
                    if (mSettingListAdapater == null) {
                        mSettingListAdapater = new AccountBookListAdapter(mContext, list);
                        setting_recyclerview.setAdapter(mSettingListAdapater);
                    } else {
                        mSettingListAdapater.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public SettingModel(Activity activity, View view) {
        this.mActivity = activity;
        this.mContext = mActivity.getApplicationContext();
        init(view);
    }

    private void init(View view) {
        initView(view);
        initRecyclerView();
        queryAccounts();

        resetData();
    }

    public void resetData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final double totalRevenue = AccountManager.getTotalRevenue(mContext);
                final double totalExpenses = AccountManager.getTotalExpenses(mContext);
                final String totalRevenueStr = AccountUtil.getAccountMoney(totalRevenue);
                final String totalExpensesStr = AccountUtil.getAccountMoney(totalExpenses);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setting_total_revenue_size.setText(totalRevenueStr);
                        setting_total_expenses_size.setText(totalExpensesStr);
                        setting_total_balance_size.setText(AccountUtil.getAccountMoney(totalRevenue - totalExpenses));
                    }
                });
            }
        }.start();
    }

    private void initView(View view) {
        setting_recyclerview = (RecyclerView) view.findViewById(R.id.setting_recyclerview);
        setting_total_revenue_size = (TextView) view.findViewById(R.id.setting_total_revenue_size);
        setting_total_expenses_size = (TextView) view.findViewById(R.id.setting_total_expenses_size);
        setting_total_balance_size = (TextView) view.findViewById(R.id.setting_total_balance_size);
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
                List<IAccountBookInfo> bookList = AccountBookManager.queryAccountBook(mContext, SettingModel.this);
                list.clear();
                list.addAll(bookList);
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        }.start();
    }

    public void setSettingModelCallback(SettingModelCallback settingModelCallback) {
        this.mSettingModelCallback = settingModelCallback;
    }

    @Override
    public void onListItemClick(int type, int position) {
        switch (type) {
            case IAccountBookInfo.TYPE_ACCOUNT_BOOK:
                AccountBookInfo accountBookInfo = (AccountBookInfo) list.get(position);
                AccountBookManager.setCurrentAccountBookId(mContext, accountBookInfo.accountBookId);
                if (mSettingModelCallback != null) {
                    mSettingModelCallback.onAccountSelect();
                }
                break;
            case IAccountBookInfo.TYPE_ACCOUNT_ADD:
                showAccountAddDialog();
                break;
        }
    }

    private void showAccountAddDialog() {
        hideAccountAddDialog();
        if (mAccountAddDialog == null) {
            mAccountAddDialog = new AccountAddDialog(mActivity);
            mAccountAddDialog.setCallback(new AccountAddDialog.Callback() {
                @Override
                public void onOkClick(Dialog dialog, String name, int colorPosition) {
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.string_account_add_error_tips), Toast.LENGTH_SHORT).show();
                    } else {
                        AccountBookManager.insertAccountBook(mContext, name.trim(), 0, colorPosition);
                        queryAccounts();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelClick(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            mAccountAddDialog.create();
        }
        mAccountAddDialog.show();
    }

    public void hideAccountAddDialog() {
        if (mAccountAddDialog != null && mAccountAddDialog.isShowing()) {
            mAccountAddDialog.dismiss();
        }
    }
}
