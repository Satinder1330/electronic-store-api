package com.electronic.store.controllers;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.helper.ApiCustomResponse;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.ProductService;
import com.electronic.store.services.imageService.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Categories Controller",description = "APIs for Categories")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    private ImageService imageService;

    public CategoryController(@Qualifier("categoryImageService") ImageService imageService) {
        this.imageService = imageService;
    }

    @Value("${category.profile.image.path}")
    private String path;

    @PostMapping("/add")
    public ResponseEntity<CategoryDto>add(@RequestBody @Valid CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiCustomResponse>delete(@PathVariable String id){
        categoryService.delete(id);
        return  new ResponseEntity<>(new ApiCustomResponse("Category of the given id has been deleted successfully",HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto>update(@RequestBody CategoryDto categoryDto,@PathVariable String id){
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, id);
        return new ResponseEntity<>(categoryDto1,HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CustomPaginationResponse>getAll(@RequestParam int pageNumber,
                                                          @RequestParam int pageSize,
                                                          @RequestParam String sortBy,
                                                          @RequestParam String sortDir){
        CustomPaginationResponse<CategoryDto> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @GetMapping("/getByTitle/{title}")
    public ResponseEntity<List<CategoryDto>>getByName(@PathVariable String title){
        List<CategoryDto> allByTitle = categoryService.findAllByTitle(title);
        return new ResponseEntity<>(allByTitle,HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<ImageApiResponse>upload(@RequestParam("image") MultipartFile image , @PathVariable String id ) throws IOException {
        ImageApiResponse imageApiResponse = imageService.uploadImage(image, path, id);
        CategoryDto categoryDto = categoryService.getOne(id);
        String coverImage = categoryDto.getCoverImage();
        return new ResponseEntity<>(new ImageApiResponse(coverImage,"Image is uploaded successfully",HttpStatus.OK),HttpStatus.OK);
    }

@GetMapping("/getImage/{id}")
    public ResponseEntity<HttpServletResponse>getImage(@PathVariable String id,HttpServletResponse response) throws IOException {
    CategoryDto categoryDto = categoryService.getOne(id);
    String coverImage = categoryDto.getCoverImage();
    InputStream resource = imageService.getResource(path, coverImage);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource,response.getOutputStream());
    return new ResponseEntity<>(response,HttpStatus.OK);
}

@PostMapping("/{catogoryId}/product")
public  ResponseEntity<ProductDto>addProduct(@PathVariable String catogoryId,
                                             @RequestBody ProductDto productDto){
    ProductDto productDto1 = productService.addProductInCategory(catogoryId, productDto);
    return  new ResponseEntity<>(productDto1,HttpStatus.OK);
}

//list of all the products in a category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CustomPaginationResponse> allProducts(@PathVariable String categoryId,
                                                                @RequestParam int pageNumber,
                                                                @RequestParam int pageSize,
                                                                @RequestParam String sortBy,
                                                                @RequestParam String sortDir){
        CustomPaginationResponse<ProductDto> response = productService.allProductsInACategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

    //assign product to a category
    @PutMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDto>assignProduct(@PathVariable String categoryId,
                                                   @PathVariable String productId){
        ProductDto productDto = productService.assignProductToCategory(categoryId, productId);
        return  new ResponseEntity<>(productDto,HttpStatus.OK);
    }
}
