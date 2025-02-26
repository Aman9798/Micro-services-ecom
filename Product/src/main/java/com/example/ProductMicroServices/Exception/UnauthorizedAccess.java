package com.example.ProductMicroServices.Exception;

public class UnauthorizedAccess extends RuntimeException{
    public UnauthorizedAccess(String message){
        super(message);
    }
}
