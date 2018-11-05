package com.ch.site.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ch.site.R;

public class MyDialog extends Dialog{
    private Button ok;
    private Button cancel;
    private EditText title;
    private EditText value;
    private  String okstr,cancelstr;
    private  onCancelOnclickListener cancelOnclickListener;
    private  onOkOnclickListener okOnclickListener;
    private  String url;
    public MyDialog(String url,@NonNull Context context, @StyleRes int themeResId) {
        super(context,themeResId);
        this.url=url;
    }
    public void setCancelOnclickListener(String str,onCancelOnclickListener cancelOnclickListener){
        if (str!=null){
            cancelstr = str;
        }
        this.cancelOnclickListener =cancelOnclickListener;
    }
    public void setOkOnclickListener(String str,onOkOnclickListener okOnclickListener){
        if (str!=null){
            okstr= str;
        }
        this.okOnclickListener = okOnclickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public String getValue(){
        return value.getText().toString();
    }



    private void initData() {
        //如果设置按钮文字
        if (okstr!= null) {
            ok.setText(okstr);
        }
        if (cancelstr!= null) {
            cancel.setText(cancelstr);
        }
        if (url!=null){
            value.setText(url);
        }
    }

    private void initEvent() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okOnclickListener != null) {
                    okOnclickListener.onOkOnclick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick();
                }
            }
        });


    }

    private void initView() {
        title =findViewById(R.id.title);
        value =findViewById(R.id.value);
        ok = findViewById(R.id.ok);
        cancel =findViewById(R.id.cancel);

    }
    public interface onCancelOnclickListener {
        public void onCancelClick();
    }

    public interface onOkOnclickListener {
        public void onOkOnclick();
    }


}

