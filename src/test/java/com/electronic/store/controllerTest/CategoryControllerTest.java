package com.electronic.store.controllerTest;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.ProductService;
import com.electronic.store.services.imageService.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private ProductService productService;
    @MockitoBean(name = "categoryImageService")
    private ImageService imageService;
    @Autowired
            private MockMvc mockMvc;

    CategoryDto categoryDto;
    ProductDto productDto;

    @BeforeEach
    public void init(){
        categoryDto=CategoryDto.builder().categoryId("3r6t7f").title("category").description("top 5").coverImage("img.jpg").build();

        productDto =ProductDto.builder().productId("2r43").productName("product").productDescription("good one").price(555.3).discountedPrice(499.0).quantity(2).addedDate(new Date()).live(true).stock(true).productImage("img123.jpg").categoryIds(Set.of("3r6t7f")).build();
    }

    @Test
    public void addTest() throws Exception {
        Mockito.when(categoryService.addCategory(Mockito.any())).thenReturn(categoryDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/category/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJsonStringConvertor(categoryDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("category"));
    }

    private String objectToJsonStringConvertor(CategoryDto categoryDto) {
        try {
            return new ObjectMapper().writeValueAsString(categoryDto);
        }catch (Exception e){
            e.printStackTrace();
        }return null;
    }
}
