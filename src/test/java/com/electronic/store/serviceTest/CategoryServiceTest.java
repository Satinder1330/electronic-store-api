package com.electronic.store.serviceTest;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.entities.Category;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {

    @MockitoBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
            private ModelMapper mapper;
    Category category;
    Category category1;
    List<Category> list;

    @BeforeEach
    public void init(){
        category=Category.builder().title("Electronics").description("All electronics stuff").coverImage("abc.jpg").build();
        category1=Category.builder().title("TVs").description("All TVs stuff").coverImage("img122.jpg").build();
       list = List.of(category,category1);
    }

    @Test
    public void addCategoryTest(){
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto categoryDto1 = mapper.map(category, CategoryDto.class);
        CategoryDto categoryDto = categoryService.addCategory(categoryDto1);
        System.out.println(category.getTitle()+" "+categoryDto.getTitle());
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle(),"Titles are different!!");
    }

    @Test
    public void updateCategoryTest(){
        String id ="123";
       CategoryDto categoryDto = new CategoryDto();
       categoryDto.setTitle("updated Category");
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        categoryService.updateCategory(categoryDto,id);
        System.out.println(category.getTitle()+" "+ categoryDto.getTitle());
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle());
    }
    @Test
    public  void getOneTest(){
        String id ="123";
        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
        CategoryDto categoryDto = categoryService.getOne(id);
        String expected = "Electronics";
        Assertions.assertEquals(expected,categoryDto.getTitle());
    }
    @Test
    public void getAllTest(){
        Page<Category> page = new PageImpl<>(list);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        CustomPaginationResponse<CategoryDto> all = categoryService.getAll(0, 2, "name", "Asc");
        Assertions.assertEquals(2,all.getContent().size());
    }
    @Test
    public void deleteCategoryTest(){
        String id ="123";
        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
        categoryService.delete(id);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }
    @Test
    public void findAllByTitle(){
        String title = "Tvs";
        Mockito.when(categoryRepository.findAllByTitle(title)).thenReturn(List.of(category1));
        List<CategoryDto> allByTitle = categoryService.findAllByTitle(title);
        Assertions.assertEquals("TVs",allByTitle.get(0).getTitle());
    }



}
