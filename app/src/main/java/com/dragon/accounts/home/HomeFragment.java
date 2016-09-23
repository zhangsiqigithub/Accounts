package com.dragon.accounts.home;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.accounts.HomeActivity;
import com.dragon.accounts.R;
import com.dragon.accounts.fragment.BaseFragment;
import com.dragon.accounts.provider.IProivderMetaData;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private Context context;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        view.findViewById(R.id.home_add).setOnClickListener(this);
        view.findViewById(R.id.home_hint).setOnClickListener(this);
        view.findViewById(R.id.home_menu_bg).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_add:

                break;
            case R.id.home_hint:

                break;
            case R.id.home_menu_bg:
                ((HomeActivity) getActivity()).openDrawer();
                break;
        }
    }

    private void updateItemInfo(String title, String content, float money, long date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("money", money);
        contentValues.put("date", date);
        getActivity().getContentResolver().insert(IProivderMetaData.AccountColumns.URI_ACCOUNT, contentValues);

    }

}
