package com.example.Product.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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