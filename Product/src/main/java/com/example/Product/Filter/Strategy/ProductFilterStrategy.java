package com.example.Product.Filter.Strategy;

import com.example.Product.DTO.FilterProductsDTO;
import com.example.Product.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;

public interface ProductFilterStrategy {
    FilterStrategy supports();
    Criteria buildCriteria(FilterProductsDTO dto);
}
