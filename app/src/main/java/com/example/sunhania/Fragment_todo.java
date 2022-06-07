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
    private TodoRecyclerAdapter todoRecyclerAdapter;
    private ArrayList<TodoItem> todoItems;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo, container, false);

        setRecyclerView();


        return view;
    }

    // 리사이클러뷰 세팅
    private void setRecyclerView() {
        TodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recyclerView);

        todoRecyclerAdapter = new TodoRecyclerAdapter();

        TodoRecyclerView.setAdapter(todoRecyclerAdapter);
        TodoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TodoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));



        //DB에서 일정 목록을 불러오는 메소드
        TodoRequest task = new TodoRequest();

        try {
            JSONArray jsonArray;
            String result;
            result = task.execute().get();
            JSONObject jsonMain = new JSONObject(result);
            jsonArray = jsonMain.getJSONArray("todolist");

            todoItems = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("completeflag").equals("0")) { //완료 플래그가 0이면 미완료, 1이면 완료이기때문에 미완료 일정만 불러옴
                    todoItems.add(new TodoItem(jsonObject.getString("title"), jsonObject.getString("startdate")));
                }
            }
            todoRecyclerAdapter.setmTodolist(todoItems);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
