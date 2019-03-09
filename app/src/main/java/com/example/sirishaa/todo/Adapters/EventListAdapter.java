package com.example.sirishaa.todo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sirishaa.todo.R;
import com.example.sirishaa.todo.Task;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter {

    ArrayList<Task> todoList;

    public EventListAdapter() {
        this.todoList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
        viewHolder.position = position;
        Task task = todoList.get(position);
        ((SimpleItemViewHolder) holder).title.setText(task.getTaskName());
    }


    public void forceNotify(ArrayList<Task> todoList) {
        this.todoList.clear();
        this.todoList.addAll(todoList);
        notifyDataSetChanged();
    }

    public final class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int position;
        TextView title;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
