package com.example.ProductMicroServices.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewDTO {

    private int productId;

    private String userName;

    private Integer rating;

    private String comment;
}
