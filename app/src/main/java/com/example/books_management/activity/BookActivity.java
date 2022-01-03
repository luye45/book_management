package com.example.books_management.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.books_management.adapter.BookAdapter;
import com.example.books_management.entity.Book;
import com.example.books_management.util.db.GetDatabase;
import com.example.books_management.R;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    ListView main_book_list;
    List<Book> list;
    GetDatabase database;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    BookAdapter bookAdapter;
    ImageButton edit_button;
    Intent globalIntent;
    SharedPreferences.Editor dataEdit;
    SharedPreferences getDataEdit;
    Button main_add_book;
    Button main_go_search;
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
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
                Intent intent = new Intent(BookActivity.this, ShowBookActivity.class);
                intent.putExtra("id",bookId);
                startActivityForResult(intent,2);
            }
        });

        main_book_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(BookActivity.this,main_book_list);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_admin,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.update:
                                Intent intent = new Intent(BookActivity.this, UpdateBookActivity.class);
                                globalIntent = getIntent();
                                Book book =list.get(position);
                                intent.putExtra("id",book.getId());
                                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivityForResult(intent,1);
                                break;
                            case R.id.delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("确认删除");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteBookItem(position);
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

        main_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookActivity.this, StoreBookActivity.class);
                startActivityForResult(intent,3);
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(BookActivity.this,edit_button);
                popupMenu.getMenuInflater().inflate(R.menu.menu_admin,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.manage_user:
                                globalIntent = getIntent();
                                Intent intent = new Intent(BookActivity.this,UserActivity.class);
                                intent.putExtra("user_id",globalIntent.getStringExtra("user_id"));
                                startActivity(intent);
                                //startActivity(new Intent(BookActivity.this,UserActivity.class));
                                finish();
                                break;
                            case R.id.exit_login:
                                dataEdit.putBoolean("isAutoLogin",false);
                                dataEdit.commit();
                                startActivity(new Intent(BookActivity.this, LoginActivity.class));
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
                Intent intent = new Intent(BookActivity.this, SearchActivity.class);
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

    private void deleteBookItem(int i){
        Book book = list.get(i);
        list.remove(i);
        sqLiteDatabase.delete("bookInfo","id = ?", new String[]{
                book.getId()
        });
        Toast.makeText(BookActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
        bookAdapter.notifyDataSetChanged();
    }

    private int queryID(String name, String model, String factory, String introduction) {
        int id = -1;
        cursor = sqLiteDatabase.query("book_table",null,"name = ? and model = ? and factory = ? and introduction = ?", new String[]{
                name, model, factory, introduction
        },null, null,null);

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }while (cursor.moveToNext());
        }
        return id;
    }
}