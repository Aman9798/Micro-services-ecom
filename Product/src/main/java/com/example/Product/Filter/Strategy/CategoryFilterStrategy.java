package com.example.Product.Filter.Strategy;

import com.example.Product.DTO.FilterProductsDTO;
import com.example.Product.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CategoryFilterStrategy implements ProductFilterStrategy {

    @Override
    public FilterStrategy supports() {
        return FilterStrategy.Category;
    }

    @Override
    public Criteria buildCriteria(FilterProductsDTO dto) {

        if (dto.getCategory() == null || dto.getCategory().isBlank()) {
            return null;
        }

        // Case-insensitive exact match
        Pattern pattern = Pattern.compile(
                "^" + Pattern.quote(dto.getCategory()) + "$",
                Pattern.CASE_INSENSITIVE
        );

        return Criteria.where("category").regex(pattern);
    }
}
