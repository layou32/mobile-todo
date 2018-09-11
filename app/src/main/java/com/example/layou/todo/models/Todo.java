package com.example.layou.todo.models;

public class Todo {
    public int TodoId;
    public String TodoDescription;

    public Todo(int TodoId, String TodoDescription) {
        this.TodoId = TodoId;
        this.TodoDescription = TodoDescription;
    }

    public int getTodoId() {
        return TodoId;
    }

    public String getTodoDescription() {
        return TodoDescription;
    }
}
