package com.example.Product.Filter.Strategy;

import com.example.Product.DTO.FilterProductsDTO;
import com.example.Product.Enums.FilterStrategy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class PriceFilterStrategy implements ProductFilterStrategy {

    @Override
    public FilterStrategy supports() {
        return FilterStrategy.Price;
    }

    @Override
    public Criteria buildCriteria(FilterProductsDTO dto) {

        Long minPrice = dto.getMinPrice();
        Long maxPrice = dto.getMaxPrice();

        if (minPrice == null && maxPrice == null) {
            return null;
        }

        Criteria priceCriteria = Criteria.where("price");

        if (minPrice != null && maxPrice != null) {
            return priceCriteria.gte(minPrice).lte(maxPrice);
        }

        if (minPrice != null) {
            return priceCriteria.gte(minPrice);
        }

        return priceCriteria.lte(maxPrice);
    }
}
