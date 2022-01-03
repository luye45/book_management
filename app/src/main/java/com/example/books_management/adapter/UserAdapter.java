package com.example.books_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.books_management.R;
import com.example.books_management.entity.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private List<User> users;
    private Context context;
    private ViewHolder viewHolder;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = (User) this.getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tv_user_id = convertView.findViewById(R.id.user_id);
            viewHolder.tv_user_sex = convertView.findViewById(R.id.user_sex);
            viewHolder.tv_user_birth = convertView.findViewById(R.id.user_birth);
            viewHolder.tv_user_mobile = convertView.findViewById(R.id.user_mobile);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_user_id.setText(user.getId());
        viewHolder.tv_user_sex.setText(user.getSex());
        viewHolder.tv_user_birth.setText(user.getBirth().toString());
        viewHolder.tv_user_mobile.setText(user.getMobile());

        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_user_id;
        private TextView tv_user_sex;
        private TextView tv_user_birth;
        private TextView tv_user_mobile;
    }
}
