package com.dragon.accounts.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dragon.accounts.R;
import com.dragon.accounts.model.ColorManager;
import com.dragon.accounts.util.CompatUtils;

import java.util.List;

public class AccountAddDialog extends Dialog implements View.OnClickListener {

    public interface Callback {
        void onOkClick(Dialog dialog, String name, int colorPosition);

        void onCancelClick(Dialog dialog);
    }

    private Context mContext;
    private Callback mCallback;
    private View dialog_account_add_btn_ok;
    private View dialog_account_add_btn_cancel;
    private EditText dialog_account_add_btn_edittext;
    private RecyclerView dialog_account_add_recyclerview;

    private MyAdapter mAdapter;
    private int currentSelectPosition;
    private List<Integer> mColorList;

    public AccountAddDialog(Context context) {
        super(context, R.style.dialog);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mColorList = ColorManager.getColorList();
    }

    @Override
    public void create() {
        super.create();
        setContentView(R.layout.dialog_account_add_layout);

        dialog_account_add_btn_ok = findViewById(R.id.dialog_account_add_btn_ok);
        dialog_account_add_btn_cancel = findViewById(R.id.dialog_account_add_btn_cancel);
        dialog_account_add_btn_edittext = (EditText) findViewById(R.id.dialog_account_add_btn_edittext);
        dialog_account_add_recyclerview = (RecyclerView) findViewById(R.id.dialog_account_add_recyclerview);

        dialog_account_add_btn_ok.setOnClickListener(this);
        dialog_account_add_btn_cancel.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dialog_account_add_recyclerview.setLayoutManager(layoutManager);
        dialog_account_add_recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int margin = CompatUtils.dp2px(mContext, 5);
                outRect.set(margin, 0, margin, 0);
            }
        });
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void show() {
        currentSelectPosition = 0;
        if (dialog_account_add_btn_edittext != null) {
            dialog_account_add_btn_edittext.setText(null);
        }
        if (mAdapter == null) {
            mAdapter = new MyAdapter();
            dialog_account_add_recyclerview.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_account_add_btn_ok:
                if (mCallback != null) {
                    mCallback.onOkClick(this, dialog_account_add_btn_edittext.getText().toString(), currentSelectPosition);
                }
                break;
            case R.id.dialog_account_add_btn_cancel:
                if (mCallback != null) {
                    mCallback.onCancelClick(this);
                }
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.layout_color_card, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder == null)
                return;
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            if (myViewHolder == null)
                return;
            myViewHolder.buildView(mColorList.get(position));
        }

        @Override
        public int getItemCount() {
            if (mColorList != null)
                return mColorList.size();
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private View bg;
            private View flag;

            public MyViewHolder(View itemView) {
                super(itemView);
                bg = itemView.findViewById(R.id.color_bg);
                flag = itemView.findViewById(R.id.color_flag);
            }

            public void buildView(Integer color) {
                bg.setBackgroundColor(color);
                flag.setVisibility(currentSelectPosition == getAdapterPosition() ? View.VISIBLE : View.GONE);
                bg.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                currentSelectPosition = getAdapterPosition();
                notifyDataSetChanged();
            }
        }
    }
}
