package com.dragon.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dragon.accounts.model.accounting.adapter.AccountingListAdapter;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;
import com.dragon.accounts.util.LogUtil;
import com.dragon.accounts.view.CalculatorView;

import java.util.List;

public class AccountingActivity extends Activity implements View.OnClickListener {

    public static void start(Activity activity, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, AccountingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
        }
    }


    private static final int SPAN_COUNT = 6;

    private TextView layout_accounting_text_money;
    private CalculatorView mCalculatorView;
    private RecyclerView layout_accounting_recyclerview;
    private AccountingListAdapter mAccountingListAdapter;

    private List<AccountIconInfo> list;

    private static final int MSG_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE:
                    if (mAccountingListAdapter == null) {
                        mAccountingListAdapter = new AccountingListAdapter(getApplicationContext(), list);
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
            public void onUpdate(float result) {
                layout_accounting_text_money.setText(String.valueOf(result));
            }

            @Override
            public void onFinish(float result) {
                LogUtil.d("onFinish:" + result);
            }
        });
    }

    private void initData() {

    }

    private void initView() {
        layout_accounting_text_money = (TextView) findViewById(R.id.layout_accounting_text_money);
        mCalculatorView = (CalculatorView) findViewById(R.id.layout_accounting_calculator_view);
        layout_accounting_recyclerview = (RecyclerView) findViewById(R.id.layout_accounting_recyclerview);

        findViewById(R.id.layout_account_back_btn).setOnClickListener(this);

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
        }

    }
}
