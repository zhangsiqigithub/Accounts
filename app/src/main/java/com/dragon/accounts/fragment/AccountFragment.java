package com.dragon.accounts.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.accounts.AccountingActivity;
import com.dragon.accounts.MainActivity;
import com.dragon.accounts.R;
import com.dragon.accounts.adapter.AccountFragmentAdapter;
import com.dragon.accounts.model.AccountBookManager;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.account.info.AccountDateInfo;
import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.model.account.info.IAccountInfo;
import com.dragon.accounts.provider.IProivderMetaData;
import com.dragon.accounts.util.AccountUtil;
import com.dragon.accounts.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends BaseFragment implements View.OnClickListener {

    public interface AccountFragmentCallback {
        void onAccountAdded();
    }

    private static final String KEY_ACCOUNT_REVENUE = "key_account_revenue";
    private static final String KEY_ACCOUNT_EXPENSES = "key_account_expenses";
    private static final String KEY_ACCOUNT_NAME = "key_account_name";

    private Context mContext;
    private List<IAccountInfo> mAccountInfoList = new ArrayList<>();
    private RecyclerView home_recyclerview;
    private AccountFragmentAdapter mAdapter;
    private int mCurrentAccountBookId;

    private AccountFragmentCallback mAccountFragmentCallback;

    private View home_hint;
    private TextView setting_total_revenue_size;
    private TextView setting_total_balance_size;
    private TextView fragment_account_title;

    private static final int MSG_UPDATE = 1;
    private static final int MSG_UPDATE_ACCOUNT_MONEY = 2;
    private static final int MSG_NOTIFY_ACCOUNT_ADDED = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
                    home_hint.setVisibility(mAccountInfoList.size() > 0 ? View.GONE : View.VISIBLE);
                    if (mAdapter == null) {
                        mAdapter = new AccountFragmentAdapter(mContext, mAccountInfoList);
                        home_recyclerview.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case MSG_UPDATE_ACCOUNT_MONEY:
                    Bundle bundle = msg.getData();
                    setting_total_revenue_size.setText(AccountUtil.getAccountMoney(bundle.getDouble(KEY_ACCOUNT_REVENUE)));
                    setting_total_balance_size.setText(AccountUtil.getAccountMoney(bundle.getDouble(KEY_ACCOUNT_EXPENSES)));
                    fragment_account_title.setText(String.valueOf(bundle.getString(KEY_ACCOUNT_NAME)));
                    break;
                case MSG_NOTIFY_ACCOUNT_ADDED:
                    if (mAccountFragmentCallback != null) {
                        mAccountFragmentCallback.onAccountAdded();
                    }
                    break;
            }
        }
    };

    public AccountFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, null);
        home_hint = view.findViewById(R.id.fragment_account_hint);
        home_recyclerview = (RecyclerView) view.findViewById(R.id.fragment_account_recyclerview);
        setting_total_revenue_size = (TextView) view.findViewById(R.id.fragment_account_total_revenue_size);
        setting_total_balance_size = (TextView) view.findViewById(R.id.fragment_account_total_expenses_size);
        fragment_account_title = (TextView) view.findViewById(R.id.fragment_account_title);
        home_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        view.findViewById(R.id.fragment_account_add).setOnClickListener(this);
        view.findViewById(R.id.fragment_account_menu_bg).setOnClickListener(this);
        home_hint.setOnClickListener(this);

        resetData();
        return view;
    }

    public void setAccountFragmentCallback(AccountFragmentCallback accountFragmentCallback) {
        this.mAccountFragmentCallback = accountFragmentCallback;
    }

    @Override
    public void resetData() {
        mCurrentAccountBookId = AccountBookManager.getCurrentAccountBookId(mContext);
        new Thread() {
            @Override
            public void run() {
                super.run();

                List<IAccountInfo> resultList = new ArrayList<>();
                List<AccountDateInfo> dateList = new ArrayList<>();
                List<AccountInfo> accountList = AccountManager.queryAllAccounts(mContext, mCurrentAccountBookId);

                double totalRevenue = 0;
                double totaExpenses = 0;
                String date = null;
                AccountDateInfo dateInfo = null;
                for (AccountInfo info : accountList) {
                    String millis = TimeUtil.getDayByMillis(info.date);
                    boolean isSameDate = millis != null && millis.equals(date);
                    if (!isSameDate) {
                        dateInfo = new AccountDateInfo();
                        dateInfo.date = millis;
                    }
                    if (info.accountType == AccountManager.ACCOUNT_TYPE_EXPENSES) {
                        dateInfo.money += info.money;
                    }
                    date = millis;
                    switch (info.accountType) {
                        case AccountManager.ACCOUNT_TYPE_REVENUE:
                            totalRevenue += info.money;
                            break;
                        case AccountManager.ACCOUNT_TYPE_EXPENSES:
                            totaExpenses += info.money;
                            break;
                    }
                    if (!isSameDate) {
                        dateList.add(dateInfo);
                        resultList.add(dateInfo);
                    }
                    resultList.add(info);
                }
                mAccountInfoList.clear();
                mAccountInfoList.addAll(resultList);
                mHandler.sendEmptyMessage(MSG_UPDATE);

                Cursor query = mContext.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK,
                        null,
                        IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + "=?",
                        new String[]{String.valueOf(mCurrentAccountBookId)},
                        null);
                String name = null;
                if (query != null && query.moveToNext()) {
                    name = query.getString(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_NAME));
                }

                Message msg = Message.obtain();
                msg.what = MSG_UPDATE_ACCOUNT_MONEY;
                Bundle bundle = new Bundle();
                bundle.putDouble(KEY_ACCOUNT_REVENUE, totalRevenue);
                bundle.putDouble(KEY_ACCOUNT_EXPENSES, totaExpenses);
                bundle.putString(KEY_ACCOUNT_NAME, name);
                msg.setData(bundle);
                mHandler.sendMessage(msg);

                mHandler.sendEmptyMessageDelayed(MSG_NOTIFY_ACCOUNT_ADDED, 100);
            }
        }.start();
    }

    private static final int REQ_CODE = 100;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_account_add:
                AccountingActivity.start(getActivity(), REQ_CODE);
                break;
            case R.id.fragment_account_hint:

                break;
            case R.id.fragment_account_menu_bg:
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            resetData();
        }
    }
}
