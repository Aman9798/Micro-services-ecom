package com.example.ProductMicroServices.DTO;

import jakarta.validation.constraints.Min;

public class FilterProductsDTO {


    @Min(value = 0,message = "minimum price should be at least 0")
    private Long minPrice;

    @Min(value = 0,message = "minimum price should be at least 0")
    private Long maxPrice;

    private static String brand;

    private String searchTerm;

    private String sortBy;

    private String sortDirection;

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public static String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
