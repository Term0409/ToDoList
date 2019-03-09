package com.example.sirishaa.todo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sirishaa.todo.CalendarActivity;
import com.example.sirishaa.todo.MainActivity;
import com.example.sirishaa.todo.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context mContext;
    private ArrayList<String> categoryList;

    public CategoryAdapter(ArrayList<String> categoryList, Context context) {
        this.mContext = context;
        this.categoryList = new ArrayList<>();
        this.categoryList.addAll(categoryList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.categoryB.setText(categoryList.get(listPosition));
        holder.categoryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listPosition = holder.getAdapterPosition();
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).getSelectedCategory(listPosition);

                }
                if (mContext instanceof CalendarActivity) {
                    ((CalendarActivity) mContext).getSelectedCategory(listPosition);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Button categoryB;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.categoryB = itemView.findViewById(R.id.category_item_b);
        }
    }
}

