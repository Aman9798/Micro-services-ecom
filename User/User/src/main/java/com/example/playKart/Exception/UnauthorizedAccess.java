package com.example.playKart.Exception;

public class UnauthorizedAccess extends RuntimeException{
    public UnauthorizedAccess(String message){
        super(message);
    }
}
