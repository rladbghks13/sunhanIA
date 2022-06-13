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
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_inventory extends Fragment {
    private View view;

    private String TAG = "프래그먼트";
    private DatabaseReference databaseReference;


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


        databaseReference.child("users").child("4").child("info").setValue(result);
        databaseReference.child("users").child("4").child("date").setValue(list);

        ArrayList list1 = new ArrayList<>();
        list1.add(1);
        list1.add(false);
        list1.add("ㅎㅇ");
        Log.i("test", String.valueOf(list1));



        return view;
    }

}
