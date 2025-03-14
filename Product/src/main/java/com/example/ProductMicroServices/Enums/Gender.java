package com.example.ProductMicroServices.Enums;

import com.example.ProductMicroServices.Exception.NoSuchBrandException;

public enum Gender {
    Men,
    Women,
    Kids;
    public Gender isValidGender(String productGender){
        for(Gender gender:Gender.values()){
            String comp = gender.toString();
            if(comp.equals(productGender)){
                return gender;
            }
        }
        throw new NoSuchBrandException("No such Gender is present");

    }
}
