package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.helper.CustomPaginationResponse;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);
    CategoryDto getOne(String categoryId);
    CustomPaginationResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    void delete(String categoryId);
    List<CategoryDto> findAllByTitle(String title);


}
