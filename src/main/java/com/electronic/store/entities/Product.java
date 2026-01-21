package com.electronic.store.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    private String productId;
    @Column(length = 30,nullable = false)
    private String productName;
    @Column(length = 1000,nullable = false)
    private String productDescription;

    private Double price;
    private Double discountedPrice;
    private Integer quantity;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date addedDate;
    private Boolean live;
    private  Boolean stock;

    private String productImage;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_categories")
    private List<Category>categories = new ArrayList<>();

}
