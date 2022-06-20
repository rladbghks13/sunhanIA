package com.example.sunhania;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sunhania.todo.TodoDAO;
import com.example.sunhania.todo.TodoTerminal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Fragment_inventory extends Fragment {
    private View view;

    private String TAG = "프래그먼트";
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_inventory, container, false);

        TodoDAO todoDAO = new TodoDAO();
        Log.i("log", String.valueOf(MainActivity.bottomNavigationView.getSelectedItemId()));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        upload();
//        read();

        return view;
    }


    private void count() {
        databaseReference.child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int todolistIDlegnth = Integer.parseInt(dataSnapshot.getKey());
                    TodoTerminal todoTerminal = new TodoTerminal();
                    todoTerminal.setTodoListLength(todolistIDlegnth);
                    Log.i("firebase key", String.valueOf(todolistIDlegnth));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "데이터 추가 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void read() {
        databaseReference.child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void upload() {

        String postKey = databaseReference.child("schedule").push().getKey(); //파이어베이스에 고유 키 생성
        String key = databaseReference.child("schedule").child(postKey).push().getKey();

        String enddate = "20220101";
        HashMap hashMap = new HashMap<>();
        HashMap hashMap2 = new HashMap<>();
        hashMap.put("title", "제목2");
        hashMap.put("content", "내용14523");
        hashMap.put("startdate", "20220101");
        hashMap.put("enddate", enddate);
        hashMap.put("postkey", postKey);
        hashMap.put("key", key);
        databaseReference.child("schedule").child(postKey).child(key).setValue(hashMap);
        String[] arr = {"20220201", "20220301", "20220401", "20220501"};
        Log.i("test", String.valueOf(arr.length));
        Log.i("test", arr[1]);
        for (int i = 0; i < arr.length; i++) {
            Log.i("test i ", String.valueOf(i));
            String key1 = databaseReference.child("schedule").child(postKey).push().getKey();
            hashMap2.put("title", "제목2");
            hashMap2.put("content", "내용46341");
            hashMap2.put("startdate", arr[i]);
            hashMap2.put("enddate", enddate);
            hashMap2.put("postkey", postKey);
            hashMap2.put("key", key1);
            databaseReference.child("schedule").child(postKey).child(key1).setValue(hashMap2);

        }


    }
}
