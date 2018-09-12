package com.example.layou.todo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layou.todo.adapters.TodoAdapter;
import com.example.layou.todo.models.Todo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements TodoAdapter.OnSelectedTodo, TodoAdapter.OnDoneTodo{

    private EditText inputTodoDescription;
    private ImageButton btnSaveTodo;
    private Button btnCleanTodo;
    private ListView todoList;
    private TodoAdapter todoAdapter;
    private List<Todo> listTodo;
    private Todo selectedTodo;
    private DatabaseReference dataBaseTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listTodo = new ArrayList<>();
        dataBaseTodo = FirebaseDatabase.getInstance().getReference("todos");
        todoList = (ListView) findViewById(R.id.list_todo);
        inputTodoDescription = (EditText) findViewById(R.id.input_description);
        btnSaveTodo = (ImageButton) findViewById(R.id.btn_save);
        btnCleanTodo = (Button) findViewById(R.id.btn_clear_description);

        btnCleanTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTodo = null;
                inputTodoDescription.setText("");
            }
        });

        btnSaveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean todoIsSelected = selectedTodo != null;

                if (todoIsSelected) {
                    updateTodo(
                        selectedTodo.getTodoId(),
                        inputTodoDescription.getText().toString().trim()
                    );
                } else {
                    addTodo();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Activity activity = this;
        dataBaseTodo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todoAdapter = new TodoAdapter(getApplicationContext(), activity);

                todoAdapter.cleanList();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Todo todo = snapshot.getValue(Todo.class);
                    todoAdapter.addItem(todo);
                }

                todoList.setAdapter(todoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSelectedTodo(Todo todo) {
        selectedTodo = todo;
        inputTodoDescription.setText(selectedTodo.getTodoDescription());
    }

    @Override
    public void onDondeTodo(Todo todo) {
        deleteTodo(todo.getTodoId());
        inputTodoDescription.setText("");
    }

    private void addTodo() {
        String todoDescription = inputTodoDescription.getText().toString().trim();
        boolean isEmpty = TextUtils.isEmpty(todoDescription);

        if (!isEmpty) {

            String id = dataBaseTodo.push().getKey();

            Todo todo = new Todo(id, todoDescription);

            dataBaseTodo.child(id).setValue(todo);

            inputTodoDescription.setText("");

            Toast.makeText(
                this,
                "Todo added",
                Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(
                this,
                "Please enter a description for the todo",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private boolean updateTodo(String id, String newDescription) {
        boolean isEmpty = TextUtils.isEmpty(newDescription);
        inputTodoDescription.setText("");
        selectedTodo = null;
        if (!isEmpty) {
            DatabaseReference databaseReference =
                    FirebaseDatabase.getInstance().getReference("todos").child(id);

            Todo todo = new Todo(id, newDescription);
            databaseReference.setValue(todo);

            Toast.makeText(
                getApplicationContext(),
                "Todo Updated",
                Toast.LENGTH_SHORT
            ).show();

            return true;
        } else {
            Toast.makeText(
                this,
                "No found description for the Todo",
                Toast.LENGTH_LONG
            ).show();
            return false;
        }
    }

    private boolean deleteTodo(String id) {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference("todos").child(id);
        databaseReference.removeValue();

        Toast.makeText(
            getApplicationContext(),
            "Todo Deleted",
            Toast.LENGTH_SHORT
        ).show();

        return true;
    }

}
