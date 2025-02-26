package com.example.ProductMicroServices.Exception;

public class NoSuchBrandException extends RuntimeException{
        public NoSuchBrandException(String message){
            super(message);
        }
}
