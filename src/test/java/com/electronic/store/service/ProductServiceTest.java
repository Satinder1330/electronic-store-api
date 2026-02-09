package com.electronic.store.service;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    Product product;
    Product product1;
    List<Product> list;
    Category category;
    List<Category>categoryList;
     @BeforeEach
    public void init(){
         category= Category.builder().categoryId("123").title("Electronics").description("All electronics stuff").coverImage("abc.jpg").build();
        categoryList=List.of(category);
         product = Product.builder().productName("Iphone").productDescription("Blue color").price(999.0).discountedPrice(900.0).quantity(10)
                                    .addedDate(new Date()).live(true).productImage("abc.jpg").categories(categoryList).build();
         product1 = Product.builder().productName("Samsung").productDescription("Blue color")
                 .price(1999.0).discountedPrice(1900.0).quantity(20).categories(categoryList).build();
   list = List.of(product1,product);

     }

     @Test
    public void addProductTest(){
         Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
         ProductDto map = mapper.map(product, ProductDto.class);
         ProductDto productDto = productService.addProduct(map);
         Assertions.assertEquals(product.getProductName(),productDto.getProductName());
     }
     @Test
    public void updateCategoryTest(){
         String id ="213";
         ProductDto macBook = ProductDto.builder().productId("456").productName("MacBook").categoryNames(Set.of("Iphone")).categoryIds(Set.of("46")).build();
         Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product1));
         Mockito.when(productRepository.save(Mockito.any())).thenReturn(product1);
         Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
         ProductDto productDto = productService.updateProduct(macBook, id);
         System.out.println(productDto.getPrice());
         Assertions.assertEquals("MacBook",productDto.getProductName());
     }
     @Test
    public void deleteProductTest(){
         String id = "32";
         Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
         productService.deleteProduct(id);
         Mockito.verify(productRepository,Mockito.times(1)).delete(product);

     }
     @Test
    public void getOneTest(){
         String id ="244";
         Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
         ProductDto productDto = productService.getOne(id);
         Assertions.assertEquals(product.getProductName(),productDto.getProductName());
     }
     @Test
    public  void  allProductsTest(){
         Page <Product>page =new PageImpl<>(list);
         Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
         CustomPaginationResponse<ProductDto> productDtoCustomPaginationResponse = productService.allProduct(0, 2, "name", "Asc");
         long totalElements = productDtoCustomPaginationResponse.getTotalElements();
         Assertions.assertEquals(2,totalElements);
     }
     @Test
    public void getByProductNameTest(){
         String subName="abc";
         Page <Product>page =new PageImpl<>(list);
         Mockito.when(productRepository.findByProductNameContainingIgnoreCase(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
         CustomPaginationResponse<ProductDto> byProductName = productService.getByProductName(subName, 0, 2, "name", "Asc");
         long totalElements = byProductName.getTotalElements();
         Assertions.assertEquals(2,totalElements);
     }
@Test
    public void addProductInCategoryTest(){
    Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
    ProductDto macBook = ProductDto.builder().productId("456").productName("MacBook").categoryNames(Set.of("Iphone")).categoryIds(Set.of("46")).build();
Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
    ProductDto productDto = productService.addProductInCategory("45", macBook);
    for (String categoryName : productDto.getCategoryNames()) {
        System.out.println(categoryName);
        Assertions.assertEquals("Electronics",categoryName);
    }
    int size = productDto.getCategoryIds().size();
    Assertions.assertEquals(1,size);

}
@Test
    public void allProductsInCategoryTest(){
    Page <Product>page =new PageImpl<>(list);
    Mockito.when(productRepository.findAllByCategories_CategoryId(Mockito.any(Pageable.class),Mockito.anyString())).thenReturn(page);
    CustomPaginationResponse<ProductDto> response = productService.allProductsInACategory("32", 0, 2, "name", "Asc");
    long totalElements = response.getTotalElements();
    Assertions.assertEquals(2,totalElements);

}

}
