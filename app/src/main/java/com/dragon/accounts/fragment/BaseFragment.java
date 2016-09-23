package com.dragon.accounts.fragment;

import android.support.v4.app.Fragment;

import com.dragon.accounts.util.LogUtil;

public class BaseFragment extends Fragment{

    protected void log(String log){
        LogUtil.d(getClass().getSimpleName() + "-->" + log);
    }
}
