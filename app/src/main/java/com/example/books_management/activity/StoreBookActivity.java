package com.example.books_management.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.books_management.R;
import com.example.books_management.entity.Book;
import com.example.books_management.util.db.GetDatabase;

import java.io.ByteArrayOutputStream;

public class StoreBookActivity extends AppCompatActivity {

    EditText add_book_id_text;
    EditText add_book_name_text;
    Spinner add_book_category_text;
    EditText add_book_author_text;
    Spinner add_book_press_text;
    EditText add_book_price_text;
    RatingBar add_book_recommendation_text;
    EditText add_book_introduction_text;
    Button AddBookActivity_button;
    ImageView add_book_profile;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    ImageButton bt_back;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);
        initView();
        setView();
    }

    @Override
    public void onBackPressed() {

        showAlertDialog();
    }

    private void initView() {
        add_book_id_text = findViewById(R.id.add_book_id_text);//书号
        add_book_name_text = findViewById(R.id.add_book_name_text);//书名
        add_book_category_text = findViewById(R.id.add_book_category_text);//类别
        add_book_author_text = findViewById(R.id.add_book_author_text);//作者
        add_book_press_text = findViewById(R.id.add_book_press_text);//出版社
        add_book_price_text = findViewById(R.id.add_book_price_text);//价格
        add_book_recommendation_text = findViewById(R.id.add_book_recommendation_text);//推荐等级
        add_book_introduction_text = findViewById(R.id.add_book_introduction_text);//简介
        add_book_profile = findViewById(R.id.add_book_profile);//添加封面
        AddBookActivity_button = findViewById(R.id.add_book_button);//书籍添加按钮
        contentValues = new ContentValues();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        bt_back = findViewById(R.id.bt_back);
    }

    private void setView() {
//    监听用户点击添加按钮
        AddBookActivity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                调用数据库插入方法
                insertBook();
            }
        });

        add_book_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewImage();

            }
        });
//监听用户点击返回按钮
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                调用弹出提示框方法
                showAlertDialog();
            }
        });
        //
    }

    private void addNewImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,0x1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x1){
            try {
                Uri uri = data.getData();
                ContentResolver cr = StoreBookActivity.this.getContentResolver();
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                add_book_profile.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private byte[] picTobyte()
    {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //压缩图片，100代表不压缩（0～100）
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }catch (Exception e){
            Toast.makeText(StoreBookActivity.this,"请填写必填参数！", Toast.LENGTH_SHORT).show();
        }
        return bytes;
    }

    //    提示框方法
    private void showAlertDialog(){
        Book book = getStoreBookInfo();
        String id = book.getId();
        String name = book.getName();
        String category = book.getCategory();
        String author = book.getAuthor();
        String press = book.getPress();
        Float price = book.getPrice();
        Float recommendation = book.getRecommendation();
        String introduction = book.getIntroduction();
        byte[] profile = book.getProfile();
        if (!id.equals("") && !name.equals("") && !category.equals("") && !author.equals("") && !press.equals("") && !price.equals("") && !recommendation.equals("") && !introduction.equals("")&&!profile.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(StoreBookActivity.this);
//           设置提示框标题
            builder.setTitle("提示");
//            设置提示框提示文字
            builder.setMessage("离开将会清除所有填写数据，是否保存！");
//            监听提示框点击保存按钮
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    保存数据到数据库方法
                    insertBook();
                }
            });
//            监听用户点击提示框取消按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
//            让提示框显示出来
            builder.show();
        }else{
//            关闭页面
            finish();
        }
    }

    private void insertBook(){
        Book book = getStoreBookInfo();
        String id = book.getId();
        String name = book.getName();
        String category = book.getCategory();
        String author = book.getAuthor();
        String press = book.getPress();
        Float price = book.getPrice();
        Float recommendation = book.getRecommendation();
        String introduction = book.getIntroduction();
        byte[] profile = book.getProfile();
        if(!(id.length()==10||id.length()==13)){
            Toast.makeText(StoreBookActivity.this,"请输入正确的ISBN！", Toast.LENGTH_SHORT).show();
        }else{
            if (!id.equals("") && !name.equals("") && !category.equals("") && !author.equals("") && !press.equals("") && !price.equals("") && !recommendation.equals("") && !introduction.equals("")&&!profile.equals("")){
                System.out.println(name);
//            设置数据库对应字段的值
                contentValues.put("id",id);
                contentValues.put("name",name);
                contentValues.put("category",category);
                contentValues.put("author",author);
                contentValues.put("press",press);
                contentValues.put("price",price);
                contentValues.put("recommendation",recommendation);
                contentValues.put("introduction",introduction);
                contentValues.put("profile",profile);
//            将数据插入到数据库中
                sqLiteDatabase.insert("bookInfo",null,contentValues);
                contentValues.clear();
                Toast.makeText(StoreBookActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StoreBookActivity.this,BookActivity.class));
                finish();
            }else {
                Toast.makeText(StoreBookActivity.this,"请填写必填参数！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Book getStoreBookInfo(){
        return new Book(add_book_id_text.getText().toString(),
                add_book_name_text.getText().toString(),
                add_book_category_text.getSelectedItem().toString(),
                add_book_author_text.getText().toString(),
                add_book_press_text.getSelectedItem().toString(),
                Float.parseFloat(add_book_price_text.getText().toString()),
                add_book_recommendation_text.getRating(),
                add_book_introduction_text.getText().toString(),picTobyte());
    }

}