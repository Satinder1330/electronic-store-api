package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;
    @Column(name = "category_title",length = 25,nullable = false)
    private String title;
    @Column(name = "category_description",length = 200,nullable = false)
    private String description;
    @Column(name = "category_image")
    private String coverImage;
    @ManyToMany(mappedBy = "categories",fetch = FetchType.LAZY)
    private List<Product>products = new ArrayList<>();
}
