package com.example.sunhania.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunhania.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<MyItemView> {
    private ArrayList<TodoItem> mTodolist;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TodoItemCase type;
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public TodoRecyclerAdapter(ArrayList<TodoItem> mTodolist, Context context, TodoItemCase type) {
        this.mTodolist = mTodolist;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (type == TodoItemCase.BASIC) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolderBasic(view);
        } else if (type == TodoItemCase.CALENDAR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item_list, parent, false);
            return new ViewHolderCalendar(view);
        }
        return null;
//        CustomViewHolder holder = new CustomViewHolder(view);
//        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        if (holder instanceof ViewHolderBasic) {
            ViewHolderBasic viewHolderBasic = (ViewHolderBasic) holder;
            viewHolderBasic.onBind(mTodolist.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() { // 아이템 클릭시 날자 반환해줌
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.todo_item_list, popup.getMenu());
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_delete:
                                        overlap(mTodolist.get(pos).getPostKey(), mTodolist.get(pos).getKey());
                                        break;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }
                }
            });

        } else if (holder instanceof ViewHolderCalendar) {
            ViewHolderCalendar viewHolderCalendar = (ViewHolderCalendar) holder;
            viewHolderCalendar.onBind(mTodolist.get(position));
        }

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return (mTodolist != null ? mTodolist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView todo_title;
        TextView todo_content;
        TextView todo_date;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.todo_content = itemView.findViewById(R.id.todo_content);
            this.todo_title = itemView.findViewById(R.id.todo_title);
            this.todo_date = itemView.findViewById(R.id.todo_date);
        }
    }


    public void overlap(String postKey, String key) { //삭제할때 반복일정인지 아닌지 검사 후 삭제하는 로직
        ArrayList keyArray = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TodoItem todoItem = dataSnapshot.getValue(TodoItem.class);
                    if (todoItem.getPostKey().equals(postKey)) {
                        keyArray.add(todoItem.getKey()); //반복일정의 key값을 array에 담아주기
                    }
                }

                if (keyArray.size() >= 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제").setMessage("동일한 반복일정이 있습니다. 같이 삭제하시겠습니까 ?");

                    builder.setPositiveButton("이 일정만 삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSchedule(key);
                        }
                    });
                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRepeatSchedule(keyArray);
                        }
                    });
                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    deleteSchedule(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void deleteRepeatSchedule(ArrayList key) { //반복일정을 같이 삭제하는 메소드
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        for (int i = 0; i < key.size(); i++) { //같은 반복일정의 고유키 만큼 반복실행
            databaseReference.child(String.valueOf(key.get(i))).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }
        Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();
    }

    private void deleteSchedule(String key) { //일정 삭제 메소드
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        databaseReference.child(String.valueOf(key)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}