package com.example.ProductMicroServices.Filter.Strategy;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;

public interface ProductFilterStrategy {
    FilterStrategy supports();
    Criteria buildCriteria(FilterProductsDTO dto);
}
