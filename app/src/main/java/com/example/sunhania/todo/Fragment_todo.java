package com.example.sunhania.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunhania.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_todo extends Fragment {
    private View view;

    //리사이클러뷰 선언
    private RecyclerView TodoRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<TodoItem> todoItems;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private int[] nowDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        TodoDAO todoDAO = new TodoDAO();
        nowDate = todoDAO.setTimeNow();
        Calendar todayCal = Calendar.getInstance(); //오늘 날짜 Calendar 형식
        todayCal.set(nowDate[0], nowDate[1], nowDate[2], 0, 0);
        Calendar beforeCal = Calendar.getInstance(); //한달 뒤 Calendar 형식
        beforeCal.add(Calendar.MONTH, 1);

        TodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recyclerView); //여기서부터 리사이클러뷰 선언
        TodoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        TodoRecyclerView.setLayoutManager(layoutManager);
        todoItems = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        Query startdateQuery = databaseReference.orderByChild("startdate"); //startdate를 기준으로 내림차순 정렬하는 쿼리
        startdateQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                todoItems.clear();
                Log.i("test","데이터 변경됨");
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    TodoItem todoItem = snapshot.getValue(TodoItem.class);
                    todoItem.setDate(convertTime(todoItem.getStartdate())); //파이어베이스 안에 int형식의 startdate를 Simpledateformat 형식으로 바꿔서 올림

                    if (todoItem.getStartdate() >= todayCal.getTimeInMillis() && todoItem.getStartdate() <= beforeCal.getTimeInMillis()) { //오늘 일자로부터 1개월안에 일정만 출력
                        todoItems.add(todoItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", String.valueOf(error.toException()));
            }
        });
        adapter = new TodoRecyclerAdapter(todoItems, getContext(), TodoItemCase.BASIC);

        TodoRecyclerView.setAdapter(adapter);

        return view;
    }

    public String convertTime(long startDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(startDate);
        return date;
    }
}
