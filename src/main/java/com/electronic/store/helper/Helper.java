package com.electronic.store.helper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;
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
}
