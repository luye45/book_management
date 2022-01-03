package com.example.books_management.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.books_management.R;
import com.example.books_management.entity.User;
import com.example.books_management.util.db.GetDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ShowUserActivity extends AppCompatActivity {

    private TextView tv_user_id_show;
    private TextView tv_user_name_show;
    private TextView tv_user_password_show;
    private TextView tv_user_sex_show;
    private TextView tv_user_birth_show;
    private TextView tv_user_mobile_show;
    private ImageButton bt_back;
    private Button my_title_edit;
    private Intent globalIntent;
    private GetDatabase database;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_show);
        initView();
        setView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        user = null;
        System.out.println(requestCode);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    user = queryUser(data.getStringExtra("id"));
                    this.user = user;
                    showData();
                    System.out.println(user);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(globalIntent.getStringExtra("id").equals("190812021")){
            setResult(RESULT_OK);
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
            startActivity(intent);
        }
        finish();
    }

    private void initView() {
        tv_user_id_show = findViewById(R.id.tv_user_id_show);//用户名
        tv_user_name_show = findViewById(R.id.tv_user_name_show);//姓名
        tv_user_sex_show = findViewById(R.id.tv_user_sex_show);//性别
        tv_user_password_show = findViewById(R.id.tv_user_password_show);//密码
        tv_user_birth_show = findViewById(R.id.tv_user_birth_show);//出生年月
        tv_user_mobile_show = findViewById(R.id.tv_user_mobile_show);//手机号
        bt_back = findViewById(R.id.bt_back);
        my_title_edit = findViewById(R.id.my_title_edit);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        globalIntent = getIntent();
        user = queryUser(globalIntent.getStringExtra("id"));
    }

    private void setView() {
        showData();
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        my_title_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowUserActivity.this, UpdateUserActivity.class);
                intent.putExtra("id",globalIntent.getStringExtra("id"));
                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                startActivityForResult(intent,1);
            }
        });
    }

    private void showData() {
        tv_user_id_show.setText(user.getId());
        tv_user_name_show.setText("姓名：" + user.getName());
        tv_user_password_show.setText("密码：" + user.getPassword());
        tv_user_sex_show.setText("性别：" + user.getSex());
        tv_user_birth_show.setText("出生年月：" + user.getBirth());
        tv_user_mobile_show.setText("手机号码：" + user.getMobile());
    }

    private User queryUser(String id){
        User user = null;
//        try {
//            cursor = sqLiteDatabase.query("userInfo",null,"id = ?",new String[]{id},null,null,null);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        cursor = sqLiteDatabase.query("userInfo",null,"id = ?",new String[]{id},null,null,null);
        if (cursor.moveToFirst()){
            do {
                id = cursor.getString(cursor.getColumnIndex("id"));
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
                user = new User(id,name,password,sex,birth,mobile);
            }while (cursor.moveToNext());
        }
        System.out.println(user);
        return user;
    }
}