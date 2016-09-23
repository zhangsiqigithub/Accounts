package com.dragon.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.dragon.accounts.home.adapter.HomeListAdapter;
import com.dragon.accounts.fragment.DetailFragment;
import com.dragon.accounts.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager home_viewpager;

    private SettingModel mSettingModel;

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
        fragmentList.add(new HomeFragment(this));
        fragmentList.add(new DetailFragment(this));
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        home_viewpager = (ViewPager) findViewById(R.id.home_viewpager);

        mSettingModel = new SettingModel(this, findViewById(R.id.left_drawer));

        home_viewpager.setAdapter(new HomeListAdapter(getSupportFragmentManager(), fragmentList));
        home_viewpager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}
