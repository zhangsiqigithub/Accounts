package com.dragon.accounts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.dragon.accounts.model.ColorManager;
import com.dragon.accounts.model.accounting.adapter.AccountingListAdapter;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;
import com.dragon.accounts.view.CalculatorView;
import com.dragon.accounts.view.CalendarDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountingActivity extends Activity implements View.OnClickListener {

    public static void start(Activity activity, int reqCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, AccountingActivity.class);
            activity.startActivityForResult(intent, reqCode);
        }
    }

    private static final int REQ_CODE_REMARK = 100;
    private static final int REQ_CODE_ICON = 101;
    private static final int SPAN_COUNT = 6;

    private TextView layout_accounting_text_money;
    private TextView layout_accounting_btn_revenue;
    private TextView layout_accounting_btn_expenses;
    private CalculatorView mCalculatorView;
    private RecyclerView layout_accounting_recyclerview;
    private ImageView layout_accounting_icon_img;
    private TextView layout_accounting_icon_title;
    private TextView layout_accounting_date_year;
    private TextView layout_accounting_date_mount_day;
    private TextView layout_accounting_date_content;
    private View layout_accounting_title_parent;

    private AccountingListAdapter mAccountingListAdapter;
    private int accountType = AccountManager.ACCOUNT_TYPE_EXPENSES;

    private List<AccountIconInfo> list;
    private int mCurrentSelectIndex;
    private CalendarDialog mCalandarDialog;
    private final DateFormat mFormatter_year = new SimpleDateFormat("yyyy");
    private final DateFormat mFormatter_mounth_day = new SimpleDateFormat("MM月dd日");
    private long mTimeLong = System.currentTimeMillis();
    private int mAccountBookBgColor;
    private boolean isInitAnim;

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
                    layout_accounting_title_parent.setBackgroundColor(mAccountBookBgColor);
                    doSelectIcon(mCurrentSelectIndex, (Boolean) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting);
        init();
    }

    private void init() {
        initData(false);
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
                        info.iconId,
                        layout_accounting_date_content.getText().toString(),
                        result,
                        accountType,
                        AccountBookManager.getCurrentAccountBookId(getApplicationContext()), mTimeLong);
                setResult(RESULT_OK);
                finish();
            }
        });

        mCalandarDialog = new CalendarDialog(this);
        mCalandarDialog.setCalendarDialogCallback(new CalendarDialog.CalendarDialogCallback() {
            @Override
            public void onDateSelected(long time) {
                mTimeLong = time;
                resetDate();
                mCalandarDialog.dismiss();
            }
        });
        mCalandarDialog.setSelectedDate(mTimeLong);
        resetDate();
    }

    private void resetDate() {
        Date tempDate = new Date(mTimeLong);
        if (layout_accounting_date_year != null) {
            layout_accounting_date_year.setText(mFormatter_year.format(tempDate));
        }
        if (layout_accounting_date_mount_day != null) {
            layout_accounting_date_mount_day.setText(mFormatter_mounth_day.format(tempDate));
        }
    }

    private AccountIconInfo.AccountingCallback mAccountingCallback = new AccountIconInfo.AccountingCallback() {
        @Override
        public void onClick(int position) {
            doSelectIcon(position, true);
        }
    };

    private void doSelectIcon(int position, boolean anim) {
        final AccountIconInfo info = list.get(position);
        switch (info.iconId) {
            case AccountIconManager.ICON_ID_ADD:
                Bundle bundle = new Bundle();
                bundle.putInt(AddAccountTypeActivity.EXTRA_ACCOUNT_TYPE, accountType);
                AddAccountTypeActivity.start(this, bundle, REQ_CODE_ICON);
                break;
            default:
                if (!isInitAnim || mCurrentSelectIndex != position) {
                    isInitAnim = true;
                    mCurrentSelectIndex = position;
                    if (anim) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_accounting_icon_img, "rotationY", 0, 90f).setDuration(100);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                layout_accounting_icon_img.setImageResource(info.iconId);
                                layout_accounting_icon_title.setText(info.name);
                                ObjectAnimator animator = ObjectAnimator.ofFloat(layout_accounting_icon_img, "rotationY", 270, 360f).setDuration(100);
                                animator.start();
                            }
                        });
                        animator.start();
                    } else {
                        layout_accounting_icon_img.setImageResource(info.iconId);
                        layout_accounting_icon_title.setText(info.name);
                    }
                }
                break;
        }
    }

    private void initData(final boolean anim) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mAccountBookBgColor = ColorManager.getCurrentBookColor(getApplicationContext());
                List<AccountIconInfo> accountIconInfos = AccountIconManager.queryAllAccountIcons(getApplicationContext(), accountType, mAccountingCallback);
                accountIconInfos.add(new AccountIconInfo(-1, AccountIconManager.ICON_ID_ADD, getString(R.string.string_icon_id_add), 0, mAccountingCallback));
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.clear();
                list.addAll(accountIconInfos);
                mCurrentSelectIndex = 0;
                isInitAnim = false;
                Message msg = Message.obtain();
                msg.what = MSG_UPDATE;
                msg.obj = anim;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initView() {
        layout_accounting_title_parent = findViewById(R.id.layout_accounting_title_parent);
        layout_accounting_text_money = (TextView) findViewById(R.id.layout_accounting_text_money);
        layout_accounting_btn_revenue = (TextView) findViewById(R.id.layout_accounting_btn_revenue);
        layout_accounting_btn_expenses = (TextView) findViewById(R.id.layout_accounting_btn_expenses);
        mCalculatorView = (CalculatorView) findViewById(R.id.layout_accounting_calculator_view);
        layout_accounting_recyclerview = (RecyclerView) findViewById(R.id.layout_accounting_recyclerview);
        layout_accounting_icon_img = (ImageView) findViewById(R.id.layout_accounting_icon_img);
        layout_accounting_icon_title = (TextView) findViewById(R.id.layout_accounting_icon_title);
        layout_accounting_date_year = (TextView) findViewById(R.id.layout_accounting_date_year);
        layout_accounting_date_mount_day = (TextView) findViewById(R.id.layout_accounting_date_mount_day);
        layout_accounting_date_content = (TextView) findViewById(R.id.layout_accounting_date_content);

        findViewById(R.id.layout_account_back_btn).setOnClickListener(this);
        findViewById(R.id.layout_accounting_date_parent).setOnClickListener(this);
        findViewById(R.id.layout_accounting_date_content_btn).setOnClickListener(this);
        findViewById(R.id.layout_accounting_date_content).setOnClickListener(this);
        layout_accounting_btn_revenue.setOnClickListener(this);
        layout_accounting_btn_expenses.setOnClickListener(this);

        layout_accounting_recyclerview.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_account_back_btn:
                finish();
                break;
            case R.id.layout_accounting_btn_revenue:
                if (accountType != AccountManager.ACCOUNT_TYPE_REVENUE) {
                    layout_accounting_btn_revenue.setAlpha(1f);
                    layout_accounting_btn_expenses.setAlpha(0.3f);
                    accountType = AccountManager.ACCOUNT_TYPE_REVENUE;
                    initData(true);
                }
                break;
            case R.id.layout_accounting_btn_expenses:
                if (accountType != AccountManager.ACCOUNT_TYPE_EXPENSES) {
                    layout_accounting_btn_revenue.setAlpha(0.3f);
                    layout_accounting_btn_expenses.setAlpha(1f);
                    accountType = AccountManager.ACCOUNT_TYPE_EXPENSES;
                    initData(true);
                }
                break;
            case R.id.layout_accounting_date_parent:
                showCalandarDialog();
                break;
            case R.id.layout_accounting_date_content_btn:
            case R.id.layout_accounting_date_content:
                Bundle bundle = new Bundle();
                bundle.putString(AccountingWriteCoutentActivity.EXTRA_CONTENT, layout_accounting_date_content.getText().toString());
                AccountingWriteCoutentActivity.start(this, bundle, REQ_CODE_REMARK);
                break;
        }
    }

    private void showCalandarDialog() {
        cancelCalandarDialog();
        mCalandarDialog.show();
    }

    private void cancelCalandarDialog() {
        if (mCalandarDialog != null && mCalandarDialog.isShowing()) {
            mCalandarDialog.cancel();
//            mCalandarDialog = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelCalandarDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_REMARK:
                    String stringExtra = data.getStringExtra(AccountingWriteCoutentActivity.EXTRA_CONTENT);
                    if (layout_accounting_date_content != null) {
                        layout_accounting_date_content.setText(stringExtra);
                    }
                    break;
                case REQ_CODE_ICON:
                    initData(false);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
