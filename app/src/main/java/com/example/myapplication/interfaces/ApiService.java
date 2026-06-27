package com.example.myapplication.interfaces;

import com.example.myapplication.model.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("todos/{id}")
    Call<Response> getTodo(@Path("id") int id);

    @GET("todos")
    Call<List<Response>> getAllTodos();

    @POST("todos")
    Call<Response> createTodo(@Body Response newTodo);

    @PUT("todos/{id}")
    Call<Response> updateTodo(@Path("id") int id, @Body Response updatedTodo);

    @DELETE("todos/{id}")
    Call<Response> deleteTodo(@Path("id") int id);


}
