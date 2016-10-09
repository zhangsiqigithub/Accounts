package com.dragon.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.dragon.accounts.adapter.MainViewPagerListAdapter;
import com.dragon.accounts.fragment.AccountFragment;
import com.dragon.accounts.fragment.DetailFragment;
import com.dragon.accounts.model.SettingModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager home_viewpager;

    private SettingModel mSettingModel;

    private AccountFragment accountFragment;
    private DetailFragment detailFragment;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        accountFragment = new AccountFragment(this);
        fragmentList.add(accountFragment);
        detailFragment = new DetailFragment(this);
        fragmentList.add(detailFragment);

        accountFragment.setAccountFragmentCallback(new AccountFragment.AccountFragmentCallback() {
            @Override
            public void onAccountAdded() {
                closeDrawer();
                mSettingModel.resetData();
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        home_viewpager = (ViewPager) findViewById(R.id.home_viewpager);

        mSettingModel = new SettingModel(this, findViewById(R.id.left_drawer));
        mSettingModel.setSettingModelCallback(new SettingModel.SettingModelCallback() {
            @Override
            public void onAccountSelect() {
                accountFragment.resetData();
            }
        });

        home_viewpager.setAdapter(new MainViewPagerListAdapter(getSupportFragmentManager(), fragmentList));
        home_viewpager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSettingModel != null) {
            mSettingModel.hideAccountAddDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSettingModel != null) {
            mSettingModel.release();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        accountFragment.onActivityResult(requestCode, resultCode, data);
    }
}
