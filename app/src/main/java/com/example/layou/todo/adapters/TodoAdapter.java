package com.example.layou.todo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layou.todo.R;
import com.example.layou.todo.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends BaseAdapter {
    private List<Todo> listTodos = new ArrayList<>();
    private Todo selectedTodo;
    private Context context;
    private LayoutInflater inflater;
    OnSelectedTodo oCallback;

    public TodoAdapter(Context context, Activity activity) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oCallback = (OnSelectedTodo) activity;
    }

    public interface OnSelectedTodo {
        public void onSelectedTodo(Todo todo);
    }

    public void addItem(final Todo item) {
        listTodos.add(item);
        notifyDataSetChanged();
    }

    public void cleanList() {
        listTodos = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listTodos.size();
    }

    @Override
    public Todo getItem(int position) {
        return listTodos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTodos.get(position).getTodoId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        final Todo todo = listTodos.get(position);

        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.item_todo, null);
            holder.itemDescription = (TextView) view.findViewById(R.id.task_title);
            holder.doneButton = (Button) view.findViewById(R.id.task_delete);

            holder.itemDescription.setText(todo.getTodoDescription());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, todo.getTodoDescription(), Toast.LENGTH_LONG).show();
            }
        });

        holder.itemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTodo = listTodos.get(position);
                oCallback.onSelectedTodo(getItem(position));
            }
        });

        return view;
    }

    public class ViewHolder {
        public TextView itemDescription;
        public Button doneButton;
    }
}
