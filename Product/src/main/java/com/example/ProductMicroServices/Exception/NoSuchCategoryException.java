package com.example.ProductMicroServices.Exception;

public class NoSuchCategoryException extends RuntimeException {
    public NoSuchCategoryException(String message){
        super(message);
    }
}
