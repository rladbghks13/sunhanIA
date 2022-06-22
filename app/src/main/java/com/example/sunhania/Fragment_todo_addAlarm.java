package com.example.sunhania;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sunhania.todo.TodoDAO;
import com.example.sunhania.todo.TodoTerminal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Fragment_todo_addAlarm extends Fragment {
    private View view;
    Fragment fragment_todo;
    ImageButton button_addAlarm, button_addAlarmExit;
    EditText add_title, add_content;
    TextView add_startdate, add_starttime, add_enddate, add_endtime, add_repeat, add_alarm;
    CheckBox checkBox_allday;
    protected int year, month, day, hour, minute;
    Calendar today;
    private int[] startdate, enddate;
    TodoDAO todoDAO = new TodoDAO();
    TodoTerminal todoTerminal = new TodoTerminal();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onPause() {
        super.onPause();

        //프래그먼트 로드 시 전부 빈칸으로
        checkBox_allday.setChecked(false);
        add_title.setText(null);
        add_content.setText(null);
        Log.i("test", "onPause");
    }


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
                read();
                if (add_title.length() == 0) { //일정 제목에 아무것도 없으면 "제목 없음"으로 등록
                    todoTerminal.setTodoTitle("제목 없음");
                } else {
                    todoTerminal.setTodoTitle(add_title.getText().toString());
                }

                if (add_content.length() == 0) {
                    todoTerminal.setTodoContent("내용 없음");
                } else {
                    todoTerminal.setTodoContent(add_content.getText().toString());
                }

                if (!checkBox_allday.isChecked()) {
                    if (add_startdate.length() == 0 || add_starttime.length() == 0 || add_enddate.length() == 0 || add_endtime.length() == 0) {
                        Toast.makeText(getContext(), "날짜, 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        if ((clacTime(startdate, enddate) == true || add_startdate.length() != 0)) {
                            todoTerminal.setTodoStartDate(String.valueOf(startdate[0]) + String.valueOf(startdate[1]) + String.valueOf(startdate[2]) + String.valueOf(startdate[3]) + String.valueOf(startdate[4]));
                            todoTerminal.setTodoEndDate(String.valueOf(enddate[0]) + String.valueOf(enddate[1]) + String.valueOf(enddate[2]) + String.valueOf(enddate[3]) + String.valueOf(enddate[4]));
                            Toast.makeText(getContext(), "등록완료", Toast.LENGTH_SHORT).show();
                            writeSchedule();
                        }
                    }
                }
                if (checkBox_allday.isChecked()) {
                    if (add_startdate.getText().length() == 0) {
                        Toast.makeText(getContext(), "시작날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        todoTerminal.setTodoStartDate(String.valueOf(startdate[0]) + String.valueOf(startdate[1]) + String.valueOf(startdate[2]) + 0 + 0);
                        todoTerminal.setTodoEndDate(String.valueOf(startdate[0]) + String.valueOf(startdate[1]) + String.valueOf(startdate[2]) + 23 + 59);
                        enddate[0]= startdate[0]; enddate[1] = startdate[1]; enddate[2] = startdate[2]; enddate[3] = 23; enddate[4] = 59;
                        Toast.makeText(getContext(), "등록완료", Toast.LENGTH_SHORT).show();
                        writeSchedule();
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
                TimePickerDialog start_timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar, start_timeSetListener, hour, 00, false);
                start_timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                start_timePickerDialog.show();
            }
        }); //시작시간 선택 텍스트뷰
        add_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeNow();
                TimePickerDialog end_timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar, end_timeSetListener, hour, 00, false);
                end_timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                end_timePickerDialog.show();
            }
        }); //종료시간 선택 텍스트뷰
        add_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_startdate.getText().length() == 0) {
                    Toast.makeText(getContext(), "시작날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    final String[] items = {"없음", "매일", "매주", "매월", "매년"};
                    final String[] items1 = {"1일마다", "2일마다", "3일마다", "4일마다", "5일마다", "6일마다"};
                    final String[] items2 = {"1주마다", "2주마다", "3주마다", "4주마다"};
                    final String[] items3 = {"월초", "월말", String.valueOf(startdate[2])};

                    AlertDialog.Builder rDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    rDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    add_repeat.setText("없음");
                                    break;
                                case 1:
                                    rDialog.setItems(items1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            add_repeat.setText(items[i] + ", " + items1[which] + "반복");
                                            todoTerminal.setTodoRepeat1(items[i]);
                                            todoTerminal.setTodoRepeat2(which + 1);
                                            Log.i("test", items[i] + "  " + String.valueOf(which));

                                        }
                                    }).show();
                                    break;
                                case 2:
                                    rDialog.setItems(items2, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            add_repeat.setText(items[i] + ", " + items2[which] + "반복");
                                            todoTerminal.setTodoRepeat1(items[i]);
                                            todoTerminal.setTodoRepeat2(which + 1);
                                            Log.i("test", items[i] + "  " + String.valueOf(which));
                                        }
                                    }).show();
                                    break;
                                case 3:
                                    rDialog.setItems(items3, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (items3[which].equals("월초") || items3[which].equals("월말")) {
                                                add_repeat.setText(items[i] + ", " + items3[which] + " 마다 반복"); // 매월 "시작날짜" 반복
                                                todoTerminal.setTodoRepeat1(items[i]);
                                                todoTerminal.setTodoRepeat2(which + 1);
                                            } else {
                                                add_repeat.setText(items[i] + ", " + items3[which] + "일 마다 반복");
                                                todoTerminal.setTodoRepeat1(items[i]);
                                                todoTerminal.setTodoRepeat2(Integer.parseInt(items3[which]));
                                            }
                                        }

                                    }).show();

                                    break;
                                case 4:
                                    Log.i("test", items[i]);
                                    add_repeat.setText(items[i] + " 반복");
                                    todoTerminal.setTodoRepeat1(items[i]);
                                    todoTerminal.setTodoRepeat2(0);
                                    break;
                            }
                        }
                    }).show();
                }
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
                        add_alarm.setText(items[i]);
                        todoTerminal.setTodoNotification(items[i]);
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
            startdate[1] = month - 1;
            startdate[2] = day;
        }
    };
    private DatePickerDialog.OnDateSetListener end_dateSetlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.i("date", year + " " + month + " " + day);
            add_enddate.setText(year + "년 " + month + "월 " + day + "일");
            enddate[0] = year;
            enddate[1] = month - 1;
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
            Log.i("date", hourOfDay + " " + minute);
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
            Log.i("date", hourOfDay + " " + minute);

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
        long today = start.getTimeInMillis();
        long dday = end.getTimeInMillis();

        long count = (today - dday);
        if (count >= 0) {
            return true;
        } else {
            return false;
        }
    }


    public void read() {
        databaseReference.child("schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("read", String.valueOf(dataSnapshot.child("content")));
                    Log.i("read", String.valueOf(dataSnapshot.child("startdate")));
                    Log.i("read", String.valueOf(dataSnapshot.child("title")));
                    Log.i("read", String.valueOf(dataSnapshot.child("key")));


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void writeSchedule() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar startday = Calendar.getInstance();
        Calendar endday = Calendar.getInstance();
        ArrayList startDateList = new ArrayList(); // 반복된 날짜를 함수에서 받아올 리스트 생성
        ArrayList endDateList = new ArrayList();
        startday.set(startdate[0], startdate[1], startdate[2], startdate[3], startdate[4]);//날자 세팅
        endday.set(enddate[0], enddate[1], enddate[2], enddate[3], enddate[4]);

        long setStartDay = startday.getTime().getTime(); //TimeStamp형식으로 DB에 저장하기위해 변환
        long setEndDay = endday.getTime().getTime();
        Log.i("test",simpleDateFormat.format(setStartDay));
        Log.i("test",simpleDateFormat.format(setEndDay));


        String postKey = databaseReference.child("schedule").push().getKey(); //파이어베이스에 고유 키 생성
        HashMap hashMap = new HashMap<>();
        hashMap.put("title", todoTerminal.getTodoTitle());
        hashMap.put("content", todoTerminal.getTodoContent());
        hashMap.put("startdate", setStartDay);
        hashMap.put("enddate", setEndDay);
        hashMap.put("postKey", postKey);
        hashMap.put("key", postKey);
        databaseReference.child("schedule").child(postKey).setValue(hashMap);


        if (add_repeat.getText() == "" || add_repeat.getText() == "없음") {
            Log.i("test", "true");
        } else {
            switch (todoTerminal.getTodoRepeat1()) {
                case "매일":
                    startDateList = todoDAO.testTime(startdate, todoTerminal.getTodoRepeat2(), "매일");
                    endDateList = todoDAO.testTime(enddate, todoTerminal.getTodoRepeat2(), "매일");
                    break;
                case "매주":
                    startDateList = todoDAO.testTime(startdate, todoTerminal.getTodoRepeat2(), "매주");
                    endDateList = todoDAO.testTime(enddate, todoTerminal.getTodoRepeat2(), "매주");
                    Log.i("매일", String.valueOf(startDateList));
                    break;
                case "매월":
                    startDateList = todoDAO.testTime(startdate, todoTerminal.getTodoRepeat2(), "매월");
                    endDateList = todoDAO.testTime(enddate, todoTerminal.getTodoRepeat2(), "매월");
                    Log.i("매일", String.valueOf(startDateList));
                    break;
                case "매년":
                    startDateList = todoDAO.testTime(startdate, todoTerminal.getTodoRepeat2(), "매년");
                    endDateList = todoDAO.testTime(enddate, todoTerminal.getTodoRepeat2(), "매년");
                    Log.i("매일", String.valueOf(startDateList));
                    break;
            }
            Log.i("test dataList size", String.valueOf(startDateList.size()));
            Log.i("test dataList", String.valueOf(startDateList));
            for (int i = 0; i < startDateList.size(); i++) {

                String key = databaseReference.child("schedule").push().getKey();
                hashMap.put("title", todoTerminal.getTodoTitle());
                hashMap.put("content", todoTerminal.getTodoContent());
                hashMap.put("startdate", startDateList.get(i));
                hashMap.put("enddate", endDateList.get(i));
                hashMap.put("postKey", postKey);
                hashMap.put("key", key);
                databaseReference.child("schedule").child(key).setValue(hashMap);
            }
        }


    }


}
