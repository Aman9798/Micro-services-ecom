package com.example.ProductMicroServices.Filter;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Filter.ProductFilterContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    private final MongoTemplate mongoTemplate;
    private final ProductFilterContext filterContext;

    public ProductQueryService(MongoTemplate mongoTemplate,
                               ProductFilterContext filterContext) {
        this.mongoTemplate = mongoTemplate;
        this.filterContext = filterContext;
    }

    public List<Product> filterProducts(FilterProductsDTO dto) {

        Criteria criteria = filterContext.buildCriteria(dto);
        Query query = new Query(criteria);

        applySorting(dto, query);

        return mongoTemplate.find(query, Product.class);
    }

    private void applySorting(FilterProductsDTO dto, Query query) {

        if (dto.getSortBy() == null || dto.getSortBy().isBlank()) {
            query.with(Sort.by(Sort.Direction.ASC, "price"));
            return;
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(dto.getSortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        switch (dto.getSortBy().toLowerCase()) {
            case "price":
                query.with(Sort.by(direction, "price"));
                break;
            case "name":
                query.with(Sort.by(direction, "name"));
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid sort field: " + dto.getSortBy()
                );
        }
    }
}
