package com.yunke.xiaovo.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by huayaowei on 2018/1/21.
 */

public class BaseFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getLayoutId() != 0) {
            view = inflater.inflate(getLayoutId(), container,false);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        ButterKnife.bind(this, view);
        initView(view);
        initData();

        return view;
    }

    protected void initData() {

    }

    protected void initView(View view) {

    }


    protected int getLayoutId() {
        return 0;
    }


}
