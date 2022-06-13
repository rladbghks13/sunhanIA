package com.example.sunhania.todo;

import java.util.ArrayList;

public class TodoTerminal {
    private String TodoTitle;

    public String getTodoTitle() {
        return TodoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        TodoTitle = todoTitle;
    }

    public int[] getTodoStartDate() {
        return TodoStartDate;
    }

    public void setTodoStartDate(int[] todoStartDate) {
        TodoStartDate = todoStartDate;
    }

    public int[] getTodoEndDate() {
        return TodoEndDate;
    }

    public void setTodoEndDate(int[] todoEndDate) {
        TodoEndDate = todoEndDate;
    }

    public String getTodoContent() {
        return TodoContent;
    }

    public void setTodoContent(String todoContent) {
        TodoContent = todoContent;
    }

    public String getTodoNotification() {
        return TodoNotification;
    }

    public void setTodoNotification(String todoNotification) {
        TodoNotification = todoNotification;
    }

    private int[] TodoStartDate;
    private int[] TodoEndDate;
    private String TodoContent;
    private String TodoNotification;
    private ArrayList TodoRepeat;

    public ArrayList getTodoRepeat() {
        return TodoRepeat;
    }

    public void setTodoRepeat(ArrayList todoRepeat) {
        TodoRepeat = todoRepeat;
    }

    public void setTodoRepeat(String s, String s1) {
    }

    public void setTodoStartDate(int year, int month, int day, int hour, int minute) {
    }

    public void setTodoEndDate(int year, int month, int day, int hour, int minute) {
    }
}


