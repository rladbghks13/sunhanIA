package com.example.sunhania.todo;

import java.util.ArrayList;

public class TodoTerminal {

    public String title;
    public String content;
    public String startdate;
    public TodoTerminal(){

    }
    public TodoTerminal(String title, String content, String startdate){
        this.title = title;
        this.content = content;
        this.startdate =startdate;
    }

    private String TodoTitle;


    public String getTodoTitle() {
        return TodoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        TodoTitle = todoTitle;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getTodoEndDate() {
        return TodoEndDate;
    }

    public void setTodoEndDate(String todoEndDate) {
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

    public String getTodoStartDate() {
        return TodoStartDate;
    }

    public void setTodoStartDate(String todoStartDate) {
        TodoStartDate = todoStartDate;
    }

    private String TodoStartDate;
    private String TodoEndDate;
    private String TodoContent;
    private String TodoNotification;
    private ArrayList TodoRepeat;
    private int TodoListLength;

    public int getTodoListLength() {
        return TodoListLength;
    }

    public void setTodoListLength(int todoListLength) {
        this.TodoListLength = todoListLength;
    }

    public ArrayList getTodoRepeat() {
        return TodoRepeat;
    }

    public void setTodoRepeat(ArrayList todoRepeat) {
        TodoRepeat = todoRepeat;
    }

    public void setTodoRepeat(String s, String s1) {
    }


}


