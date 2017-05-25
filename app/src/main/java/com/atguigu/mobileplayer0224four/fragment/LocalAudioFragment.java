package com.atguigu.mobileplayer0224four.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mobileplayer0224four.base.BaseFragment;

/**
 * 作者：田学伟 on 2017/5/25 10:12
 * QQ：93226539
 * 作用：
 */

public class LocalAudioFragment extends BaseFragment {
    private TextView textView;

    @Override
    protected View initView() {
        textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("本地音乐");
    }
}
