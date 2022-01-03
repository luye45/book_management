package com.example.books_management.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.books_management.R;
import com.example.books_management.entity.User;
import com.example.books_management.util.db.GetDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateUserActivity extends AppCompatActivity {

    EditText et_user_id;
    EditText et_user_name;
    Spinner spn_user_sex;
    EditText et_user_password;
    EditText tv_user_birth;
    ImageButton btn_user_birth;
    EditText et_user_mobile;
    Button btn_user_update;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;
    Intent globalIntent;
    ImageButton btn_back;
    String id;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_update);
        initView();
        setView();
    }


    private void initView() {
        et_user_id = findViewById(R.id.et_user_id_update);//用户名
        et_user_name = findViewById(R.id.et_user_name_update);//姓名
        spn_user_sex = findViewById(R.id.spn_user_sex_update);//性别
        et_user_password = findViewById(R.id.et_user_password_update);//密码
        tv_user_birth = findViewById(R.id.tv_user_birth_update);//生日
        btn_user_birth = findViewById(R.id.btn_user_birth_update);//更新
        et_user_mobile = findViewById(R.id.et_user_mobile_update);//手机号
        btn_user_update = findViewById(R.id.btn_user_update);//更新
        contentValues = new ContentValues();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        btn_back = findViewById(R.id.bt_back);
        contentValues = new ContentValues();
        globalIntent = getIntent();
        id = String.valueOf(globalIntent.getStringExtra("id"));
        //System.out.println(id);
        user = queryUser(id);
//        if (!globalIntent.getStringExtra("user_id").equals("admin")) {
//            et_user_id.setEnabled(false);
//        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void setView() {
        showData();
        btn_user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用数据库更新方法
                updateUser(id);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });
        btn_user_birth.setOnClickListener(new View.OnClickListener() {
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
                tv_user_birth.setText(df.format(calendar.getTime()));
            }
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateUserActivity.this,listener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否保存当前修改");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateUser(id);
                Toast.makeText(UpdateUserActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                setResult(RESULT_CANCELED, intent);
                finish();
                finish();
            }
        });
        builder.show();
    }

    private void updateUser(String id) {
        System.out.println("传递过来的id：" + id);
//        if (Integer.parseInt(id) < 0) {
//            Toast.makeText(UpdateUserActivity.this, "数据异常！", Toast.LENGTH_SHORT).show();
//        } else {
            User user = getEditUserInfo();
            if(user.getMobile().length()!=11){
                Toast.makeText(UpdateUserActivity.this,"请输入正确的手机号！", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println(user.getName());
                contentValues.put("name", user.getName());
                contentValues.put("password", user.getPassword());
                contentValues.put("sex", user.getSex());
                contentValues.put("mobile", user.getMobile());
                contentValues.put("birth", user.getBirth().toString());
                sqLiteDatabase.update("userInfo", contentValues, "id = ?", new String[]{id});
                contentValues.clear();
                Toast.makeText(UpdateUserActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("id", id);
                System.out.println("设置的 ID:" + id);
                setResult(RESULT_OK, intent);
                finish();
            }

    }


    private void showData() {
        et_user_id.setText(user.getId());
        et_user_name.setText(user.getName());
        et_user_password.setText(user.getPassword());
        setSpinnerItemSelectedByValue(spn_user_sex,user.getSex());
        et_user_mobile.setText(user.getMobile());
        tv_user_birth.setText(user.getBirth().toString());
    }

    private User queryUser(String id) {
        User user = null;
        cursor = sqLiteDatabase.query("userInfo", null, "id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //id = cursor.getString(cursor.getColumnIndex("id"));
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
            } while (cursor.moveToNext());
        }
        return user;
    }

    private User getEditUserInfo() {
        try {
            user = new User(et_user_id.getText().toString(),et_user_name.getText().toString(),et_user_password.getText().toString(),spn_user_sex.getSelectedItem().toString(), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(tv_user_birth.getText().toString()).getTime()),et_user_mobile.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }
}
