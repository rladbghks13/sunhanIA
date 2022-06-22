package com.example.sunhania;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunhania.backup.BackupRequest;
import com.example.sunhania.todo.TodoRequest;
import com.example.sunhania.todo.TodoTerminal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Fragment_todo extends Fragment {
    private View view;

    //리사이클러뷰 선언
    private RecyclerView TodoRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<TodoItem> todoItems;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo, container, false);

        TodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recyclerView);
        TodoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        TodoRecyclerView.setLayoutManager(layoutManager);
        todoItems = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                todoItems.clear();
                for(DataSnapshot snapshot :datasnapshot.getChildren()){
                    TodoItem todoItem = snapshot.getValue(TodoItem.class);
                    todoItems.add(todoItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", String.valueOf(error.toException()));
            }
        });
        adapter = new TodoRecyclerAdapter(todoItems,getContext());
        TodoRecyclerView.setAdapter(adapter);


        return view;
    }


}
