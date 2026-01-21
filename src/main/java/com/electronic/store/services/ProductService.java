package com.electronic.store.services;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.helper.CustomPaginationResponse;


public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto , String productId);
    void deleteProduct(String productId);
    ProductDto getOne(String productId);
    CustomPaginationResponse<ProductDto>allProduct(int pageNumber,int pageSize,String sortBy,String sortDir);
    CustomPaginationResponse<ProductDto> getByProductName(String subName,int pageNumber,int pageSize,String sortBy,String sortDir);
    CustomPaginationResponse<ProductDto>allLive(int pageNumber,int pageSize,String sortBy,String sortDir);
    ProductDto addProductWithCategory(ProductDto productDto);
    ProductDto addProductInCategory(String categoryId, ProductDto productDto);
    CustomPaginationResponse<ProductDto>allProductsInACategory(String categoryId, int pageNumber,int pageSize,String sortBy,String sortDir);
    ProductDto assignProductToCategory(String categoryId, String productId);


}
