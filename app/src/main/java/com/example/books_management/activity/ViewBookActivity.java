package com.example.books_management.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.books_management.R;
import com.example.books_management.entity.Book;
import com.example.books_management.util.db.GetDatabase;

public class ViewBookActivity extends ShowBookActivity {
    TextView tv_book_id_show;
    TextView tv_book_name_show;
    TextView tv_book_category_show;
    TextView tv_book_author_show;
    TextView tv_book_press_show;
    TextView tv_book_price_show;
    TextView tv_book_recommendation_show;
    TextView tv_book_introduction_show;
    ImageView imv_book_profile_show;
    ImageButton bt_back;
    Button my_title_edit;
    Intent globalIntent;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        initView();
        setView();
    }

    private void initView() {
        tv_book_id_show = findViewById(R.id.book_id_show);
        tv_book_name_show = findViewById(R.id.book_name_show);
        tv_book_category_show = findViewById(R.id.book_category_show);
        tv_book_author_show = findViewById(R.id.book_author_show);
        tv_book_press_show = findViewById(R.id.book_press_show);
        tv_book_price_show = findViewById(R.id.book_price_show);
        tv_book_recommendation_show = findViewById(R.id.book_recommendation_show);
        tv_book_introduction_show = findViewById(R.id.book_introduction_show);
        imv_book_profile_show = findViewById(R.id.book_profile_show);
        bt_back = findViewById(R.id.bt_back);
        my_title_edit = findViewById(R.id.my_title_edit);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        globalIntent = getIntent();
        book = queryBook(globalIntent.getStringExtra("id"));
    }

    private void setView() {
        showData();
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void showData() {
        tv_book_id_show.setText("ISBN:" + book.getId());
        tv_book_name_show.setText(book.getName());
        tv_book_category_show.setText("类型："+book.getCategory());
        tv_book_author_show.setText("作者："+book.getAuthor());
        tv_book_press_show.setText("出版社："+book.getPress());
        tv_book_price_show.setText("¥ "+book.getPrice().toString());
        tv_book_recommendation_show.setText(getStars(book.getRecommendation()));
        tv_book_introduction_show.setText(book.getIntroduction());
        imv_book_profile_show.setImageBitmap(BitmapFactory.decodeByteArray(book.getProfile(), 0,book.getProfile().length));
    }

    private String getStars(Float rank){
        String stars="";
        switch(rank.toString()){
            case "1.0": stars = "⭐";break;
            case "2.0": stars = "⭐⭐";break;
            case "3.0": stars = "⭐⭐⭐";break;
            case "4.0": stars = "⭐⭐⭐⭐";break;
            case "5.0": stars = "⭐⭐⭐⭐⭐";break;
        }
        return stars;
    }

    private Book queryBook(String id){
        Book book = null;
        cursor = sqLiteDatabase.query("bookInfo",null,"id = ?",new String[]{id},null,null,null);
        if (cursor.moveToFirst()){
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
        System.out.println(book);
        return book;
    }

}