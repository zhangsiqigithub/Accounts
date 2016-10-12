package com.dragon.accounts.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dragon.accounts.fragment.BaseFragment;

import java.util.List;

public class MainViewPagerListAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragmentList;

    public MainViewPagerListAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList != null) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragmentList != null) {
            return fragmentList.size();
        }
        return 0;
    }

}
