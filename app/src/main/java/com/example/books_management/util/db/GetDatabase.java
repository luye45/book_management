package com.example.books_management.util.db;

import android.content.Context;

public class GetDatabase {
//    public CommodityDataBase getDatabase(Context context){
//        return new CommodityDataBase(context,"commodity_database",null,1);
//    }
//    public UserDataBase getDatabase(Context context){
//        return new UserDataBase(context,"user_database",null,1);
//    }
//    public BookDataBase getDatabase(Context context){
//        return new BookDataBase(context,"book_database",null,1);
//    }
//    public MarkDataBase getDatabase(Context context){
//        return new MarkDataBase(context,"mark_database",null,1);
//    }
    public DataBaseHelper getDatabase(Context context){
        return new DataBaseHelper(context,"database",null,1);
    }
}
