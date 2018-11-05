package com.ch.site.view;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.ch.site.R;

import java.util.ArrayList;
import java.util.List;


public class MyDialogS extends Dialog {
    private String key = null;
    private EditText myedit;
    private Button btn;
    private SQLiteDatabase db;
    private OnSearchClickLisenter searchClickLisenter;
    private List<String> list;
    private String title;

    public MyDialogS(SQLiteDatabase db, @NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.db=db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
    }

    public void  setSearchClickLisenter(OnSearchClickLisenter searchClickLisenter){
        this.searchClickLisenter =searchClickLisenter;
    }
    private void initEvent() {
        list =findtitle();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchClickLisenter.onSearchClick();
            }
        });

    }
    public String  getTitle(){
        title = myedit.getText().toString();
        return title;
    }


    public List<String> findtitle(){
        Cursor cursor = db.query("Info",new String[]{"title"},null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title= cursor.getString(cursor.getColumnIndex("title"));
            list.add(title);
        }
        return list;
    }

    private void initView() {
        myedit = findViewById(R.id.auto);
        btn =findViewById(R.id.btn);
        list =new ArrayList<>();
    }

    public interface  OnSearchClickLisenter{
           public void onSearchClick();
    }
}
