package com.dragon.accounts.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.accounts.MainActivity;
import com.dragon.accounts.R;
import com.dragon.accounts.adapter.AccountFragmentAdapter;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.model.account.info.IAccountInfo;
import com.dragon.accounts.provider.AccountContentProvider;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private List<IAccountInfo> mAccountInfoList = new ArrayList<>();
    private RecyclerView home_recyclerview;
    private AccountFragmentAdapter mAdapter;
    private int mCurrentAccountBookId;

    private View home_hint;

    private static final int MSG_UPDATE = 1;
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
            }
        }
    };

    public AccountFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        home_hint = view.findViewById(R.id.fragment_account_hint);
        home_recyclerview = (RecyclerView) view.findViewById(R.id.fragment_account_recyclerview);
        home_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        view.findViewById(R.id.fragment_account_add).setOnClickListener(this);
        view.findViewById(R.id.fragment_account_menu_bg).setOnClickListener(this);
        home_hint.setOnClickListener(this);

        resetData();

        return view;
    }

    public void resetData() {
        mCurrentAccountBookId = AccountManager.getCurrentAccountBookId(mContext);
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<AccountInfo> infoList = AccountContentProvider.queryAccounts(mContext, mCurrentAccountBookId);
                mAccountInfoList.clear();
                mAccountInfoList.addAll(infoList);
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_account_add:
                AccountContentProvider.insertAccount(mContext, "用餐", "早饭", 100, AccountManager.ACCOUNT_TYPE_EXPENSES, mCurrentAccountBookId);
                resetData();
                break;
            case R.id.fragment_account_hint:

                break;
            case R.id.fragment_account_menu_bg:
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }

}
