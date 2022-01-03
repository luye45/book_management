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
import com.example.books_management.entity.Mark;
import com.example.books_management.util.db.GetDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MarkActivity extends AppCompatActivity {
    ListView main_mark_list;
    List<Mark> marks;
    List<Book> books;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    BookAdapter bookAdapter;
    Intent globalIntent;
    ImageButton edit_button;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
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
        main_mark_list = findViewById(R.id.list_mark_main);
        marks = new ArrayList<Mark>();
        books = new ArrayList<Book>();
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        edit_button = findViewById(R.id.edit_button);
        dataEdit = getSharedPreferences("user_data",MODE_PRIVATE).edit();
        getDataEdit = getSharedPreferences("user_data",MODE_PRIVATE);
        onLoadMarkList();
    }

    private void setView() {
        main_mark_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = books.get(position);
                String bookId = book.getId();
                System.out.println("item的id："+bookId);
                globalIntent = getIntent();
                Intent intent = new Intent(MarkActivity.this, ViewBookActivity.class);
                intent.putExtra("id",bookId);
                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                startActivityForResult(intent,2);
            }
        });

        main_mark_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MarkActivity.this,main_mark_list);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_user_cancel,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mark:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MarkActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("确认取消收藏");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteMarkItem(position);
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

//        main_add_mark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MarkActivity.this, LoginActivity.class);
//                startActivityForResult(intent,3);
//            }
//        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MarkActivity.this,edit_button);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.index:
                                Intent intent = new Intent(MarkActivity.this, MainActivity.class);
                                globalIntent = getIntent();
                                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.my_info:
                                Intent intent2 = new Intent(MarkActivity.this, UpdateMyInfoActivity.class);
                                globalIntent = getIntent();
                                intent2.putExtra("id",globalIntent.getStringExtra("user_id"));
                                intent2.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivity(intent2);
                                finish();
                                break;
                            case R.id.exit_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                startActivity(new Intent(MarkActivity.this, LoginActivity.class));
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

    private void onLoadMarkList(){
        getMarkList();
        //List<String> list = new ArrayList<String>();
        Iterator<Mark> it = marks.iterator();
        while(it.hasNext()){
            cursor = sqLiteDatabase.query("bookInfo",null,"id="+it.next().getBookId(), null,null,null,null);
            if (cursor.moveToFirst()){
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
                books.add(book);
            }while (cursor.moveToNext());
        }
        bookAdapter = new BookAdapter(this, books);
        main_mark_list.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();
    }

    private void deleteMarkItem(int i){
        Mark mark = marks.get(i);
        Book book = books.get(i);
        books.remove(i);
        sqLiteDatabase.delete("markInfo","user_id = ? and book_id= ?", new String[]{
                mark.getUserId(),mark.getBookId()
        });
        Toast.makeText(MarkActivity.this,"取消收藏成功!",Toast.LENGTH_SHORT).show();
        bookAdapter.notifyDataSetChanged();
    }

    private void getMarkList(){
        cursor = sqLiteDatabase.query("markInfo",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                String bookId = cursor.getString(cursor.getColumnIndex("book_id"));
                Mark mark = new Mark(userId,bookId);
                marks.add(mark);
            }while (cursor.moveToNext());
        }
    }

//    private int queryID(String name, String sex, String mobile, Date birth) {
//        int id = -1;
//        cursor = sqLiteDatabase.query("markInfo",null,"name = ? and sex = ? and mobile = ? and birth = ?", new String[]{
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