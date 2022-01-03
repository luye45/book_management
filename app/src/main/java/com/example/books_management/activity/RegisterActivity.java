package com.example.books_management.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.books_management.R;
import com.example.books_management.util.db.GetDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    Button btn_register;
    ImageButton bt_back;
    EditText et_reg_id;
    EditText et_reg_name;
    EditText et_reg_password;
    EditText et_reg_password_repeat;
    EditText et_reg_mobile;
    Spinner spn_sex;
    TextView tv_reg_birth;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        setView();
    }

    private void initView() {
        btn_register = findViewById(R.id.register_button);
        bt_back = findViewById(R.id.bt_back);
        et_reg_id = findViewById(R.id.register_id);
        et_reg_name = findViewById(R.id.register_name);
        et_reg_password = findViewById(R.id.register_password);
        et_reg_password_repeat = findViewById(R.id.register_re_password);
        et_reg_mobile = findViewById(R.id.register_mobile);
        spn_sex = findViewById(R.id.register_sex);
        tv_reg_birth = findViewById(R.id.register_birth);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        contentValues = new ContentValues();
    }

    private void setView() {

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tv_reg_birth.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            private DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener(){  //
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    calendar.set(Calendar.YEAR, arg1);
                    calendar.set(Calendar.MONTH, arg2);
                    calendar.set(Calendar.DAY_OF_MONTH, arg3);
                    updateDate();
                }
            };
            //当DatePickerDialog关闭，更新日期显示
            private void updateDate(){
                SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
                tv_reg_birth.setText(df.format(calendar.getTime()));
            }
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this,listener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
            
        });
    }



    private String getId(){
        return et_reg_id.getText().toString();
    }

    private String getName(){return et_reg_name.getText().toString();}

    private String getPassword(){
        return et_reg_password.getText().toString();
    }

    private String getPasswordRepeat(){
        return et_reg_password_repeat.getText().toString();
    }

    private String getSex(){
        return spn_sex.getSelectedItem().toString();
    }

    private String getBirth(){
        return tv_reg_birth.getText().toString();
    }

    private String getMobile(){
        return et_reg_mobile.getText().toString();
    }
    private void registerUser() {
        String id = getId();
        String name = getName();
        String password = getPassword();
        String passwordRepeat= getPasswordRepeat();
        String sex = getSex();
        String birth = getBirth();
        String mobile = getMobile();

        if (!id.equals("") && !password.equals("") && !passwordRepeat.equals("")){
            if(!(mobile.length()==11)){
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号！", Toast.LENGTH_SHORT).show();
            }else {
                if (password.equals(passwordRepeat)) {
                    cursor = sqLiteDatabase.query("userInfo", null, "id = ?", new String[]{id}, null, null, null);
                    if (cursor.moveToFirst()) {
                        Toast.makeText(RegisterActivity.this, "账号已存在！", Toast.LENGTH_SHORT).show();
                    } else {
                        contentValues.put("id", id);
                        contentValues.put("name", name);
                        contentValues.put("password", password);
                        contentValues.put("mobile", mobile);
                        contentValues.put("sex", sex);
                        contentValues.put("birth", birth);
                        sqLiteDatabase.insert("userInfo", null, contentValues);
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(RegisterActivity.this,"参数不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
}