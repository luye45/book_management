package com.example.books_management.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.books_management.R;
import com.example.books_management.entity.Book;

import java.util.List;


//定义列表的适配器
public class BookAdapter extends BaseAdapter {
    List<Book> book;
    Context context;
    private ViewHolder viewHolder;
//构造参数，传入列表要用的数据以及上下文
    public BookAdapter(Context context, List<Book> book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public int getCount() {
        return book.size();
    }

    @Override
    public Object getItem(int position) {
        return book.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Book book = (Book) this.getItem(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.book_item,null);
            viewHolder.tv_book_name = convertView.findViewById(R.id.book_name);
            viewHolder.tv_book_press = convertView.findViewById(R.id.book_press);
            viewHolder.tv_book_author = convertView.findViewById(R.id.book_author);
            viewHolder.tv_book_recommendation = convertView.findViewById(R.id.book_recommendation);
            viewHolder.imv_book_profile = convertView.findViewById(R.id.book_profile);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//设置标题
        viewHolder.tv_book_name.setText(book.getName());
        viewHolder.tv_book_press.setText(book.getPress());
        viewHolder.tv_book_author.setText("作者：" + book.getAuthor());
        viewHolder.tv_book_recommendation.setText(getStars(book.getRecommendation()));
        viewHolder.imv_book_profile.setImageBitmap(BitmapFactory.decodeByteArray(book.getProfile(), 0,book.getProfile().length));

        return convertView;
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

    class ViewHolder{
        TextView tv_book_name;
        TextView tv_book_press;
        TextView tv_book_author;
        TextView tv_book_recommendation;
        ImageView imv_book_profile;
    }
}
