package com.example.sunhania.todo;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

public class TodoDAO {

    DatabaseReference databaseReference;
    TodoTerminal todoTerminal = new TodoTerminal();



    //시간 계산하는 메서드
    public int countdday(int myear, int mmonth, int mday, int mHour, int mMinute) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Calendar todaCal = Calendar.getInstance();
            Calendar ddayCal = Calendar.getInstance();

            mmonth -= 1;
            ddayCal.set(myear, mmonth, mday, mHour, mMinute);
            Log.i("test", simpleDateFormat.format(todaCal.getTime()) + "");
            Log.i("test", simpleDateFormat.format(ddayCal.getTime()) + "");

            long today = todaCal.getTimeInMillis();
            long dday = ddayCal.getTimeInMillis();

            Log.i("test", String.valueOf(today));
            Log.i("test", String.valueOf(dday));
            long count = (dday - today);

            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int[] nowDate(){
        int[] DateTime = new int[5];
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String current = simpleDateFormat.format(currentTime);

        String year = current.substring(0,4);
        String month = current.substring(4,6);
        String day = current.substring(6,8);
        String hour = current.substring(8,10);
        String minute = current.substring(10,12);

        int myear = Integer.parseInt(year);
        int mmonth = Integer.parseInt(month);
        int mday = Integer.parseInt(day);
        int mhour = Integer.parseInt(hour);
        int mminute = Integer.parseInt(minute);

        DateTime[0] = myear;
        DateTime[1] = mmonth;
        DateTime[2] = mday;
        DateTime[3] = mhour;
        DateTime[4] = mminute;

        Log.i("test", String.valueOf(DateTime));
        return DateTime;
    }
    public void test(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR,1);
        today.add(Calendar.MONTH,-3);
        Log.i("test",simpleDateFormat.format(today.getTime()));
    }
    public void uploadTest(){
        Log.i("test",todoTerminal.getTodoTitle());
        getTodolistLength();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap hashMap = new HashMap<>();
        hashMap.put("title",todoTerminal.getTodoTitle());
        hashMap.put("content",todoTerminal.getTodoContent());
        hashMap.put("startdate",todoTerminal.getTodoStartDate());
        List<String> list = new ArrayList<String>();
        list.add("202201010101");
        list.add("202201010102");
        list.add("202201010103");
        Log.i("test", String.valueOf(hashMap));

        int TodoNumber = todoTerminal.getTodoListLength();
        TodoNumber = TodoNumber + 1;
        databaseReference.child("schedule").child(String.valueOf(TodoNumber)).child("info").setValue(hashMap);
        databaseReference.child("schedule").child(String.valueOf(TodoNumber)).child("date").setValue(list);
    }
    public void getTodolistLength() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int todolistIDlegnth = Integer.parseInt(dataSnapshot.getKey());
                    TodoTerminal todoTerminal = new TodoTerminal();
                    todoTerminal.setTodoListLength(todolistIDlegnth);
                    Log.i("firebase", String.valueOf(todoTerminal.getTodoListLength()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void testtest() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.i("firebase",databaseReference.child("schedule").getKey());
    }
}
