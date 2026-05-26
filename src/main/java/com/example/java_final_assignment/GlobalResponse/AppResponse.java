package com.example.java_final_assignment.GlobalResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppResponse<T>{

    private int statusCode;
    private String message;

    private T data;

    public AppResponse(T data){
        this.statusCode = 200;
        this.message = "Success";
        this.data = data;
    }

    public AppResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public AppResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }


}
