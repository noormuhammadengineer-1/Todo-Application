package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Response;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Response> todoList = new ArrayList<>();
    private OnTodoClickListener listener;

    public interface OnTodoClickListener {
        void onEditClick(Response todo);
        void onDeleteClick(Response todo);
    }

    public void setOnTodoClickListener(OnTodoClickListener listener) {
        this.listener = listener;
    }

    public void setTodoList(List<Response> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Response todo = todoList.get(position);
        holder.tvTitle.setText(todo.getTitle());
        holder.cbCompleted.setChecked(todo.isCompleted());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(todo);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(todo);
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        CheckBox cbCompleted;
        ImageButton btnEdit, btnDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
