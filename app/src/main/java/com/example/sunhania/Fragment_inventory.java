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

import java.text.ParseException;

public class Fragment_inventory extends Fragment {
    private View view;

    private String TAG = "프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_inventory, container, false);

        TodoDAO todoDAO = new TodoDAO();
        Log.i("log", String.valueOf(MainActivity.bottomNavigationView.getSelectedItemId()));

//        Log.i("test", todoDAO.countdday(2022,06,02, 15, 35) +"");
//        todoDAO.nowDate();
        todoDAO.test();


        return view;
    }

}
