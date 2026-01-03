package com.example.ProductMicroServices.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reviews")
@Data
@Builder
@Getter
@Setter
public class Review {
    @Id
    private String id;

    @Indexed
    private int productId;

    private String userId;

    private String userName;

    private Integer rating;

    private String comment;

    private Date createdAt;
}