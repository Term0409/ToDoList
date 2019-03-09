package com.example.sirishaa.todo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sirishaa.todo.CategoryListActivity;
import com.example.sirishaa.todo.R;

import java.util.ArrayList;

public class CategoriesGridAdapter extends BaseAdapter {

    Context mContext;
    Typeface heavy_tf, medium_tf;
    ArrayList<String> categoryList;

    public CategoriesGridAdapter(Context context, ArrayList<String> categoryList) {
        this.mContext = context;
        this.categoryList = new ArrayList<>();
        this.categoryList.addAll(categoryList);
        heavy_tf = Typeface.createFromAsset(context.getAssets(), "fonts/avenir_heavy.ttf");
        medium_tf = Typeface.createFromAsset(context.getAssets(), "fonts/avenir_medium.ttf");
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.category_grid_item, parent, false);
            viewHolder = new ViewHolderItem(convertView);

            convertView.setTag(viewHolder);


        } else {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.category_grid_item, parent, false);
            viewHolder = new ViewHolderItem(convertView);
        }

        viewHolder.catCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryListIntent = new Intent(mContext, CategoryListActivity.class);
                categoryListIntent.putExtra("Category", viewHolder.categoryTv.getText() + "");
                mContext.startActivity(categoryListIntent);

            }
        });


        viewHolder.categoryTv.setText(categoryList.get(position));
        //viewHolder.tasksTv.setText("tasks");

        viewHolder.categoryTv.setTypeface(heavy_tf);
        //viewHolder.numTasksTv.setTypeface(medium_tf);
        //viewHolder.tasksTv.setTypeface(medium_tf);

        return convertView;
    }


    static class ViewHolderItem {
        ImageView iconIv;
        TextView categoryTv; //numTasksTv, tasksTv;
        CardView catCv;

        ViewHolderItem(View view) {
            iconIv = view.findViewById(R.id.icon_iv);
            catCv = view.findViewById(R.id.cat_cv);
            categoryTv = view.findViewById(R.id.category_tv);
            //numTasksTv = view.findViewById(R.id.num_tasks_tv);
            //tasksTv = view.findViewById(R.id.task_tv);

        }
    }
}
