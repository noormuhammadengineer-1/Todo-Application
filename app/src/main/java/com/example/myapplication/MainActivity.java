package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ViewModels.TodosViewModel;
import com.example.myapplication.adapters.TodoAdapter;
import com.example.myapplication.model.Response;
import com.example.myapplication.utils.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoClickListener {

    private TodosViewModel todosViewModel;
    private TodoAdapter todoAdapter;
    private RecyclerView rvTodos;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private List<Response> currentTodoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();
        initViewModel();
    }

    private void initUI() {
        rvTodos = findViewById(R.id.rvTodos);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAdd);

        todoAdapter = new TodoAdapter();
        todoAdapter.setOnTodoClickListener(this);
        rvTodos.setAdapter(todoAdapter);

        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> showTodoDialog(null));
        }
    }

    private void initViewModel() {
        todosViewModel = new ViewModelProvider(this).get(TodosViewModel.class);

        todosViewModel.getTodos().observe(this, new Observer<Resource<List<Response>>>() {
            @Override
            public void onChanged(Resource<List<Response>> resource) {
                if (resource != null) {
                    switch (resource.state) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d("API_STATUS", "Loading data...");
                            break;

                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            if (resource.data != null) {
                                currentTodoList = new ArrayList<>(resource.data);
                                todoAdapter.setTodoList(currentTodoList);
                                Log.d("API_STATUS", "Success! Total items: " + resource.data.size());
                            }
                            break;

                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            String errorMsg = resource.message;
                            Log.e("API_STATUS", "Error: " + errorMsg);
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
    }

    private void showTodoDialog(Response todoToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(todoToEdit == null ? "Add Todo" : "Edit Todo");

        final EditText input = new EditText(this);
        input.setHint("Enter Todo Title");
        if (todoToEdit != null) {
            input.setText(todoToEdit.getTitle());
        }
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = input.getText().toString().trim();
            if (!title.isEmpty()) {
                if (todoToEdit == null) {
                    createTodo(1, title);
                } else {
                    updateTodo(todoToEdit.getId(), title, todoToEdit.isCompleted());
                }
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void createTodo(int userId, String title) {
        Response newTodo = new Response();
        newTodo.setUserId(userId);
        newTodo.setTitle(title);
        newTodo.setCompleted(false);

        todosViewModel.createTodo(newTodo).observe(this, resource -> {
            if (resource != null) {
                switch (resource.state) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Created! ID: " + resource.data.getId(), Toast.LENGTH_SHORT).show();
                        // Local update (Mock API doesn't persist)
                        currentTodoList.add(0, resource.data);
                        todoAdapter.setTodoList(currentTodoList);
                        rvTodos.scrollToPosition(0);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Create Error: " + resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateTodo(int id, String title, boolean completed) {
        Response updatedTodo = new Response();
        updatedTodo.setId(id);
        updatedTodo.setUserId(1); 
        updatedTodo.setTitle(title);
        updatedTodo.setCompleted(completed);

        todosViewModel.updateTodo(id, updatedTodo).observe(this, resource -> {
            if (resource != null) {
                switch (resource.state) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        // Update local list
                        for (int i = 0; i < currentTodoList.size(); i++) {
                            if (currentTodoList.get(i).getId() == id) {
                                currentTodoList.set(i, resource.data);
                                break;
                            }
                        }
                        todoAdapter.setTodoList(currentTodoList);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Update Error: " + resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onEditClick(Response todo) {
        showTodoDialog(todo);
    }

    @Override
    public void onDeleteClick(Response todo) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this todo?")
                .setPositiveButton("Yes", (dialog, which) -> deleteTodo(todo.getId()))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteTodo(int id) {
        todosViewModel.deleteTodo(id).observe(this, resource -> {
            if (resource != null) {
                switch (resource.state) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        // Remove from local list
                        for (int i = 0; i < currentTodoList.size(); i++) {
                            if (currentTodoList.get(i).getId() == id) {
                                currentTodoList.remove(i);
                                break;
                            }
                        }
                        todoAdapter.setTodoList(currentTodoList);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Delete Error: " + resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
