package com.electronic.store.repositories;

import com.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product> findByProductNameContainingIgnoreCase(String subName , Pageable pageable);
    Page<Product>findByLiveTrue(Pageable pageable);
    Page<Product> findAllByCategories_CategoryId(Pageable pageable,String categoryId);


}
