package com.example.Product.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewRequestDTO {

    private int productId;

    private String userName;

    private Integer rating;

    private String comment;
}
