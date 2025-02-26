package com.example.ProductMicroServices.Enums;

import com.example.ProductMicroServices.Exception.NoSuchBrandException;

public enum Brand {
    Nike,
    Adidas,
    Puma,
    Reebok;

    public Brand isValidBrand(String productBrand){
        for(Brand brands:Brand.values()){
            String brand = brands.toString();
            if(brand.equals(productBrand)){
                return brands;
            }
        }
        throw new NoSuchBrandException("No such Brand is present");

    }

}


