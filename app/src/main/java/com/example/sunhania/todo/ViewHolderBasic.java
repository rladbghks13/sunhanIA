package com.example.sunhania.todo;

import android.animation.ValueAnimator;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sunhania.R;

public class ViewHolderBasic extends MyItemView{
    TextView todo_title;
    TextView todo_content;
    TextView todo_date;

    TodoItem todoItem;

    public ViewHolderBasic(@NonNull View itemView){
        super(itemView);

        todo_title = itemView.findViewById(R.id.todo_title);
        todo_content = itemView.findViewById(R.id.todo_content);
        todo_date = itemView.findViewById(R.id.todo_date);


//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(itemView.getContext(), v);
//                popup.getMenuInflater().inflate(R.menu.todo_item_list, popup.getMenu());
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_delete:
//
//                                Toast.makeText(itemView.getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
//                                break;
//                            default:
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popup.show();
//            }
//        });
    }
    public void onBind(MyItem todoItem){
        this.todoItem = (TodoItem) todoItem;
        todo_title.setText(this.todoItem.getTitle());
        todo_content.setText(this.todoItem.getContent());
        todo_date.setText(this.todoItem.getDate());

    }
}
