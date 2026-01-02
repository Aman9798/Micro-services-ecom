package com.example.ProductMicroServices.Filter;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SearchTermFilterStrategy implements ProductFilterStrategy {

    @Override
    public FilterStrategy supports() {
        return FilterStrategy.SearchTerm;
    }

    @Override
    public Criteria buildCriteria(FilterProductsDTO dto) {

        String searchTerm = dto.getSearchTerm();

        if (searchTerm == null || searchTerm.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                Pattern.quote(searchTerm),
                Pattern.CASE_INSENSITIVE
        );

        return new Criteria().orOperator(
                Criteria.where("name").regex(pattern),
                Criteria.where("description").regex(pattern),
                Criteria.where("brand").regex(pattern)
        );
    }
}
