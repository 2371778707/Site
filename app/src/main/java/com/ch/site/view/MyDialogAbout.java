package com.ch.site.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ch.site.R;

public class MyDialogAbout extends Dialog{
    private TextView myurl;


    public MyDialogAbout(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        //空白处不能取消动画
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
    }


    private void initData() {

    }

    private void initView() {
        myurl = findViewById(R.id.myurl);
    }
}
