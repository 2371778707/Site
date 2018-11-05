package com.ch.site.view;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ch.site.R;
import java.util.ArrayList;
import java.util.List;


public class MyDialogC extends Dialog{
    private Button ok;
    private Button cancel;
    private EditText value1;
    private  String okstr,cancelstr;
    private  onCancelOnclickListener cancelOnclickListener;
    private  onOkOnClickListener okOnclickListener;
    private  onSpinOnitemClickListener spinOnitemClickLisenter;
    private  Spinner spin;
    private SQLiteDatabase db;
    private List<String> list;
    private List<String> list1;
    private String ti,vi;

    public MyDialogC(@NonNull Context context,String url)
    {
        super(context);
        value1.setText(url);
    }
    public MyDialogC(SQLiteDatabase db,@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.db = db;
    }

    public void setCancelOnclickListener(String str,onCancelOnclickListener cancelOnclickListener){
        if (str!=null){
            cancelstr = str;
        }
        this.cancelOnclickListener =cancelOnclickListener;
    }
    public void setOkOnclickListener(String str,onOkOnClickListener okOnclickListener){
        if (str!=null){
            okstr= str;
        }
        this.okOnclickListener = okOnclickListener;
    }

    public  void  setSpinOnitemClickLisenter(onSpinOnitemClickListener spinOnitemClickLisenter){
        this.spinOnitemClickLisenter =spinOnitemClickLisenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog1);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okOnclickListener != null) {
                    okOnclickListener.onOkOnclick();
                    vi = value1.getText().toString();
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

       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (spinOnitemClickLisenter!=null){
                   spinOnitemClickLisenter.onSpinItemClick(i);
                   value1.setText(list1.get(i));
                   ti = list.get(i);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

    }

    public String getVi(){
        vi= value1.getText().toString();
        return vi;
    }

    public String getTi() {
        return ti;
    }

    private void initData() {
        if (okstr!= null) {
            ok.setText(okstr);
        }
        if (cancelstr!= null) {
            cancel.setText(cancelstr);
        }
    }


    private void initView() {
          value1=findViewById(R.id.value1);
          spin=findViewById(R.id.spin);
          ok=findViewById(R.id.ok);
          cancel =findViewById(R.id.cancel);
          list =new ArrayList<>();
          list1 =new ArrayList<>();
          list = findtitle();
          list1 = findvalue();
          String[] array=list.toArray(new String[list.size()]);
          ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,array);
          spin.setAdapter(adapter);
    }

    public List<String> findtitle(){
        Cursor cursor = db.query("Info",new String[]{"title"},null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title= cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        return list;
    }

    public List<String> findvalue(){
        Cursor cursor = db.query("Info",new String[]{"value"},null,null,null,null,null,null);
        while (cursor.moveToNext()) {
            String value= cursor.getString(cursor.getColumnIndex("value"));
            list1.add(value);
        }
        return list1;
    }
    public interface onCancelOnclickListener {
        public void onCancelClick();
    }

    public interface onOkOnClickListener {
        public void onOkOnclick();
    }
    public  interface  onSpinOnitemClickListener{
        public void  onSpinItemClick(int pos);
    }

    }
