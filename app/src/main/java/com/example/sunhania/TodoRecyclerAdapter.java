package com.example.sunhania;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.CustomViewHolder> {
    private ArrayList<TodoItem> mTodolist;
    private Context context;

    public TodoRecyclerAdapter(ArrayList<TodoItem> mTodolist, Context context) {
        this.mTodolist = mTodolist;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.todo_title.setText(mTodolist.get(position).getTitle());
        holder.todo_content.setText(mTodolist.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (mTodolist !=null ? mTodolist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView todo_title;
        TextView todo_content;


    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        this.todo_content = itemView.findViewById(R.id.todo_content);
        this.todo_title = itemView.findViewById(R.id.todo_title);

    }
}
}