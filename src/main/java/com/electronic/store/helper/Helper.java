package com.electronic.store.helper;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Helper {
//custom pagination response mapping
    public <T>CustomPaginationResponse<T> getPaginatioResponse(Page<?> page, List<T> list, int pageNumber, int pageSize){

        CustomPaginationResponse<T> response = new CustomPaginationResponse<>();
        response.setContent(list);
        response.setLastPage(page.isLast());
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return  response;
    }

public void categoryToProductDto(Product product, ProductDto map){
    Set<String>ids=new HashSet<>();
    Set<String>names=new HashSet<>();
    List<Category> categories = product.getCategories();
    categories.forEach(category -> {
        ids.add(category.getCategoryId());
        names.add(category.getTitle());
    });
    map.setCategoryNames(names);
    map.setCategoryIds(ids);
}

}
