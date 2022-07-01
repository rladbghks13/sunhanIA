package com.example.sunhania.todo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sunhania.R;

public class ViewHolderCalendar extends MyItemView{
    TextView todo_title;

    TodoItem todoItem;

    public ViewHolderCalendar(@NonNull View itemView){
        super(itemView);

        todo_title = itemView.findViewById(R.id.todo_calendar_title);
    }

    public void onBind(MyItem todoItem){
        this.todoItem = (TodoItem) todoItem;
        todo_title.setText(this.todoItem.getTitle());
    }
}
