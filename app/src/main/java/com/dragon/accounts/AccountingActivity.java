package com.dragon.accounts;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.accounts.model.AccountBookManager;
import com.dragon.accounts.model.AccountIconManager;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.accounting.adapter.AccountingListAdapter;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;
import com.dragon.accounts.view.CalculatorView;

import java.util.ArrayList;
import java.util.List;

public class AccountingActivity extends Activity implements View.OnClickListener {

    public static void start(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, AccountingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }


    private static final int SPAN_COUNT = 6;

    private TextView layout_accounting_text_money;
    private TextView layout_accounting_btn_revenue;
    private TextView layout_accounting_btn_expenses;
    private CalculatorView mCalculatorView;
    private RecyclerView layout_accounting_recyclerview;
    private ImageView layout_accounting_icon_img;
    private TextView layout_accounting_icon_title;

    private AccountingListAdapter mAccountingListAdapter;
    private int accountType = AccountManager.ACCOUNT_TYPE_EXPENSES;

    private List<AccountIconInfo> list;
    private int mCurrentSelectIndex;

    private static final int MSG_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE:
                    if (mAccountingListAdapter == null) {
                        mAccountingListAdapter = new AccountingListAdapter(list);
                        layout_accounting_recyclerview.setAdapter(mAccountingListAdapter);
                    } else {
                        mAccountingListAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_accounting);
        init();
    }

    private void init() {
        initData();
        initView();
        mCalculatorView.setCalculatorViewCallback(new CalculatorView.CalculatorViewCallback() {
            @Override
            public void onUpdate(String result) {
                layout_accounting_text_money.setText(result);
            }

            @Override
            public void onFinish(double result) {
                AccountIconInfo info = list.get(mCurrentSelectIndex);
                AccountManager.insertAccount(getApplicationContext(),
                        info.name,
                        "",
                        result,
                        accountType,
                        AccountBookManager.getCurrentAccountBookId(getApplicationContext()));
                finish();
            }
        });
    }

    private AccountIconInfo.AccountingCallback mAccountingCallback = new AccountIconInfo.AccountingCallback() {
        @Override
        public void onClick(int position) {
            AccountIconInfo info = list.get(position);
            switch (info.iconId) {
                case AccountIconManager.ICON_ID_ADD:

                    break;
                default:
                    mCurrentSelectIndex = position;
                    layout_accounting_icon_img.setImageResource(AccountIconManager.getTitleIconIdByIconId(info.iconId));
                    layout_accounting_icon_title.setText(info.name);

                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout_accounting_icon_img, "scaleX", 1.0f, 1.2f, 0.5f, 1.0f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout_accounting_icon_img, "scaleY", 1.0f, 1.2f, 0.5f, 1.0f);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(scaleX, scaleY);
                    animatorSet.setDuration(500);
                    animatorSet.start();
                    break;
            }
        }
    };

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<AccountIconInfo> accountIconInfos = AccountIconManager.queryAllAccountIcons(getApplicationContext(), mAccountingCallback);
                accountIconInfos.add(new AccountIconInfo(-1, AccountIconManager.ICON_ID_ADD, getString(R.string.string_icon_id_add), mAccountingCallback));
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.clear();
                list.addAll(accountIconInfos);
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        }.start();
    }

    private void initView() {
        layout_accounting_text_money = (TextView) findViewById(R.id.layout_accounting_text_money);
        layout_accounting_btn_revenue = (TextView) findViewById(R.id.layout_accounting_btn_revenue);
        layout_accounting_btn_expenses = (TextView) findViewById(R.id.layout_accounting_btn_expenses);
        mCalculatorView = (CalculatorView) findViewById(R.id.layout_accounting_calculator_view);
        layout_accounting_recyclerview = (RecyclerView) findViewById(R.id.layout_accounting_recyclerview);
        layout_accounting_icon_img = (ImageView) findViewById(R.id.layout_accounting_icon_img);
        layout_accounting_icon_title = (TextView) findViewById(R.id.layout_accounting_icon_title);

        findViewById(R.id.layout_account_back_btn).setOnClickListener(this);
        layout_accounting_btn_revenue.setOnClickListener(this);
        layout_accounting_btn_expenses.setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        layout_accounting_recyclerview.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_account_back_btn:
                finish();
                break;
            case R.id.layout_accounting_btn_revenue:
                layout_accounting_btn_revenue.setAlpha(1f);
                layout_accounting_btn_expenses.setAlpha(0.3f);
                accountType = AccountManager.ACCOUNT_TYPE_REVENUE;
                break;
            case R.id.layout_accounting_btn_expenses:
                layout_accounting_btn_revenue.setAlpha(0.3f);
                layout_accounting_btn_expenses.setAlpha(1f);
                accountType = AccountManager.ACCOUNT_TYPE_EXPENSES;
                break;
        }

    }
}
