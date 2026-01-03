package com.example.Product.Exception;

public class NoSuchBrandException extends RuntimeException{
        public NoSuchBrandException(String message){
            super(message);
        }
}
