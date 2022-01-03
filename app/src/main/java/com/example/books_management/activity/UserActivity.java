package com.example.books_management.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.books_management.adapter.UserAdapter;
import com.example.books_management.entity.Book;
import com.example.books_management.entity.User;
import com.example.books_management.util.db.GetDatabase;
import com.example.books_management.R;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    ListView main_user_list;
    List<User> list;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    UserAdapter userAdapter;
    Intent globalIntent;
    ImageButton edit_button;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        setView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
            case 3:
                initView();
                break;
        }
    }

    private void initView() {
        main_user_list = findViewById(R.id.list_user_main);
        list = new ArrayList<User>();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        edit_button = findViewById(R.id.edit_button);
        dataEdit = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        getDataEdit = getSharedPreferences("user_data",MODE_PRIVATE);
        onLoadUserList();
    }

    private void setView() {
        main_user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user =list.get(position);
                String userId = user.getId();
                System.out.println("item的id："+userId);
                globalIntent = getIntent();
                Intent intent = new Intent(UserActivity.this, ShowUserActivity.class);
                intent.putExtra("id",userId);
                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                startActivityForResult(intent,2);
            }
        });

        main_user_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(UserActivity.this,main_user_list);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_admin,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.update:
                                Intent intent = new Intent(UserActivity.this, UpdateUserActivity.class);
                                globalIntent = getIntent();
                                User user =list.get(position);
                                intent.putExtra("id",user.getId());
                                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivityForResult(intent,1);
                                break;
                            case R.id.delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("确认删除");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteUserItem(position);
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                                //startActivity(new Intent(BookActivity.this,UserActivity.class));
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

//        main_add_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
//                startActivityForResult(intent,3);
//            }
//        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(UserActivity.this,edit_button);
                popupMenu.getMenuInflater().inflate(R.menu.menu_admin,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.manage_book:
                                startActivity(new Intent(UserActivity.this,BookActivity.class));
                                finish();
                                break;
                            case R.id.exit_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                                finish();
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void onLoadUserList(){
        cursor = sqLiteDatabase.query("userInfo",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                Date birth = null;
                try {
                    birth = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex("birth"))).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                User user = new User(id,password,name,sex,birth,mobile);
                list.add(user);
            }while (cursor.moveToNext());
        }
        userAdapter = new UserAdapter(this,list);
        main_user_list.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void deleteUserItem(int i){
        User user = list.get(i);
        list.remove(i);
        sqLiteDatabase.delete("userInfo","id = ?", new String[]{
                user.getId()
        });
        userAdapter.notifyDataSetChanged();
    }

//    private int queryID(String name, String sex, String mobile, Date birth) {
//        int id = -1;
//        cursor = sqLiteDatabase.query("userInfo",null,"name = ? and sex = ? and mobile = ? and birth = ?", new String[]{
//                name, sex, mobile, birth.toString()
//        },null, null,null);
//
//        if (cursor.moveToFirst()){
//            do {
//                id = cursor.getInt(cursor.getColumnIndex("id"));
//            }while (cursor.moveToNext());
//        }
//        return id;
//    }
}