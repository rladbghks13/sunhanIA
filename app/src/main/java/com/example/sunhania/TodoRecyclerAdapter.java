package com.example.sunhania;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {
    private ArrayList<TodoItem> mTodolist;

    @NonNull
    @Override
    public TodoRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(mTodolist.get(position));
    }

    @Override
    public int getItemCount() {
        return mTodolist.size();
    }

    public void setmTodolist(ArrayList<TodoItem> list){
        this.mTodolist = list;
        notifyDataSetChanged();


    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView todo_title;
        TextView todo_time;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            todo_title = (TextView) itemView.findViewById(R.id.todo_title);
            todo_time = (TextView) itemView.findViewById(R.id.todo_time);
        }

        void onBind(TodoItem item){
            todo_title.setText(item.getTodo_title());
            todo_time.setText(item.getTodo_time());
        }
    }
}
