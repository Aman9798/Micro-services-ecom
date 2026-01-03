package com.example.Product.Enums;

import com.example.Product.Exception.NoSuchBrandException;

public enum Brand {
    Nike,
    Adidas,
    Puma,
    Reebok;

    public static Brand isValidBrand(String productBrand){
        for(Brand brand : Brand.values()){
            String brandString = brand.toString();
            if(brandString.equals(productBrand)){
                return brand;
            }
        }
        throw new NoSuchBrandException("No such Brand is present");

    }

}


