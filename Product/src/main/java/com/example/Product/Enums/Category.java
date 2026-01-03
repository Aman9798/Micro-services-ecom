package com.example.Product.Enums;

import com.example.Product.Exception.NoSuchCategoryException;

public enum Category {
    SportsWears,
    SportsEquipments;

    public static Category isValidCategory(String category){
        for(Category categories:Category.values()){
            String cat = categories.toString();
            if(cat.equals(category)){
                return categories;
            }
        }

        throw new NoSuchCategoryException("No such category is present");
    }

}
