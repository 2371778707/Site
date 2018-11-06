package com.ch.site.view;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.site.R;
import com.ch.site.bean.Info;
import com.ch.site.db.MyOpenHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private PullToRefreshListView mPullRefreshListView;
    private static final String TAG ="a" ;
    private TextView newi,modify;
    private Handler handler = new Handler();
    private List<Info> list = new ArrayList<Info>();
    private MyAdapter adapter = new MyAdapter();
    private SQLiteDatabase db ;
    private MyOpenHelper helper;
    private ClipData clipData;
    private String text=null;
    private List<String> li1;
    private LinearLayout bg;
    private ClipboardManager cm;
    private MyDialogAbout dialog;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        list =new ArrayList();
        li1= new ArrayList<>();
        helper = new MyOpenHelper(this);
        try {
            db = helper.getWritableDatabase();
        }catch (Exception e){
            Toast.makeText(this, "数据库访问失败,请重启应用", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        mPullRefreshListView =(PullToRefreshListView)findViewById(R.id.listview);
        newi = (TextView)findViewById(R.id.newi);
        modify = (TextView)findViewById(R.id.modify);
        newi.setOnClickListener(this);
        modify.setOnClickListener(this);
        bg =(LinearLayout)findViewById(R.id.bg);
        int num =(int)(Math.random()*6);
        switch (num){
            case 0:
                bg.setBackgroundResource(R.mipmap.bg0);
                break;
            case 1:
                bg.setBackgroundResource(R.mipmap.bg1);
                break;
            case 2:
                bg.setBackgroundResource(R.mipmap.bg2);
                break;
            case 3:
                bg.setBackgroundResource(R.mipmap.bg3);
                break;
            case 4:
                bg.setBackgroundResource(R.mipmap.bg4);
                break;
            case 5:
                bg.setBackgroundResource(R.mipmap.bg5);
                break;

                default:
                    break;
        }

        list = findall();

        if (list!=null){
        adapter = new MyAdapter();
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list = findall();
                        adapter.notifyDataSetChanged();
                        mPullRefreshListView.onRefreshComplete();
                        int num =(int)(Math.random()*8);
                        switch (num){
                            case 0:
                                bg.setBackgroundResource(R.mipmap.bg0);
                                break;
                            case 1:
                                bg.setBackgroundResource(R.mipmap.bg1);
                                break;
                            case 2:
                                bg.setBackgroundResource(R.mipmap.bg2);
                                break;
                            case 3:
                                bg.setBackgroundResource(R.mipmap.bg3);
                                break;
                            case 4:
                                bg.setBackgroundResource(R.mipmap.bg4);
                                break;
                            case 5:
                                bg.setBackgroundResource(R.mipmap.bg5);
                                break;
                            case 6:
                                bg.setBackgroundResource(R.mipmap.bg6);
                                break;
                            case 7:
                                bg.setBackgroundResource(R.mipmap.bg7);
                                break;
                            default:
                                break;
                        }


                    }
                },1000);
            }
        });

        }
    }

    private List<Info> findall() {
        List<Info> list = new ArrayList<>();

        Cursor cursor = db.query("Info", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                info = new Info();
                info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                info.setValue(cursor.getString(cursor.getColumnIndex("value")));
                info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(info);
            }
            return list;

    }

    private boolean findByValue(String value){

       Cursor cursor = db.query("info", new String[]{"value"},"value=?",new String[]{value},null,null,null);

       if (cursor.getCount()==0){
           return true;
       }
       return false;
    }
    private boolean findByTitle(String title){

        Cursor cursor = db.query("info", new String[]{"title"},"title=?",new String[]{title},null,null,null);
        if (cursor.getCount()==0){
            return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                final MyDialogSearch dialogS =new MyDialogSearch(db,this,R.style.MyDialog);
                dialogS.setSearchClickLisenter(new MyDialogSearch.OnSearchClickLisenter() {
                    @Override
                    public void onSearchClick() {

                        if (!dialogS.getTitle().toString().trim().equals("")){
                            list.clear();
                            db.beginTransaction();
                            Cursor cursor = db.query("Info",new String[]{"id","title","value","time"},"title like ?",new String[]{dialogS.getTitle().toString().trim()+"%"},null,null,null);
                            if (cursor.getCount()==0){
                                Toast.makeText(MainActivity.this, "没有搜索到相关内容v_v", Toast.LENGTH_SHORT).show();
                            }else {
                                while (cursor.moveToNext()) {
                                    info = new Info();
                                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                                    String title = cursor.getString(cursor.getColumnIndex("title"));
                                    String value = cursor.getString(cursor.getColumnIndex("value"));
                                    String time = cursor.getString(cursor.getColumnIndex("time"));
                                    info.setId(id);
                                    info.setTitle(title);
                                    info.setValue(value);
                                    info.setTime(time);
                                    list.add(info);
                                }
                                db.setTransactionSuccessful();
                                db.endTransaction();
                                adapter.notifyDataSetChanged();
                                dialogS.dismiss();
                            }

                        }else {

                        }

                    }
                });

                dialogS.show();
                break;
            case R.id.about:
                dialog = new MyDialogAbout(this,R.style.MyDialog);
                dialog.show();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newi:
                final MyDialogNewi dialog = new MyDialogNewi(null,this,R.style.MyDialog);
                dialog.setOkOnclickListener("添加", new MyDialogNewi.onOkOnclickListener() {
                    @Override
                    public void onOkOnclick() {
                        if (dialog.getTitle().equals("") || dialog.getValue().equals("")) {
                            Toast.makeText(MainActivity.this, "添加失败！" , Toast.LENGTH_SHORT).show();
                        } else {
                            if (Patterns.WEB_URL.matcher(dialog.getValue()).matches() && URLUtil.isValidUrl(dialog.getValue())&&contain(dialog.getValue())) {
                               if (findByValue(dialog.getValue())) {
                                   if (dialog.getTitle().length() > 24) {
                                       Toast.makeText(MainActivity.this, "请简化标题！", Toast.LENGTH_SHORT).show();
                                   } else {
                                       if (findByTitle(dialog.getTitle())){
                                       Date date = new Date();
                                       java.sql.Date sqldate = new java.sql.Date(date.getTime());
                                       @SuppressLint("SimpleDateFormat")
                                       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       String date1 = format.format(sqldate);
                                       String sql = "insert into Info (title,value,time)values('" + dialog.getTitle() + "','" + dialog.getValue() + "','" + date1 + "');";
                                       db.execSQL(sql);
                                       list.clear();
                                       list = findall();
                                       adapter.notifyDataSetChanged();
                                       Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                       cm.setText("");
                                       dialog.dismiss();
                                   }
                                   else {
                                           Toast.makeText(MainActivity.this, "换个标题试试^_^", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }else {
                                   Toast.makeText(MainActivity.this, "该网址已存储" , Toast.LENGTH_SHORT).show();
                               }
                            }else {
                                Toast.makeText(MainActivity.this, "网址有误", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                dialog.setCancelOnclickListener("取消", new MyDialogNewi.onCancelOnclickListener() {
                    @Override
                    public void onCancelClick() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.modify:
                Cursor cursor = db.query("Info",null,null,null,null,null,null,null);
                if (cursor.moveToNext()) {

                    final MyDialogModify dialog1 = new MyDialogModify(db, this, R.style.MyDialog);
                    dialog1.setOkOnclickListener("确定", new MyDialogModify.onOkOnClickListener() {
                        @Override
                        public void onOkOnclick() {
                            String  title = dialog1.getTi();
                            String  value =  dialog1.getVi();
                            if (Patterns.WEB_URL.matcher(value).matches()&&URLUtil.isValidUrl(value)&&contain(value)) {
                                if (findByValue(value)) {
                                        String sql = "update  Info set value = '" + value + "' where title='" + title + "';";
                                        db.execSQL(sql);
                                        list.clear();
                                        list = findall();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        cm.setText("");
                                        dialog1.dismiss();

                                } else {
                                    Toast.makeText(MainActivity.this, "该网址已存储", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(MainActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    dialog1.setCancelOnclickListener("取消", new MyDialogModify.onCancelOnclickListener() {
                        @Override
                        public void onCancelClick() {
                            dialog1.dismiss();
                        }
                    });

                    dialog1.setSpinOnitemClickLisenter(new MyDialogModify.onSpinOnitemClickListener() {
                        @Override
                        public void onSpinItemClick(int pos) {
                            if (li1 != null) {
                                li1.clear();
                            }
                            Cursor cursor = db.query("Info", new String[]{"value"}, null, null, null, null, null);
                            while (cursor.moveToNext()) {
                                String value = cursor.getString(cursor.getColumnIndex("value"));

                                li1.add(value);
                            }


                        }
                    });
                    dialog1.show();
                }
                break;
            default:
                break;
        }


    }

    public boolean contain(String url){
        if (url.contains("://")) {
            return true;
        }
        return false;
    }
    @Override
    protected void onResume() {
        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipData = cm.getPrimaryClip();
        if (clipData!=null) {
            ClipData.Item item = clipData.getItemAt(0);
            text = item.getText().toString();
            if (text != null) {
                if (!text.trim().equals("")) {
                    if (findByValue(text)) {
                        if (Patterns.WEB_URL.matcher(text).matches()&&URLUtil.isValidUrl(text)&&contain(text)) {
                                final MyDialogNewi dialog = new MyDialogNewi(text, this, R.style.MyDialog);
                                dialog.setOkOnclickListener("添加", new MyDialogNewi.onOkOnclickListener() {
                                    @Override
                                    public void onOkOnclick() {

                                            if (dialog.getTitle().equals("") || dialog.getTitle() == null) {
                                                Toast.makeText(MainActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (dialog.getTitle().length() > 24) {
                                                    Toast.makeText(MainActivity.this, "请简化标题", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (findByTitle(dialog.getTitle())) {
                                                        Date date = new Date();
                                                        java.sql.Date sqldate = new java.sql.Date(date.getTime());
                                                        @SuppressLint("SimpleDateFormat")
                                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        String date1 = format.format(sqldate);
                                                        String sql = "insert into Info (title,value,time)values('" + dialog.getTitle() + "','" + text + "','" + date1 + "');";
                                                        db.beginTransaction();
                                                        try {
                                                            db.execSQL(sql);
                                                            db.setTransactionSuccessful();
                                                        } catch (Exception e) {
                                                            throw e;
                                                        } finally {
                                                            db.endTransaction();
                                                        }
                                                        if (list != null) {
                                                            list.clear();
                                                        }
                                                        list = findall();
                                                        adapter.notifyDataSetChanged();
                                                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                                        cm.setText("");
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "换个标题试试^_^", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                    }
                                });
                                dialog.setCancelOnclickListener("取消", new MyDialogNewi.onCancelOnclickListener() {
                                    @Override
                                    public void onCancelClick() {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                        }
                    }

                }
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setText("");
        super.onPause();
    }

    class MyAdapter extends BaseAdapter {

        List<DragView> views = new ArrayList<DragView>();
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_item , null);
                convertView.setTag(holder);
                final DragView view = (DragView) convertView.findViewById(R.id.drag_view);
                views.add(view);
                view.setOnDragStateListener(new DragView.DragStateListener() {
                    @Override
                    public void onOpened(DragView dragView) {
                        close(position);
                    }

                    @Override
                    public void onClosed(DragView dragView) {

                    }

                    @Override
                    public void onForegroundViewClick(DragView dragView , View v) {
                        int pos = (int) dragView.getTag();
                        Info info =new Info();
                        info=(Info)getItem(pos);
                        String url = info.getValue();
                        if (Patterns.WEB_URL.matcher(url).matches() || URLUtil.isValidUrl(url)) {
                            try{

                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                            startActivity(intent);
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, "无法访问！", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else {
                            Toast.makeText(MainActivity.this, "无法访问", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onBackgroundViewClick(DragView dragView , View v) {
                        int pos = (int) dragView.getTag();
                        Info info =(Info)getItem(pos);
                        db.delete("Info","id=?",new String[]{String.valueOf(info.getId())});
                        list.clear();
                        list =findall();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this ,info.getTitle()+"已被删除", Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dv = (DragView) convertView.findViewById(R.id.drag_view);
            holder.dv.setTag(position);
            holder.dv.close();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            Info info = new Info();
            info = (Info) getItem(position);
            holder.tv.setText(info.getTitle());
            holder.tv1.setText(info.getTime());
            return convertView;
        }

        class ViewHolder{
            DragView dv;
            TextView tv;
            TextView tv1;
        }

        public void close(int pos){
            for (int i = 0; i < views.size(); i++) {
                if (i == pos) {

                } else {
                    if (views.get(i).isOpen())
                        views.get(i).closeAnim();

                       }
            }
        }
    }
}
