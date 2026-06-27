package com.example.myapplication.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Repository.TodoRepository;
import com.example.myapplication.model.Response;
import com.example.myapplication.utils.Resource;

import java.util.List;

public class TodosViewModel extends ViewModel {
    private  TodoRepository todoRepository;
    private LiveData<Resource<List<Response>>> todoList;

    public TodosViewModel(){
        todoRepository=new TodoRepository();
    }


    public LiveData<Resource<List<Response>>> getTodos() {
        if (todoList == null) {
            todoList = todoRepository.fetchAllTodos();
        }
        return todoList;
    }

    public LiveData<Resource<Response>> createTodo(Response todo) {
        return todoRepository.createNewTodo(todo);
    }

    public LiveData<Resource<Response>> updateTodo(int id, Response todo) {
        return todoRepository.updateNewTodo(id, todo);
    }

    public LiveData<Resource<Response>> deleteTodo(int id) {
        return todoRepository.deleteTodo(id);
    }
}
