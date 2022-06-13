package com.example.sunhania;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sunhania.todo.TodoDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
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
        HashMap result = new HashMap<>();
        result.put("name","강감찬");
        result.put("age","25");
        result.put("date","202205192157");
        List<String> list = new ArrayList<String>();
        list.add("202201010101");
        list.add("202201010102");
        list.add("202201010103");


        databaseReference.child("users").child("info").setValue(result);
        databaseReference.child("users").child("date").setValue(list);

        ArrayList list1 = new ArrayList<>();
        list1.add(1);
        list1.add(false);
        list1.add("ㅎㅇ");
        Log.i("test", String.valueOf(list1));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String msg = dataSnapshot.getValue().toString();
                    Log.i("firebase",msg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


    private void initDatabase(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

}
