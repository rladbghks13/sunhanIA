package com.example.sunhania;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sunhania.backup.Fragment_backup;
import com.example.sunhania.todo.Fragment_todo;
import com.example.sunhania.todo.Fragment_todo_Calendar;
import com.example.sunhania.todo.Fragment_todo_addAlarm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static BottomNavigationView bottomNavigationView;



    //프래그먼트 변수
    Fragment fragment_backup;
    Fragment fragment_todo;
    Fragment fragment_worklist;
    Fragment fragment_inventory;
    Fragment fragment_todo_calendar;
    Fragment fragment_todo_addAlarm;
    public static FragmentManager manager;


    //플로팅 버튼
    FloatingActionButton fab_menu, fab_wallpaper, fab_list, fab_addAlarm;
    ConstraintLayout alarmwrap, wallpaperwrap, checklistwrap; //레이아웃
    TextView text_addAlarm, text_wallpaper, text_checklist;
    Boolean isFabOpen = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("test","start");
        manager = getSupportFragmentManager();

        //프래그먼트 생성
        fragment_backup = new Fragment_backup();
        fragment_todo = new Fragment_todo();
        fragment_worklist = new Fragment_worklist();
        fragment_inventory = new Fragment_inventory();
        fragment_todo_calendar = new Fragment_todo_Calendar();
        fragment_todo_addAlarm = new Fragment_todo_addAlarm();


        //플로팅 버튼 선언
        fab_menu = findViewById(R.id.fab_menubtn);
        fab_wallpaper = findViewById(R.id.fab_wallpaper);
        fab_list = findViewById(R.id.fab_checklist);
        fab_addAlarm = findViewById(R.id.fab_addAlarm);

        //슬라이딩 업 패널 선언

        //플로팅 버튼 온클릭 리스너
        fab_menu.setOnClickListener(this);
        fab_wallpaper.setOnClickListener(this);
        fab_list.setOnClickListener(this);
        fab_addAlarm.setOnClickListener(this);


        //레이아웃 선언
        alarmwrap = findViewById(R.id.alarmwrap);
        wallpaperwrap = findViewById(R.id.wallpaperwrap);
        checklistwrap = findViewById(R.id.checklistwrap);

        text_addAlarm = findViewById(R.id.text_addAlarm);
        text_wallpaper = findViewById(R.id.text_wallpaper);
        text_checklist = findViewById(R.id.text_checklist);

        //초기화면엔 텍스트뷰 숨기기
        text_addAlarm.setVisibility(View.INVISIBLE);
        text_wallpaper.setVisibility(View.INVISIBLE);
        text_checklist.setVisibility(View.INVISIBLE);

        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //초기 프래그먼트 설정

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_todo).commitAllowingStateLoss();

        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        animHide();
        //리스너 등록
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todo:
                        fab_menu.setVisibility(View.VISIBLE); //버튼 보이게
                        wallpaperwrap.setVisibility(View.VISIBLE);
                        checklistwrap.setVisibility(View.VISIBLE);
                        alarmwrap.setVisibility(View.VISIBLE);
//                        addToBackStack(null).
                        manager.beginTransaction().replace(R.id.main_layout, fragment_todo).commitAllowingStateLoss();
                        return true;
                    case R.id.backup:
                        hideButton();
                        manager.beginTransaction().replace(R.id.main_layout, fragment_backup).commitAllowingStateLoss();
                        return true;
                    case R.id.work:
                        hideButton();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_worklist).commitAllowingStateLoss();
                        return true;
                    case R.id.inventory:
                        hideButton();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_inventory).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.fab_menubtn:
                anim();
                break;
            case R.id.fab_wallpaper:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_todo_calendar).commitAllowingStateLoss();
                animHide();
                isFabOpen = !isFabOpen;
                break;
            case R.id.fab_checklist:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_todo).commitAllowingStateLoss();
                animHide();
                isFabOpen = !isFabOpen;
                break;
            case R.id.fab_addAlarm:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_todo_addAlarm).commitAllowingStateLoss();
                animHide();
                isFabOpen = !isFabOpen;
                break;
        }
    }

    /**
     * 일정 +버튼 애니메이션 *
     */
    public void anim() {

        if (isFabOpen) {
            animHide();
        } else {
            animShow();
        }
        isFabOpen = !isFabOpen;
    }

    private void animHide() {
        ObjectAnimator.ofFloat(wallpaperwrap, "translationY", 0f).start();
        ObjectAnimator.ofFloat(checklistwrap, "translationY", 0f).start();
        ObjectAnimator.ofFloat(alarmwrap, "translationY", 0f).start();
        fab_list.hide();
        fab_wallpaper.hide();
        fab_addAlarm.hide();
        text_addAlarm.setVisibility(View.INVISIBLE);
        text_wallpaper.setVisibility(View.INVISIBLE);
        text_checklist.setVisibility(View.INVISIBLE);

    }

    private void animShow() {
        ObjectAnimator.ofFloat(wallpaperwrap, "translationY", -180f).start();
        ObjectAnimator.ofFloat(checklistwrap, "translationY", -340f).start();
        ObjectAnimator.ofFloat(alarmwrap, "translationY", -500f).start();
        fab_list.show();
        fab_wallpaper.show();
        fab_addAlarm.show();
        text_addAlarm.setVisibility(View.VISIBLE);
        text_wallpaper.setVisibility(View.VISIBLE);
        text_checklist.setVisibility(View.VISIBLE);


    }


    private void hideButton() {
        fab_menu.setVisibility(View.GONE);
        wallpaperwrap.setVisibility(View.GONE);
        checklistwrap.setVisibility(View.GONE);
        alarmwrap.setVisibility(View.GONE);

    }

//    public static void changeFragment(int index){
//        switch (index){
//            case FRAGMENT_
//        }
//}


}
