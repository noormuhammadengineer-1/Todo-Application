package com.example.myapplication.utils;

public class Resource<T> {
    public enum State{SUCCESS,ERROR,LOADING}

    public State state;
    public T data;
    public String message;

    private Resource(State state,T data,String message)
    {
        this.state=state;
        this.data=data;
        this.message=message;
    }
    public static <T> Resource<T> success(T data){
        return new Resource<>(State.SUCCESS,data,null);
    }
    public static <T> Resource<T> error(String message){
        return new Resource<>(State.ERROR,null,message);
    }
    public static <T> Resource<T> loading(){
        return new Resource<>(State.LOADING,null,null);
    }

}
