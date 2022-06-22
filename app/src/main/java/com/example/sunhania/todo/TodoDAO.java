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

import java.sql.Time;
import java.sql.Timestamp;
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

    public int[] nowDate() {
        int[] DateTime = new int[5];
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String current = simpleDateFormat.format(currentTime);

        String year = current.substring(0, 4);
        String month = current.substring(4, 6);
        String day = current.substring(6, 8);
        String hour = current.substring(8, 10);
        String minute = current.substring(10, 12);

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

    public ArrayList testTime(int[] date, int i, String flg) { //반복할때 시간 곱셈
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar today = Calendar.getInstance();
        long time;
        ArrayList dateList = new ArrayList();
        if (flg.equals("매일")) {
            for (int k = 1; k <= (365 / i); k++) { //365일을 i(일수)로 나눔
                today.add(Calendar.DAY_OF_MONTH, i);
                Log.i("매일 반복", simpleDateFormat.format(today.getTime()));
                time = (today.getTime().getTime());
                dateList.add(time);
            }
            return dateList;
        } else if (flg.equals("매주")) {
            for (int k = 1; k <= (365 / (i * 7)); k++) { //365일을 i(주)로 나눔
                today.add(Calendar.WEEK_OF_MONTH, i);
                Log.i("매주 반복", simpleDateFormat.format(today.getTime()));
                time = (today.getTime().getTime());
                dateList.add(time);
            }
            return dateList;
        } else if (flg.equals("매월")) {
            if (i == 1) { //월초마다 1년 반복하는 로직
                today.set(date[0], date[1] - 1, 1, date[3], date[4]);
                for (int k = 0; k <= 12; k++) {
                    today.add(Calendar.MONTH, +1);
                    Log.i("월초 반복", simpleDateFormat.format(today.getTime()));
                    time = (today.getTime().getTime());
                    dateList.add(time);
                }
                return dateList;
            } else if (i == 2) { //월말마다 1년으로 반복하는 로직
                for (int k = 0; k <= 12; k++) {
                    today.set(date[0], date[1], 1, date[3], date[4]);
                    today.add(Calendar.MONTH, k + 1);
                    today.add(Calendar.DAY_OF_MONTH, -1);
                    Log.i("월말 반복", simpleDateFormat.format(today.getTime()));
                    time = (today.getTime().getTime());
                    dateList.add(time);
                }
                return dateList;
            } else {
                today.set(date[0], date[1], i, date[3], date[4]);
                for (int k = 0; k <= 12; k++) {
                    today.add(Calendar.MONTH, 1);
                    Log.i("매월 지정일자 반복", simpleDateFormat.format(today.getTime()));
                    time = (today.getTime().getTime());
                    dateList.add(time);
                }
                return dateList;
            }
        } else if (flg.equals("매년")) {
            today.set(date[0], date[1], date[2], date[3], date[4]);
            for (int k = 0; k <= 3; k++) {
                today.add(Calendar.YEAR, +1);
                Log.i("연 반복", simpleDateFormat.format(today.getTime()));
                time = (today.getTime().getTime());
                dateList.add(time);
            }
            return dateList;
        }
        return null;
    }

    public void uploadTest() {
        Log.i("test", todoTerminal.getTodoTitle());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap hashMap = new HashMap<>();
        hashMap.put("title", todoTerminal.getTodoTitle());
        hashMap.put("content", todoTerminal.getTodoContent());
        hashMap.put("startdate", todoTerminal.getTodoStartDate());
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


    public void testtest() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.i("firebase", databaseReference.child("schedule").getKey());
    }
}
