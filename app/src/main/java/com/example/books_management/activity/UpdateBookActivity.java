package com.example.books_management.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.books_management.R;
import com.example.books_management.entity.Book;
import com.example.books_management.util.db.GetDatabase;

import java.io.ByteArrayOutputStream;

public class UpdateBookActivity extends AppCompatActivity {

    EditText et_book_id;
    EditText et_book_name;
    Spinner spn_book_category;
    EditText et_book_author;
    Spinner spn_book_press;
    EditText et_book_price;
    RatingBar rb_book_recommendation;
    EditText et_book_introduction;
    ImageView imv_book_profile;
    Button edit_book_button;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;
    Bitmap bitmap;
    Intent globalIntent;
    ImageButton btn_back;
    String id;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_update);
        initView();
        setView();


    }


    private void initView() {
        et_book_id = findViewById(R.id.update_book_id_text);
        et_book_name = findViewById(R.id.update_book_name_text);
        spn_book_category = findViewById(R.id.update_book_category_text);
        et_book_author = findViewById(R.id.update_book_author_text);
        spn_book_press = findViewById(R.id.update_book_press_text);
        et_book_price = findViewById(R.id.update_book_price_text);
        rb_book_recommendation = findViewById(R.id.update_book_recommendation_text);
        imv_book_profile = findViewById(R.id.update_book_profile);
        et_book_introduction = findViewById(R.id.update_book_introduction_text);
        edit_book_button = findViewById(R.id.update_book_button);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        contentValues = new ContentValues();
        globalIntent = getIntent();
        btn_back = findViewById(R.id.bt_back);
        id = globalIntent.getStringExtra("id");
        System.out.println("获取到之前页面传递过来的id：" + id);
        book = queryBook(id);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void setView() {
        showData();
        edit_book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用数据库更新方法
                updateBook(id);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        imv_book_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateImage();
            }
        });

    }

    private void updateImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,0x1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1) {
            try {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                imv_book_profile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBookActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否保存当前修改");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateBook(id);
                Toast.makeText(UpdateBookActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
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

    private void updateBook(String id) {
        System.out.println("传递过来的id：" + id);
            Book book = getEditBookInfo();
            //System.out.println(book.getId().length());
            if( !(book.getId().length()== 10 || book.getId().length()== 13)){
                Toast.makeText(UpdateBookActivity.this,"请输入正确的ISBN！", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println(book.getName());
                contentValues.put("id", book.getId());
                contentValues.put("name", book.getName());
                contentValues.put("category", book.getCategory());
                contentValues.put("author", book.getAuthor());
                contentValues.put("press", book.getPress());
                contentValues.put("price", book.getPrice());
                contentValues.put("recommendation", book.getRecommendation());
                contentValues.put("introduction", book.getIntroduction());
                contentValues.put("profile", book.getProfile());
                sqLiteDatabase.update("bookInfo", contentValues, "id = ?", new String[]{id});
                contentValues.clear();
                Toast.makeText(UpdateBookActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("id", id);
                System.out.println("设置的 ID:" + id);
                setResult(RESULT_OK, intent);
                finish();
            }
    }


    private void showData() {
        et_book_id.setText(book.getId());
        et_book_name.setText(book.getName());
        setSpinnerItemSelectedByValue(spn_book_category,book.getCategory());
        setSpinnerItemSelectedByValue(spn_book_press,book.getPress());
        et_book_price.setText(book.getPrice().toString());
        rb_book_recommendation.setRating(book.getRecommendation());
        et_book_author.setText(book.getAuthor());
        et_book_introduction.setText(book.getIntroduction());
        imv_book_profile.setImageBitmap(BitmapFactory.decodeByteArray(book.getProfile(), 0,book.getProfile().length));
    }

    private Book queryBook(String id) {
        Book book = null;
        cursor = sqLiteDatabase.query("bookInfo", null, "id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String press = cursor.getString(cursor.getColumnIndex("press"));
                Float price = cursor.getFloat(cursor.getColumnIndex("price"));
                Float recommendation = cursor.getFloat(cursor.getColumnIndex("recommendation"));
                String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                byte[] profile = cursor.getBlob(cursor.getColumnIndex("profile"));
                book = new Book(id,name,category,author,press,price,recommendation,introduction,profile);
            }while (cursor.moveToNext());
        }
        return book;
    }

    private Book getEditBookInfo() {
        return new Book(et_book_id.getText().toString(), et_book_name.getText().toString(), spn_book_category.getSelectedItem().toString(), et_book_author.getText().toString(),
                spn_book_press.getSelectedItem().toString(), Float.parseFloat(et_book_price.getText().toString()), rb_book_recommendation.getRating(), et_book_introduction.getText().toString(),picTobyte());
    }

    private byte[] picTobyte()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //压缩图片，100代表不压缩（0～100）
        bitmap = ((BitmapDrawable)(imv_book_profile.getDrawable())).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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
