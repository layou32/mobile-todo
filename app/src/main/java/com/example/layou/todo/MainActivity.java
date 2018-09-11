package com.example.layou.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layou.todo.adapters.TodoAdapter;
import com.example.layou.todo.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnSelectedTodo{
    private EditText inputTodoDescription;
    private ImageButton btnSaveTodo;
    private ListView todoList;
    private TodoAdapter todoAdapter;
    private List<Todo> listTodo = new ArrayList<>();
    private Todo selectedTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = (ListView) findViewById(R.id.list_todo);
        inputTodoDescription = (EditText) findViewById(R.id.input_description);
        btnSaveTodo = (ImageButton) findViewById(R.id.btn_save);

        btnSaveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean todoIsSelected = selectedTodo != null;

                if (todoIsSelected) {
                    String text = selectedTodo.getTodoDescription();
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    final boolean isUpdate = selectedTodo.getTodoId() > -1;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        todoAdapter = new TodoAdapter(this, this);

        Todo todo = new Todo(1, "prueba 2");
        todoAdapter.addItem(todo);

        todo = new Todo(2, "prueba 3");
        todoAdapter.addItem(todo);

        todoList.setAdapter(todoAdapter);
    }

    @Override
    public void onSelectedTodo(Todo todo) {
        selectedTodo = todo;
        inputTodoDescription.setText(selectedTodo.getTodoDescription());
    }
}
