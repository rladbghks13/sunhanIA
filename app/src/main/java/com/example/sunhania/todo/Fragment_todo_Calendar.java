package com.example.sunhania.todo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunhania.MainActivity;
import com.example.sunhania.R;
import com.example.sunhania.metarialCalendar.EventDecorator;
import com.example.sunhania.metarialCalendar.SaturdayDecorator;
import com.example.sunhania.metarialCalendar.SundayDecorator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Fragment_todo_Calendar extends Fragment {
    private View view;
    private MaterialCalendarView materialCalendarView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private RecyclerView TodoRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TodoItem> todoItems;
    private RecyclerView.Adapter adapter;
    private TodoDAO todoDAO;
    private Fragment_todo fragment_todo;


    private String TAG = "todo_fragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "todolist");
        view = inflater.inflate(R.layout.fragment_todo_calendar, container, false);

        BackPressEvent();
        fragment_todo = new Fragment_todo();

        database = FirebaseDatabase.getInstance();// 데이터베이스 기본 선언
        databaseReference = database.getReference("schedule");


        materialCalendarView = view.findViewById(R.id.calendarView); //달력 캘린더뷰 선언\
        slidingUpPanelLayout = view.findViewById(R.id.calendar_frame);

        setCalendar();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() { //달력 날짜 클릭이벤트
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(date.getYear(), date.getMonth(), date.getDay(),0,0);
                Log.i("test", "선택된날짜 " + simpleDateFormat.format(calendar.getTimeInMillis())+ " " + calendar.getTimeInMillis());
                long selectedDate = calendar.getTimeInMillis();
                addRecycler(selectedDate);
            }
        });
        initDatabase();
        setDot();


        return view;
    }

    private void initDatabase() {
        Calendar todayCal = Calendar.getInstance(); //오늘 날짜 Calendar 형식
        todayCal.setTime(new Date());
        Calendar beforeCal = Calendar.getInstance(); //한달 뒤 Calendar 형식
        beforeCal.add(Calendar.MONTH, 1);

        TodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_Calendar_recyclerView); //여기서부터 리사이클러뷰 선언
        TodoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        TodoRecyclerView.setLayoutManager(layoutManager);
        todoItems = new ArrayList<>();


    }


    private void setCalendar() {
        //첫 시작 요일이 월요일
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );
        materialCalendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        materialCalendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

    }

    private void setDot() {
        Query query = databaseReference.orderByChild("startdate");
        ArrayList<String> result = new ArrayList<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy,MM,dd");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    long time = (long) dataSnapshot.child("startdate").getValue();
                    Log.i("test",simpleDateFormat.format(time));
                    result.add(simpleDateFormat.format(time));
                }

                new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "데이터베이스 로드 오류", Toast.LENGTH_SHORT).show();
                Log.e("error", String.valueOf(error));
            }
        });
    }

    private void addRecycler(long selectedDate) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
        Query startdateQuery = databaseReference.orderByChild("startdate"); //startdate를 기준으로 내림차순 정렬하는 쿼리
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        startdateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                todoItems.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    TodoItem todoItem = snapshot.getValue(TodoItem.class);
                    if (selectedDate <= todoItem.getStartdate() && (selectedDate+86340000) > todoItem.getStartdate() ) { //달력 날자에는 그 당일 사이에 있는 일정만 RecyclerView에 담아주는 로직
                        todoItems.add(todoItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", String.valueOf(error.toException()));
            }
        });
        adapter = new TodoRecyclerAdapter(todoItems, getContext(), TodoItemCase.CALENDAR);

        TodoRecyclerView.setAdapter(adapter);
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList Time_Result;

        ApiSimulator(ArrayList Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();


            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.size(); i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result.get(i).toString().split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, getActivity()));
        }
    }

    private void BackPressEvent() { //뒤로가기 버튼 이벤트
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.manager.beginTransaction().replace(R.id.main_layout, fragment_todo).commitAllowingStateLoss();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


}
