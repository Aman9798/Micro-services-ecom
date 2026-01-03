package com.example.Product.Exception;

public class UnauthorizedAccess extends RuntimeException{
    public UnauthorizedAccess(String message){
        super(message);
    }
}
