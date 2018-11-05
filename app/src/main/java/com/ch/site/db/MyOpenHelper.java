package com.ch.site.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper{
    public static final String sql = "create table" +
            " Info(id integer primary key autoincrement" +
            ",title varchar(200)," +
            "value varchar(200)," +
            "time varchar(20))";

    public  MyOpenHelper(Context context){
        super(context,"record.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
