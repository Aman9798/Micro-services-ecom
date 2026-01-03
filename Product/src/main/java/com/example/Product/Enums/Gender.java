package com.example.Product.Enums;

import com.example.Product.Exception.NoSuchGenderException;

public enum Gender {
    Men,
    Women,
    Kids;

    public static Gender isValidGender(String productGender){
        for(Gender gender:Gender.values()){
            String comp = gender.toString();
            if(comp.equals(productGender)){
                return gender;
            }
        }
        throw new NoSuchGenderException("No such Gender is present");

    }
}
