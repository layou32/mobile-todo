package com.example.layou.todo.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Todo {
    public String todoId;
    public String todoDescription;

    public Todo() {
    }

    public Todo(String todoId, String todoDescription) {
        this.todoId = todoId;
        this.todoDescription = todoDescription;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getTodoDescription() {
        return todoDescription;
    }
}
