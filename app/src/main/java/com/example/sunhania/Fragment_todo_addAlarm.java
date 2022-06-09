package com.example.sunhania;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sunhania.todo.TodoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Fragment_todo_addAlarm extends Fragment {
    private View view;
    Fragment fragment_todo;
    ImageButton button_addAlarm, button_addAlarmExit;
    EditText add_title, add_content;
    TextView add_startdate, add_starttime, add_enddate, add_endtime, add_repeat, add_alarm;
    CheckBox checkBox_allday;
    protected int year, month, day, hour, minute;
    Calendar today;
    private String upload_title;
    private String upload_startdate;
    private String upload_starttime;
    private String upload_enddate;
    private String upload_endtime;
    private String upload_alarmContent;
    private String upload_repeatFLAG;
    private String upload_alarmFLAG;
    private int[] startdate, enddate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo_add_alarm, container, false);

        BackPressEvent();
        fragment_todo = new Fragment_todo();

        startdate = new int[5];
        enddate = new int[5];
        button_addAlarmExit = view.findViewById(R.id.btn_addAlarm_exit);
        button_addAlarm = view.findViewById(R.id.btn_addAlarm);
        add_title = view.findViewById(R.id.edittext_alarmtitle);
        add_content = view.findViewById(R.id.edittext_alarmcontent);
        add_startdate = view.findViewById(R.id.textview_alarmStartDate);
        add_starttime = view.findViewById(R.id.textview_alarmStartTime);
        add_enddate = view.findViewById(R.id.textview_alarmEndDate);
        add_endtime = view.findViewById(R.id.textview_alarmEndTime);
        add_repeat = view.findViewById(R.id.textView_alarmrepeat);
        add_alarm = view.findViewById(R.id.textView_alarmBell);
        checkBox_allday = view.findViewById(R.id.checkbox_allday); //선언부

        checkBox_allday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //하루종일 체크박스 선택했을시 종료시간 빈칸, 입력불가능하게 바꿈
                if (checkBox_allday.isChecked()) {
                    add_starttime.setHint("하루종일");
                    add_starttime.setEnabled(false);
                    add_starttime.setText("");
                    add_enddate.setHint("");
                    add_enddate.setText("");
                    add_endtime.setHint("");
                    add_endtime.setText("");
                } else if (!checkBox_allday.isChecked()) {
                    add_starttime.setEnabled(true);
                    add_starttime.setHint("터치하여 선택");
                    add_enddate.setHint("터치하여 선택");
                    add_endtime.setHint("터치하여 선택");
                }
            }
        });


        /**
         * Datepicker, Timepicker selectListener
         */
        button_addAlarm.setOnClickListener(new View.OnClickListener() { //우측 상단 체크모양 눌러서 등록
            @Override
            public void onClick(View view) {
                if (add_title.length() == 0) { //일정 제목에 아무것도 없으면 "제목 없음"으로 등록
                    upload_title = "제목 없음";
                } else {
                    upload_title = String.valueOf(add_title.getText());
                }

                if (add_content.length() == 0) {
                    upload_alarmContent = "내용 없음";
                } else {
                    upload_alarmContent = String.valueOf(add_content.getText());
                }

                if (!checkBox_allday.isChecked()) {
                    if (add_startdate.length() == 0 || add_starttime.length() == 0 || add_enddate.length() == 0 || add_endtime.length() == 0) {
                        Toast.makeText(getContext(), "날짜, 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        if ((clacTime(startdate, enddate) == true)){

                        }

                    }
                }

            }
        });
        add_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeNow();
                DatePickerDialog start_datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, start_dateSetlistener, year, month + 1, day);
                start_datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                start_datePickerDialog.show();
            }
        }); //시작일자 선택 텍스트뷰
        add_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeNow();
                DatePickerDialog end_datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, end_dateSetlistener, year, month + 1, day);
                end_datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                end_datePickerDialog.show();
            }
        }); //종료일자 선택 텍스트뷰
        add_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeNow();
                TimePickerDialog start_timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar, start_timeSetListener, hour, minute, false);
                start_timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                start_timePickerDialog.show();
            }
        }); //시작시간 선택 텍스트뷰
        add_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeNow();
                TimePickerDialog end_timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar, end_timeSetListener, hour, minute, false);
                end_timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                end_timePickerDialog.show();
            }
        }); //종료시간 선택 텍스트뷰
        add_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"없음", "매일", "매주", "매월", "매년"};
                final String[] items1 = {"1일마다", "2일마다", "3일마다", "4일마다", "5일마다"};
                AlertDialog.Builder rDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                rDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                add_repeat.setHint("없음");
                                break;
                            case 1:
                                rDialog.setItems(items1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        upload_repeatFLAG = items1[i];
                                        Log.i("test", items1[i]);
                                    }
                                }).show();
                                break;
                            case 2:

                                Log.i("test", items[2]);
                                break;
                            case 3:
                                Log.i("test", items[3]);
                                break;
                            case 4:
                                Log.i("test", items[4]);
                                break;

                        }
                    }
                }).show();
            }
        });
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"없음", "정시", "5분 전", "10분 전", "15분전", "30분 전", "1시간 전", "2시간 전", "3시간 전", "12시간 전", "1일 전", "2일 전"};
                AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                aDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                add_alarm.setHint("없음");
                        }
                    }
                }).show();
            }
        });
        return view;


    }

    private DatePickerDialog.OnDateSetListener start_dateSetlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.i("date", year + " " + month + " " + day);
            add_startdate.setText(year + "년 " + month + "월 " + day + "일");
            startdate[0] = year;
            startdate[1] = month;
            startdate[2] = day;
        }
    };
    private DatePickerDialog.OnDateSetListener end_dateSetlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.i("date", year + " " + month + " " + day);
            add_enddate.setText(year + "년 " + month + "월 " + day + "일");
            enddate[0] = year;
            enddate[1] = month;
            enddate[2] = day;
        }
    };

    private TimePickerDialog.OnTimeSetListener start_timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String mhour, mminute, ampm;
            startdate[3] = hourOfDay;
            startdate[4] = minute;
            String[] timearray = convertTime(hourOfDay, minute);
            mhour = timearray[0];
            mminute = timearray[1];
            ampm = timearray[2];
            add_starttime.setText(ampm + " " + mhour + "시 " + mminute + "분");
        }
    };
    private TimePickerDialog.OnTimeSetListener end_timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String mhour, mminute, ampm;
            enddate[3] = hourOfDay;
            enddate[4] = minute;
            String[] timearray = convertTime(hourOfDay, minute);
            mhour = timearray[0];
            mminute = timearray[1];
            ampm = timearray[2];
            add_endtime.setText(ampm + " " + mhour + "시 " + mminute + "분");
        }
    }; //datepicker, timepicker 세팅


    //뒤로가기 버튼 이벤트
    private void BackPressEvent() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.manager.beginTransaction().replace(R.id.main_layout, fragment_todo).commitAllowingStateLoss();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setTimeNow() {  //시간을 현재시간으로 맞춤
        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);
        hour = today.get(Calendar.HOUR_OF_DAY);
        minute = today.get(Calendar.MINUTE); //현재시간 선언부
    }


    private String[] convertTime(int hourOfDay, int minute) { //시간 선택했을때 텍스트뷰에 출력하기위해 변환
        String[] converTime = new String[3];
        if (hourOfDay > 13) {
            converTime[0] = String.valueOf(hourOfDay - 12);
            converTime[2] = "오후";
        } else {
            converTime[2] = "오전";
            converTime[0] = String.valueOf(hourOfDay);
        }
        if (String.valueOf(minute).length() == 1) {
            converTime[1] = "0" + String.valueOf(minute);
        } else {
            converTime[1] = String.valueOf(minute);
        }
        return converTime;
    }

    private String test(int year, int month, int day) {  //반복일정 계산 테스트
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        today.set(year, month, day);
        today.add(Calendar.YEAR, 1);
        for (int i = 1; i <= 30; i++) {
            today.add(Calendar.DAY_OF_MONTH, 1);
            Log.i("test", simpleDateFormat.format(today.getTime()));
        }

        return null;
    }

    private boolean clacTime(int[] startdate, int[] enddate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(startdate[0], startdate[1], startdate[2], startdate[3], startdate[4]);
        end.set(enddate[0], enddate[1], enddate[2], enddate[3], enddate[4]);
        Log.i("test", simpleDateFormat.format((start.getTime())));
        Log.i("test", simpleDateFormat.format((end.getTime())));

        long today = start.getTimeInMillis();
        long dday = end.getTimeInMillis();

        long count = (today - dday);
        if (count <= 0) {
            return true;
        } else {
            return false;
        }
    }


}
