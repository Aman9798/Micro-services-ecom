package com.example.ProductMicroServices.Enums;

import com.example.ProductMicroServices.Exception.NoSuchCategoryException;

public enum Category {
    SportsWears,
    SportsEquipments;

    public Category isValidCategory(String category){
        for(Category categories:Category.values()){
            String cat = categories.toString();
            if(cat.equals(category)){
                return categories;
            }
        }

        throw new NoSuchCategoryException("No such category is present");
    }

}
