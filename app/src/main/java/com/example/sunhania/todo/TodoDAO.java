package com.example.sunhania.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
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
    FirebaseDatabase firebaseDatabase;
    TodoTerminal todoTerminal = new TodoTerminal();


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


    public int[] setTimeNow(){
        Calendar today;
        int[] dateArray;
        int year, month, day, hour, minute;
        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);
        hour = today.get(Calendar.HOUR_OF_DAY);
        minute =today.get(Calendar.MINUTE);
        dateArray = new int[]{year, month, day, hour, minute};
        return dateArray;
    }

}
