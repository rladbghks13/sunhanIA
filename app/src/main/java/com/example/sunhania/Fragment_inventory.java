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
import com.example.sunhania.todo.TodoItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

//        upload();
//        read();
//        key();
//        date();
//        countdday();
        test();
        return view;
    }

    public void read() {
        databaseReference.child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TodoItem todoItem = snapshot.getValue(TodoItem.class);


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
        hashMap.put("title", "제목2");
        hashMap.put("content", "내용14523");
        hashMap.put("startdate", "20220101");
        hashMap.put("enddate", enddate);
        hashMap.put("postkey", postKey);
        hashMap.put("key", key);
        databaseReference.child("schedule").child(postKey).child(key).setValue(hashMap);
        String[] arr = {"20220201", "20220301", "20220401", "20220501"};
        // TODO: 2022-06-20 고유키를 이용하여 테이블 재설계하기...  (timestamp 이용) 



    }

    public void date(){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.set(2022,06,22,8,21);
        calendar1.set(2022,06,22,8,22);
        Date toTimeStamp =new Date();
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm"); //1분 60000   1일 86400000     1개월 2592000000 1719014437840
        Log.i("test", dateFormat.format(timestamp));
        long time1 = calendar.getTimeInMillis();
        long time2 = calendar1.getTimeInMillis();
        long time3 = calendar1.getTime().getTime();
        long result = time2-time1;
        Log.i("test", String.valueOf(time1));
        Log.i("test", String.valueOf(time2));
        Log.i("test", String.valueOf(time3));
        Log.i("test", String.valueOf(result));
        Log.i("test",dateFormat.format(time1));
        Log.i("test",dateFormat.format(result));
    }

    public void countdday() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Calendar todaCal = Calendar.getInstance();
            todaCal.setTime(new Date());
            Calendar ddayCal = Calendar.getInstance();

            ddayCal.set(2022, 05, 24, 13, 00);
            Log.i("test", simpleDateFormat.format(todaCal.getTime()) + "");
            Log.i("test", simpleDateFormat.format(ddayCal.getTime()) + "");

            Calendar month = Calendar.getInstance();
            month.add(Calendar.MONTH,1);
            Log.i("test", String.valueOf(todaCal.getTimeInMillis())+" todacal gettime mil");
            Log.i("test", String.valueOf(month.getTimeInMillis())+" month gettime mil");
            Log.i("test",simpleDateFormat.format(todaCal.getTime())+ " now time");
            Log.i("test",simpleDateFormat.format(month.getTime())+" after one month");

            long diffSec = (todaCal.getTimeInMillis()-ddayCal.getTimeInMillis()) /1000; //초 차이
            long diffDays = diffSec / (24*60*60); //일자수 차이
            long diffTime = diffSec / (60*60); //시간 차이
            Log.i("test", String.valueOf(diffSec) + " 초 차이");
            Log.i("test", String.valueOf(diffDays) + " 일 차이");
            Log.i("test", String.valueOf(diffTime) + " 시간 차이");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Clenadartest(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar calendar = Calendar.getInstance();
        long time = 1653145246015L;
        Log.i("test",simpleDateFormat.format(time));
    }
    public void test(){
        if (1656428402280L <= 1656428417856L && (1656428402280L+86390000) > 1656428417856L){
            Log.i("test", "correct");
        }

    }
}
