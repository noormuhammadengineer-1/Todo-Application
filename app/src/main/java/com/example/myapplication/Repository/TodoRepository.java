package com.example.myapplication.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.interfaces.ApiService;
import com.example.myapplication.model.Response;
import com.example.myapplication.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoRepository {
    private ApiService apiService;


    public TodoRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Yeh line missing hogi aapki:
        apiService = retrofit.create(ApiService.class);
    }
    public MutableLiveData<Resource<Response>> deleteTodo(int id) {
        MutableLiveData<Resource<Response>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.deleteTodo(id).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Server Error Code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                result.setValue(Resource.error(t.getLocalizedMessage()));
            }
        });
        return result;
    }


    public MutableLiveData<Resource<Response>> updateNewTodo(int id, Response todo){
        MutableLiveData<Resource<Response>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());
        apiService.updateTodo(id, todo).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Server Error Code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                result.setValue(Resource.error(t.getLocalizedMessage()));
            }
        });
        return result;

    }
    public MutableLiveData<Resource<Response>> createNewTodo(Response todo) {
        MutableLiveData<Resource<Response>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.createTodo(todo).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Server Error Code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                result.setValue(Resource.error(t.getLocalizedMessage()));
            }
        });
        return result;


    }

    public MutableLiveData<Resource<List<Response>>> fetchAllTodos() {
        MutableLiveData<Resource<List<Response>>> data = new MutableLiveData<>();

        data.setValue(Resource.loading());
        apiService.getAllTodos().enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error("Server Error Code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                // 3. NETWORK ERROR: Internet nahi hai, timeout ho gaya, ya bad connection
                String errorMessage = "No Internet Connection!";
                if (t.getMessage() != null) {
                    errorMessage = t.getLocalizedMessage();
                }
                data.setValue(Resource.error(errorMessage));

            }
        });
        return data;
    }
}
