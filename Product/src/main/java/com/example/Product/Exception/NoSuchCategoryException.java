package com.example.Product.Exception;

public class NoSuchCategoryException extends RuntimeException {
    public NoSuchCategoryException(String message){
        super(message);
    }
}
