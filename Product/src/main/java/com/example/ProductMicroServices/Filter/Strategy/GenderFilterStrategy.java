package com.example.ProductMicroServices.Filter.Strategy;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GenderFilterStrategy implements ProductFilterStrategy {

    @Override
    public FilterStrategy supports() {
        return FilterStrategy.Gender;
    }

    @Override
    public Criteria buildCriteria(FilterProductsDTO dto) {

        if (dto.getGender() == null || dto.getGender().isBlank()) {
            return null;
        }

        // Case-insensitive exact match
        Pattern pattern = Pattern.compile(
                "^" + Pattern.quote(dto.getGender()) + "$",
                Pattern.CASE_INSENSITIVE
        );

        return Criteria.where("gender").regex(pattern);
    }
}
