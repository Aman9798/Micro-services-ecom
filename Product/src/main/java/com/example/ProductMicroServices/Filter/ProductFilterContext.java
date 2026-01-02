package com.example.ProductMicroServices.Filter;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.Entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductFilterContext {
    private final Map<String, ProductFilterStrategy> strategies;

    public ProductFilterContext(List<ProductFilterStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(s -> s.getClass().getSimpleName(), s -> s));
    }

    public Criteria buildCriteria(FilterProductsDTO dto) {

        Criteria combined = new Criteria();
        boolean hasCriteria = false;

        for (ProductFilterStrategy strategy : strategies.values()) {
            Criteria criteria = strategy.buildCriteria(dto);
            if (criteria != null) {
                combined = hasCriteria
                        ? new Criteria().andOperator(combined, criteria)
                        : criteria;
                hasCriteria = true;
            }
        }

        return hasCriteria ? combined : new Criteria();
    }

}
