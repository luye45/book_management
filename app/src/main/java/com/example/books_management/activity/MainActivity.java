package com.example.books_management.activity;

import android.content.ContentValues;
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

import com.example.books_management.R;
import com.example.books_management.adapter.BookAdapter;
import com.example.books_management.entity.Book;
import com.example.books_management.util.db.GetDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView main_book_list;
    List<Book> list;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    BookAdapter bookAdapter;
    ImageButton edit_button;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;
    private Intent globalIntent;
    Button main_add_book;
    Button main_go_search;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        main_book_list = findViewById(R.id.list_book);
        list = new ArrayList<Book>();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        edit_button = findViewById(R.id.edit_button);
        dataEdit = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        getDataEdit = getSharedPreferences("user_data",MODE_PRIVATE);
        main_add_book = findViewById(R.id.btn_book_add);
        main_go_search = findViewById(R.id.btn_book_search);
        onLoadBookList();
    }

    private void setView() {
        main_book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book =list.get(position);
                String bookId = book.getId();
                System.out.println("item的id："+bookId);
                Intent intent = new Intent(MainActivity.this,ViewBookActivity.class);
                globalIntent = getIntent();
                intent.putExtra("id",bookId);
                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                startActivityForResult(intent,2);
            }
        });

        main_book_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,main_book_list);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_user,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mark:
                                        markBookItem(position);
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

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,edit_button);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.my_info:
                                Intent intent = new Intent(MainActivity.this,ShowUserActivity.class);
                                globalIntent = getIntent();
                                intent.putExtra("id",globalIntent.getStringExtra("user_id"));
                                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivity(intent);
                                //startActivity(new Intent(MainActivity.this,UserActivity.class));
                                finish();
                                break;
                            case R.id.my_mark_list:
                                Intent intent2 = new Intent(MainActivity.this, MarkActivity.class);
                                globalIntent = getIntent();
                                intent2.putExtra("id",globalIntent.getStringExtra("user_id"));
                                intent2.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivity(intent2);
                                finish();
                                break;
                            case R.id.exit_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

        main_go_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                globalIntent = getIntent();
                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                startActivityForResult(intent,3);
            }
        });
    }

    private void onLoadBookList(){
        cursor = sqLiteDatabase.query("bookInfo",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String press = cursor.getString(cursor.getColumnIndex("press"));
                Float price = cursor.getFloat(cursor.getColumnIndex("price"));
                Float recommendation = cursor.getFloat(cursor.getColumnIndex("recommendation"));
                String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                byte[] profile = cursor.getBlob(cursor.getColumnIndex("profile"));
                Book book = new Book(id,name,category,author,press,price,recommendation,introduction,profile);
                list.add(book);
            }while (cursor.moveToNext());
        }
        bookAdapter = new BookAdapter(this,list);
        main_book_list.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();
    }

    private void markBookItem(int i){
        Book book = list.get(i);
        String bookId = book.getId();
        globalIntent = getIntent();
        String userId = globalIntent.getStringExtra("user_id");
        cursor = sqLiteDatabase.query("markInfo",null,"book_id = ? and user_id = ?",new String[]{bookId,userId},null,null,null);
        if (cursor.moveToFirst()) {
            list.remove(i);//
            list.add(book);
            bookAdapter.notifyDataSetChanged();//
            Toast.makeText(MainActivity.this, "已收藏，无须重复操作！", Toast.LENGTH_SHORT).show();
        }else{
            contentValues = new ContentValues();
            contentValues.put("book_id",bookId);
            contentValues.put("user_id",userId);
            sqLiteDatabase.insert("markInfo",null,contentValues);
            Toast.makeText(MainActivity.this,"收藏成功!",Toast.LENGTH_SHORT).show();
            bookAdapter.notifyDataSetChanged();
        }
    }
}