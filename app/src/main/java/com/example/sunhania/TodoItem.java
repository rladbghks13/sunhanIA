package com.example.sunhania;

public class TodoItem {
    String todo_title;
    String todo_time;

    public TodoItem(String todo_title, String todo_time){
        this.todo_title = todo_title;
        this.todo_time = todo_time;
    }

    public String getTodo_title() {
        return todo_title;
    }

    public void setTodo_title(String todo_title) {
        this.todo_title = todo_title;
    }

    public String getTodo_time() {
        return todo_time;
    }

    public void setTodo_time(String todo_time) {
        this.todo_time = todo_time;
    }
}
