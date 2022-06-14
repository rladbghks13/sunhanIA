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

import java.util.ArrayList;
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




        count();


        databaseReference.child("schedule").child("1").child("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.i("firebase",dataSnapshot.getValue().toString());
                }
//                TodoTerminal todoTerminal = snapshot.getValue(TodoTerminal.class);
//                Log.i("firebase",todoTerminal.title +" "+ todoTerminal.content+" "+todoTerminal.startdate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    private void add(){
        HashMap result = new HashMap<>();
        result.put("title", "인증서갱신");
        result.put("content", "인증서갱신하기");
        result.put("startdate", "202205192157");
        List<String> list = new ArrayList<String>();
        list.add("202201010101");
        list.add("202201010102");
        list.add("202201010103");


        databaseReference.child("schedule").child("1").child("info").setValue(result);
        databaseReference.child("schedule").child("1").child("date").setValue(list);

        ArrayList list1 = new ArrayList<>();
        list1.add(1);
        list1.add(false);
        list1.add("ㅎㅇ");
        Log.i("test", String.valueOf(list1));

    }

    private void count(){
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
}
