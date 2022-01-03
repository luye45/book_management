package com.example.books_management.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static  final String userInfo =
            "CREATE TABLE IF NOT EXISTS userInfo(" +
                    "id varchar(30) PRIMARY KEY ," +
                    "name varchar(30)," +
                    "password varchar(30)," +
                    "sex char(2)," +
                    "birth date," +
                    "mobile char(11)" +
                    ")";

    private static  final String bookInfo =
            "CREATE TABLE IF NOT EXISTS bookInfo(" +
            "id varchar(30) PRIMARY KEY ," +
            "name varchar(30)," +
            "category varchar(30)," +
            "author varchar(30)," +
            "press varchar (30)," +
            "price float(11)," +
            "recommendation float(5)," +
            "introduction varchar(150)," +
            "profile BLOB" +
            ")";
    private static  final String markInfo =
            "CREATE TABLE IF NOT EXISTS markInfo(" +
                    "user_id varchar(30)," +
                    "book_id varchar(30)" +
                    ")";
    private static  final String suInfo =
            "INSERT INTO userInfo VALUES(" +
                    "'190812021'," +
                    "'刘雨轩',"+
                    "'123456'," +
                    "'女'," +
                    "'1999-04-03'," +
                    "'18347236712'" +
                    ")";
    private Context mContext;
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(bookInfo);
        sqLiteDatabase.execSQL(userInfo);
        sqLiteDatabase.execSQL(markInfo);
        sqLiteDatabase.execSQL(suInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists bookInfo");
        sqLiteDatabase.execSQL("drop table if exists markInfo");
        sqLiteDatabase.execSQL("drop table if exists userInfo");
        onCreate(sqLiteDatabase);
    }
}
