package com.example.sunhania.todo;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.login.LoginException;

public class TodoDAO {



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
}
