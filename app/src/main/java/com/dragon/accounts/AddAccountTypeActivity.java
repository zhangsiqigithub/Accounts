package com.dragon.accounts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dragon.accounts.adapter.AddAccountTypeAdapter;
import com.dragon.accounts.model.AccountIconManager;
import com.dragon.accounts.model.AccountManager;

public class AddAccountTypeActivity extends Activity implements View.OnClickListener {

    public static void start(Activity activity, Bundle bundle, int reqCode) {
        if (activity == null)
            return;
        Intent intent = new Intent(activity, AddAccountTypeActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, reqCode);
    }

    public static final String EXTRA_ACCOUNT_TYPE = "extra_account_type";
    private static final int SPAN_COUNT = 6;

    private ImageView add_account_type_img;
    private RecyclerView add_account_type_recyclerview;
    private AddAccountTypeAdapter mAdapter;
    private EditText add_account_type_edittext;

    private int accountType = AccountManager.ACCOUNT_TYPE_EXPENSES;
    private int[][] mList;
    private int mCurrentSelectPosition;

    private static final int MSG_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
                    if (mAdapter == null) {
                        mAdapter = new AddAccountTypeAdapter(AddAccountTypeActivity.this, mList);
                        mAdapter.setAddAccountTypeAdapterCallback(new AddAccountTypeAdapter.AddAccountTypeAdapterCallback() {
                            @Override
                            public void onItemClick(int position) {
                                if (mCurrentSelectPosition != position) {
                                    mCurrentSelectPosition = position;
                                    ObjectAnimator animator = ObjectAnimator.ofFloat(add_account_type_img, "rotationY", 0, 90f).setDuration(100);
                                    animator.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            add_account_type_img.setImageResource(mList[mCurrentSelectPosition][0]);
                                            ObjectAnimator animator = ObjectAnimator.ofFloat(add_account_type_img, "rotationY", 270, 360f).setDuration(100);
                                            animator.start();
                                        }
                                    });
                                    animator.start();
                                }
                            }
                        });
                        add_account_type_recyclerview.setAdapter(mAdapter);
                        if (mList.length > 0) {
                            add_account_type_img.setImageResource(mList[0][0]);
                        }
                    } else {
                        mAdapter.setDatas(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_type);
        initView();
        initData();
    }

    private void initView() {
        add_account_type_img = (ImageView) findViewById(R.id.add_account_type_img);
        add_account_type_edittext = (EditText) findViewById(R.id.add_account_type_edittext);
        add_account_type_recyclerview = (RecyclerView) findViewById(R.id.add_account_type_recyclerview);
        add_account_type_recyclerview.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));

        findViewById(R.id.add_account_type_back_btn).setOnClickListener(this);
        findViewById(R.id.add_account_type_finish).setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                accountType = bundle.getInt(EXTRA_ACCOUNT_TYPE);
            }
        }
        switch (accountType) {
            case AccountManager.ACCOUNT_TYPE_REVENUE:
                mList = AccountIconManager.ICON_ARRAY_REVENUE;
                break;
            case AccountManager.ACCOUNT_TYPE_EXPENSES:
                mList = AccountIconManager.ICON_ARRAY_EXPENSES;
                break;
        }
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_account_type_back_btn:
                finish();
                break;
            case R.id.add_account_type_finish:
                String text = add_account_type_edittext.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    AccountIconManager.insertAccountIcon(this, mList[mCurrentSelectPosition][0], text, accountType);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.string_add_account_type_tips), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
