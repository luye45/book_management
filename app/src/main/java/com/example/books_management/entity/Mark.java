package com.example.books_management.entity;

import java.io.Serializable;
import java.sql.Date;

public class Mark implements Serializable {
    private String userId;//用户id
    private String bookId;//

    public Mark(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
