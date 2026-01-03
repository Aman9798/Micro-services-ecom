package com.example.Cart.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


import java.util.Random;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 255 , message = "name cannot be more than 100 characters")
    private String name;

    @Size(min = 2, max = 500 , message = "name cannot be more than 1000 characters")
    private String description;

    private Long price;

    private Long stock;

    private String category;

    private String brand;

    private String imageURL;

    private String gender;

    public void setId() {
        Random random = new Random();
        this.id = random.nextInt(1000000);
    }
}