package com.example.books_management.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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

public class SearchActivity extends AppCompatActivity {

    Button search_button_search;
    Spinner search_by;
    EditText search_text_search;
    SQLiteDatabase sqLiteDatabase;
    GetDatabase database;
    Cursor cursor;
    Intent globalIntent;
    ListView search_book_list;
    BookAdapter bookAdapter;
    ImageButton bt_back;
    List<Book> adapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 4:
                System.out.println("查看是否执行本回调");
                setAdapter();
                break;
        }
    }

    private void initView() {
        search_button_search = findViewById(R.id.search_button_search);
        search_by = findViewById(R.id.spn_search_by);
        search_text_search = findViewById(R.id.search_text_search);
        database = new GetDatabase();
        sqLiteDatabase = database.getDatabase(this).getWritableDatabase();
        search_book_list = findViewById(R.id.search_book_list);
        bt_back = findViewById(R.id.bt_back);
    }

    private void setView() {

        search_text_search.setEnabled(true);
        search_text_search.setFocusable(true);
        search_text_search.setFocusableInTouchMode(true);
        search_text_search.requestFocus();
        search_button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getInputName().equals("")){
                    setAdapter();
                }else {
                    Toast.makeText(SearchActivity.this,"输入为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        search_book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book =adapterList.get(position);
                String bookId = book.getId();
                globalIntent = getIntent();
                String userId = globalIntent.getStringExtra("user_id");
                System.out.println("item的id："+bookId);
                Intent intent;
                if(userId.equals("190812021")){
                    intent = new Intent(SearchActivity.this, ShowBookActivity.class);
                }else{
                    intent = new Intent(SearchActivity.this, ViewBookActivity.class);
                }
                intent.putExtra("user_id",userId);
                intent.putExtra("id",bookId);
                startActivityForResult(intent,4);
            }
        });

        search_book_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBookItem(position);
                        Toast.makeText(SearchActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }



    private List<Book> searchBy(String searchBy, String searchText){
        List<Book> list = new ArrayList<>();
        Book book = null;
        cursor = sqLiteDatabase.query("bookInfo",null,searchBy +" like ?", new String[]{"%"+searchText+"%"},null,null,null);
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
                book = new Book(id,name,category,author,press,price,recommendation,introduction,profile);
                list.add(book);
            }while (cursor.moveToNext());
        }
        return list;
    }

    private void deleteBookItem(int i){
        Book book = adapterList.get(i);
        adapterList.remove(i);
        sqLiteDatabase.delete("bookInfo","id = ?", new String[]{
                book.getId()
        });
        bookAdapter.notifyDataSetChanged();
    }


    private void setAdapter(){
        adapterList = searchBy(getSearchBy(),getInputName());
        bookAdapter = new BookAdapter(SearchActivity.this,adapterList);
        search_book_list.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();
    }

    private String getInputName(){
        return search_text_search.getText().toString();
    }
    private String getSearchBy(){
        String key = search_by.getSelectedItem().toString();
        String result = "name";
        switch (key){
            case "书号":
                result = "id";break;
            case "书名":
                result = "name";break;
            case "作者":
                result = "author";break;
            case "出版社":
                result = "press";break;
            case "类别":
                result = "category";break;
        }
        return result;
    }
}